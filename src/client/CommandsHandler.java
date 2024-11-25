package client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CommandsHandler {
	Client Cl;
	
	public CommandsHandler(Client Cl) {
		this.Cl=Cl;
	}

	public void changeUsername() {
        try {
            String serverResponse;   
            do {
                System.out.print("Write your username: ");
                Cl.username = Cl.keyboard.readLine();
                Cl.out.println(Cl.username); // Send username to server
                serverResponse = Client.Canal.poll(5, TimeUnit.SECONDS);
                System.out.println(serverResponse); // Display server's response
            } while (serverResponse.equals("System Notification: this username is in use"));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	public void setUsername() {
        try {
            String serverResponse;   
            do {
                System.out.print("Write your username: ");
                Cl.username = Cl.keyboard.readLine();
                Cl.out.println(Cl.username); // Send username to server
                serverResponse = Cl.in.readLine();
                System.out.println(serverResponse); // Display server's response
            } while (serverResponse.contains("System Notification: this username is in use"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private void closeConnection() {
        try {
        	
            Cl.out.close();
            Cl.in.close();
            Cl.socket.close();
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public void handleCommande(String command) {
		switch (command) {
		case "/change username":
		{
			Cl.out.println("/change username");
			changeUsername();
			break;
		}
		case "/help" :
		{
			System.out.println("@<username> <message> to send a private mesaage");
			System.out.println("\"/change username\" " + "to change your username");
			System.out.println("\"/users\" " + "to show users connected to the chatroom");
			break;
		}
		case "/users" :
		{
			Cl.out.println("/users");
			break;
		}
		case "/exit" :
		{
			Cl.out.println("/exit");
			closeConnection();
			break;
		}
		default:
			System.out.println("\"" +command +"\" " + "is not recognized as valid command"  );
		}
	}
}
