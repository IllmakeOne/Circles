package Online;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client class for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientRingz {
    private static final String USAGE
        = "usage: java week7.cmdline.Client <player type> <name> <address> <port>";

    /** Starts a Client application. */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(USAGE);
            System.exit(0);
        }

        String name = args[1];
        String nature = args[0];
        InetAddress addr = null;
        int port = 0;
        Socket sock = null;

        // check args[1] - the IP-adress
        try {
            addr = InetAddress.getByName(args[2]);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + args[2] + " unknown");
            System.exit(0);
        }

        // parse args[2] - the port
        try {
            port = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[3]
            		           + " is not an integer");
            System.exit(0);
        }

        // try to open a Socket to the server
        try {
            sock = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + addr
                    + " and port " + port);
        }

        // create Peer object and start the two-way communication
        try {
            Peer client = new Peer(name, nature, sock);
            Thread streamInputHandler = new Thread(client);
            streamInputHandler.start();
            client.lobby();
            client.shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class Client
