package online;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientRingz { 
    private static final String USAGE
        = "usage: java week7.cmdline.Client <player type> <name> <address> <port>";


    /**
     * this functions asks the user for an IP address.
     * @return the nature of the IP address
     */
    public static  /*pure*/ String getAdress() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(
                  System.in));
    	System.out.println("Please give the Internet Address");
    	String addres = "";
		try {
			addres = in.readLine(); 
		} catch (IOException e) {
			System.out.println("Cant read address");
		}
    	return addres;
    }
    
    
    /**
     * this functions asks the user if they will use an AI as a player or a human.
     * 0 is AI and 1 is Human
     * @return the nature of the player
     */
    /*
     * @ensure \result.equals("1") || \result.equals("0");
     */
    public static /*pure*/ String getNature() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(
                  System.in));
    	System.out.println("Are you a computer(0) or a human(1)?");
    	String addres = "";
    	int flag = 0;
		try {
			addres = in.readLine();
			while (flag == 0) {
				if (addres.equals("0") || addres.equals("1")) {
					flag = 1;
				} else {
					System.out.println("Please valid input  0 or 1");
					addres = in.readLine();
				}
			}
		} catch (IOException e) {
			System.out.println("Cant read nature");
		}
    	return addres;
    }
    
    /**
     * this functions asks the user to put in their preferred username.
     * @return their user name
     */
    public static /*pure*/ String getName() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(
                  System.in));
    	System.out.println("Please give your username");
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
     * this functions asks the user to give the Port it will connect to.
     * @return the port
     */
    public static String getPort() {
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
     * starts the client.
     * @param args should be empty
     */ 
    public static void main(String[] args) {
 
//        String name = args[1];
//        String nature = args[0];
        String name = getName();
        String nature = getNature();
        InetAddress addr = null;
        int port = 0;
        Socket sock = null;

        
        try {
            //addr = InetAddress.getByName(args[2]);
        	addr = InetAddress.getByName(getAdress());
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + args[2] + " unknown");
            System.exit(0);
        }

        
        try {
          //  port = Integer.parseInt(args[3]);
        	port = Integer.parseInt(getPort());
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[3]
            		           + " is not an integer");
            System.exit(0); 	
        }
        
        
        try {
            sock = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + addr
                    + " and port " + port);
        }

        
        try {
            Peer client = new Peer(name, nature, sock);
            Thread streamInputHandler = new Thread(client, "imputhandle");
            streamInputHandler.start();
          //client.shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} 
