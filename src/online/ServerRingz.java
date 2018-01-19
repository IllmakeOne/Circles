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
		
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + args[1]
         		           + " is not an integer");
			System.exit(0);
		}

		try {
			ssock = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on "  + " and port " + port);
		}
		
		System.out.println("Server Started");
		try {
			sock = ssock.accept();
			nrClinets++;
			System.out.println("Client " +  nrClinets + " connected");
		} catch (IOException e) {
			System.out.println("sth wrong in accept");
		}
     
		
	//	try {
            ServerPeer server = new ServerPeer(sock, lobby);
			Thread streamInputHandler = new Thread(server);
			streamInputHandler.start();
			server.lobby();
<<<<<<< HEAD
		//	server.shutDown();
=======
			server.shutDown();
>>>>>>> branch 'master' of https://git.snt.utwente.nl/s1942727/Circles.git
		//} catch (IOException e) {
//			e.printStackTrace();
		//}
	}
	
} 
