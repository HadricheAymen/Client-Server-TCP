package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String ClientName;
    private BufferedReader in ;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
        	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        	out = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    BufferedReader getIn() {return this.in;}
    PrintWriter getOut() {return this.out;}
    String getClientName() {return this.ClientName;}
    Socket getClientSocket() {return this.clientSocket;}
    void setClientName(String name) {this.ClientName=name;}
    
    
    @Override  
    public void run() {
        try {
            // Get the initial username from the client
            boolean clientnameExist = true;
            do {
                String username = in.readLine(); // Read the username
                clientnameExist = Server.cmdhandler.setClientName(this, username); // Check if name exists
            } while (clientnameExist);

            // Add this client handler to the global list
            Server.clienthandlers.add(this);

            
            // Handle incoming messages from the client
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    // Handle commands such as /change username, /exit, etc.
                    Server.cmdhandler.handleCommande(this, message);
                } else {
                    // Handle messages
                    String nameExtracted = getWordAfterAt(message);
                    handleMessage(message, nameExtracted);
                    System.out.println("Message reçu du " + this.ClientName + " : " + message);
                }
            }

        } catch (IOException e) {
            System.out.println(ClientName + " has left the chat");
            
        }finally {
        	
        }
    }

    

    private void handleMessage(String message,String nameExtracted) {
    	Boolean clientExist = false;
    	String newMessage=cleanMessage(message);
    	if (nameExtracted=="-1") // -1 means this message is not private or this is a problem with the indicator
    	{
    		this.out.println("erreur syntaxe");
    	}
    	else 
    		if (nameExtracted=="0")
    		{
    		publicMessage(newMessage);
    		}
    		else {
    	{
	    for (ClientHandler clienthandler : Server.clienthandlers) 
	    	{    		
	    		if (clienthandler.getClientName().equals(nameExtracted)) 
	    		{
	    			
	    			clientExist = true;
	    			privateMessage(newMessage, clienthandler);
	    		}
	    	}
    	
	    if (clientExist==false)
	    	{
	    		this.out.println("System Notification: this username does not exist");
	    	}
    	}
    		}
    }
    private void privateMessage(String message,ClientHandler client) { 
    	//send message to specific client 
    	if (client.equals(this)) {
    		out.println("Me (to Me) :"+message);
    	}
    	else {
    	client.getOut().println(this.ClientName + " (private): " + message);
    	out.println("Me (to "+client.getClientName()+"): "+message);
    	}

    }
    private void publicMessage(String message) { 
    	//send message to all clients
    	for (ClientHandler clienthandler : Server.clienthandlers) {
    		
    		if (this.clientSocket.getPort()!= clienthandler.getClientSocket().getPort()) {
    			clienthandler.getOut().println(this.ClientName+": " + message); //send the message to other clients
    		}
    		else {
    			clienthandler.getOut().println("Me: "+ message); //send the message to the original sender
    		}
    	}
    }
    private String getWordAfterAt(String line) { 
        // Check if the line contains '@'
        int atIndex = line.indexOf('@');
        if (atIndex == -1) {
            return "0";
        }
        // Check if the line starts with '@'
        if (!line.startsWith("@"))
        	return "-1";
        
        // Find the word after '@'
        int start = atIndex + 1; // Move to the character after '@'
        
        // If '@' is the last character, no word exists
         if (start >= line.length()) {
            return "-1";
        } 
        
        // Extract the word after '@' until a space or the end of the string
        StringBuilder wordAfterAt = new StringBuilder();
        for (int i = start; i < line.length(); i++) {
            char ch = line.charAt(i);
            // Break if we encounter a space or special character
            if (Character.isWhitespace(ch)) {
                break;
            }
            wordAfterAt.append(ch);
        }

        // If no word is found after '@', return a message
        if (wordAfterAt.length() == 0) {
            return "-1";
        }
        return wordAfterAt.toString();
    }
    private String cleanMessage(String message) {
    	int place=0;
    	// Check if the line contains '@'
    	int atIndex = message.indexOf('@');
        if (atIndex == -1) {
            return message;
        }
    	StringBuilder wordAfterAt = new StringBuilder();
        for (place =0 ; place < message.length(); place++) {
            char ch = message.charAt(place);
            // Break if we encounter a space or special character
            if (Character.isWhitespace(ch)) {
                break;
          }
            wordAfterAt.append(ch);
        }
        StringBuilder newMessage= new StringBuilder();
        for (int i=place+1 ; i < message.length(); i++) {
        	char ch =message.charAt(i);
        	newMessage.append(ch);
        }
        return newMessage.toString();
    }

}