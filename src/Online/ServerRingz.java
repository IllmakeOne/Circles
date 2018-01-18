package Online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerRingz {


	private ArrayList<Socket> Clients;

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
		
		// parse args[1] - the port
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + args[1]
         		           + " is not an integer");
			System.exit(0);
		}

     // try to open a Socket to the server
		try {
			ssock = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on "  + " and port " + port);
		}

		try {
			sock = ssock.accept();
		} catch (IOException e) {
			System.out.println("sth wrong in accept");
		}
     
     // create Peer object and start the two-way communication
		try {
            ServerPeer serv = new ServerPeer(name, sock);
			Thread streamInputHandler = new Thread(serv);
			streamInputHandler.start();
			serv.handleTerminalInput();
			serv.shutDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
