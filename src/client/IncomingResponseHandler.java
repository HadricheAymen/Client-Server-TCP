package client;

public class IncomingResponseHandler implements Runnable{
	private Client Cl;
	CommandsHandler cmdHandler;
	
	public IncomingResponseHandler(Client Cl, CommandsHandler cmdhandler) {
		this.Cl=Cl;
		this.cmdHandler=cmdhandler;
	}

	@Override
	public void run() {
		try {
			String response;
		while ((response = Cl.in.readLine()) != null) {
			
				/*if (!response.isEmpty())
					System.out.println(response);
				}*/
				if (response.equals("System Notification: this username is in use") || 
						response.equals("System Notification: Username accepted"))
					Client.Canal.put(response);
				else 
					System.out.println(response);
			}
		}
			catch (Exception e) {
				//e.printStackTrace();
			}
		
			
	}
	
		
}
