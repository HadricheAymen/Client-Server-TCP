package server;

import java.io.IOException;
import java.util.ArrayList;

public class ClientCommandHandler {

	public void handleCommande(ClientHandler Cl , String command) {	
		switch (command) {
		case "/users":
			for (ClientHandler client : Server.clienthandlers)
				Cl.getOut().println(client.getClientName());
			
			break;
		case "/change username":
		{
			try {
				boolean clientnameExist=true;
	        	do {
	        		
	        		String username = Cl.getIn().readLine();
	        		clientnameExist = setClientName(Cl, username);
	        	} while (clientnameExist);
			} 
			catch (IOException e) {e.printStackTrace();}		
		
		}
		break;
		case "/exit" :
		{
			handleExit(Cl);
		}
		default:
			break;
		}
	}
	

	public boolean setClientName(ClientHandler Cl, String newClientname) {
	    boolean exist = false;
	    String oldClientname = Cl.getClientName();

	    // Check if the new username is already in use
	    for (ClientHandler client : Server.clienthandlers)
	        if (client.getClientName().equals(newClientname))
	            exist = true;

	    if (exist) { // When the username already exists
	        Cl.getOut().println("System Notification: this username is in use");
	    } else {
	        // Successfully change the username
	        Cl.setClientName(newClientname);

	        if (oldClientname == null) // When the client is new
	        { 
	            Cl.getOut().println("System Notification: Write \"/help\" to show useful commands");
	            Cl.getOut().println("System Notification: Welcome " + newClientname);
	            for (ClientHandler client : Server.clienthandlers)
	                if (!client.getClientName().equals(newClientname))
	                    client.getOut().println("System notification: " + newClientname + " joined the chat");
	            System.out.println("System Notification: "+newClientname + " joined the chat");
	            
	        } else { // When changing the username
	            Cl.getOut().println("System Notification: Username accepted");
	            Cl.getOut().println("System Notification: You changed your username to " + newClientname);
	            for (ClientHandler client : Server.clienthandlers)
	                if (!client.getClientName().equals(newClientname))
	                    client.getOut().println("System Notification: "+oldClientname + " changed their name to " + newClientname);
	            System.out.println("System Notification: "+oldClientname + " changed his username to " + newClientname); 
	        }
	    }
	    return exist;
	}

	private void handleExit(ClientHandler Cl) {
        
        // Inform other users that the client has left
		for (ClientHandler client : Server.clienthandlers) {
	        if (!client.equals(Cl)) {
	            client.getOut().println("System Notification: "+Cl.getClientName() + " has left the chat.");
	        }
	    }
        Server.clienthandlers.remove(Cl);
        
        // Close the connection and remove the client from the list
		 try {
		        Cl.getClientSocket().close();  
		        Cl.getIn().close();            
		        Cl.getOut().close();           
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
    }
}
