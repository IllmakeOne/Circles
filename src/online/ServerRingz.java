package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import view.ServerTUI;


public class ServerRingz {

	

	/**
     * this functions asks the server manage to name the server.
     * @return their user name of the server
     */
    public static /*pure*/ String getName() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(
                  System.in));
    	System.out.println("Please give the name for the server");
    	String name = "";
		try {
			name = in.readLine(); 
		} catch (IOException e) {
			System.out.println("Cant read name"); 
		}
    	return name;
    }
    
    /**
     * this tests if @param testedport is a valid port.
     * @return true if it is.
     */
    public static /*pure*/ boolean validPort(String testedport) {
    	if (testedport.length() < 6 && testedport.length() > 3) {
			for (int i = 0; i < testedport.length(); i++) {
				if (Character.isDigit(testedport.charAt(i)) == false) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
    }
    
    /** 
     * reads only one string from the console.
     * @param prompt
     * @return
     */
    public static void readExit(boolean shut) {
        System.out.print("If you type at any time exit, it will shut down");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                System.in));
        String input = "";
        while (!input.equals("exit")) {
        	try {
        		input = in.readLine();
        	} catch (IOException e) {
        		System.out.println("error in readExit");
        	}
        }
    }
    
    /**
     * this functions asks the user to give the Port it will connect to.
     * @return the port
     */
    public static/*pure*/ String getPort() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(
                  System.in));
    	System.out.println("Please give the port");
    	String port = "";
    	int flag = 0;
		try {
			port = in.readLine();
			while (flag == 0) {
				if (validPort(port)) {
					flag = 1;
				} else {
					System.out.println("Please give a valid port");
					port = in.readLine();
				}
			}
				
		} catch (IOException e) {
			System.out.println("Cant read port");
		}
    	return port;
    }
	
	/**
	 * starts the server.
	 * @param args should be empty
	 */
	public static void main(String[] args) {
		int port = 0;
		ServerSocket ssock = null;
		Socket sock = null;
		ServerTUI tui = new ServerTUI();
		Lobby lobby = new Lobby(tui);
		boolean ison = true;
		
		try {
			//port = Integer.parseInt(args[1]);
			port = Integer.parseInt(getPort());
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + args[1]
         		           + " is not an integer");
			System.exit(0);
		}
		
    	
    	int fine = 0;
    	while (fine == 0) {
    		try {
    			ssock = new ServerSocket(port);
    			fine = 1;
    		} catch (IOException e) {
    			System.out.println("ERROR: could not create a socket ");
    			System.out.println("port already in use");
    			port = Integer.parseInt(getPort());
    		}
    	}


    	System.out.println("Server Started");
//    	thread = new Thread(() -> readExit(ison));
//    	thread.start(); 
	    while (ison) {
    	
	    	try {
	    		sock = ssock.accept();
	    	} catch (IOException e) {
	    		System.out.println("sth wrong in accept");
	    	}
     
            ServerPeer client = new ServerPeer(sock, lobby, tui);
			Thread clientHandler = new Thread(client);
			clientHandler.start();
	    }
	}
	
} 
