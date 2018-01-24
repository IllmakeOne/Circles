package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ringz.Board;
import ringz.Move;


public class ServerRingz {

 
	
	/** Starts a Server-application. */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("wrong arguments"); 
			System.exit(0);
		}
		  
		String name = args[0];
		int port = 0;
		ServerSocket ssock = null;
		Socket sock = null;
		Lobby lobby = new Lobby(name);
		int nrClinets = 0;
		boolean ison = true;
		
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + args[1]
         		           + " is not an integer");
			System.exit(0);
		}

    	System.out.println("Server Started");
    	
    	try {
    		ssock = new ServerSocket(port);
    	} catch (IOException e) {
    		System.out.println("ERROR: could not create a socket ");
    	}
		
	    while (ison) {
	    	
	    	try {
	    		sock = ssock.accept();
	    		nrClinets++;
	    		System.out.println("Client " +  nrClinets + " connected");
	    	} catch (IOException e) {
	    		System.out.println("sth wrong in accept");
	    	}
     
		
	//	try {
            ServerPeer client = new ServerPeer(sock, lobby);
			Thread clientHandler = new Thread(client);
			clientHandler.start();
		//	server.shutDown();

		//} catch (IOException e) {
//			e.printStackTrace();
		//}
	    }
	}
	
} 
