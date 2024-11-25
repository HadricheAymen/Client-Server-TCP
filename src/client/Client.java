package client;

import java.io.*;

import java.net.*;
import java.util.concurrent.SynchronousQueue;



public class Client {
	String host = "127.0.0.1";
    int port = 12345;
    Socket socket;
    BufferedReader in;
    PrintWriter out; 
    BufferedReader keyboard;
    String username;
	Thread keyhandlerThread;
	Thread responseThread;
	public static boolean allowed=true;
	static SynchronousQueue<String> Canal = new SynchronousQueue<String>();
    
    
    
    Client() {
    	try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			username="";
		    
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
    }

    public static void main(String[] args) {
        
        	Client Cl =new Client();
        	CommandsHandler cmdhandler= new CommandsHandler(Cl);
            System.out.println("Connecté au serveur TCP.");
            
            //send username to Server
            cmdhandler.setUsername();
            
            //Thread for handling keyboard
            KeyboardHandler keyhandler = new KeyboardHandler(Cl,cmdhandler);
            Cl.keyhandlerThread = new Thread(keyhandler);
            Cl.keyhandlerThread.start();
            
            
            //Thread for Receiving Server messages
            
            IncomingResponseHandler receive = new IncomingResponseHandler(Cl,cmdhandler);
            Cl.responseThread = new Thread(receive);
            Cl.responseThread.start();
         
                        

    }

        
}