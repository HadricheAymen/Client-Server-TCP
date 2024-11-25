package client;

public class KeyboardHandler implements Runnable {
	Client Cl;
	CommandsHandler cmdHandler;
	public KeyboardHandler(Client Cl, CommandsHandler cmdhandler) {
		this.Cl=Cl;
		this.cmdHandler=cmdhandler;
	}
	public void run() {
		try {
			//send username to Server
            //setUsername(Cl);
			 
			
          //Sending Messages to Server 
            String message;
            while (true) {               
                message = Cl.keyboard.readLine();
                if (message.startsWith("/"))
                {
                	cmdHandler.handleCommande(message);
                }
                else {
                	Cl.out.println(message);
                }
                if (message.equalsIgnoreCase("/exit")) {
                	System.out.println("Exiting chat...");
                	break;
                }
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
