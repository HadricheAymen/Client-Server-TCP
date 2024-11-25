package server;

import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server {
	static ArrayList<ClientHandler> clienthandlers = new ArrayList<ClientHandler>();
	static ClientCommandHandler cmdhandler = new ClientCommandHandler();
	

    public static void main(String[] args) {
        int port = 12345;



        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP server waiting for connections on port " + port + "...");


            // Utilisation d'un pool de threads pour gérer les connexions.
            ExecutorService executor = Executors.newFixedThreadPool(10);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion acceptée de " + clientSocket.getInetAddress()+ ", port :\\"+ clientSocket.getPort() );
                ClientHandler client =new ClientHandler(clientSocket);
                executor.execute(client);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

