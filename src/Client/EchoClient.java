package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client app for a chat system that allows to send message to all users using the multicast distribution implemented via
 * sockets, each user has its own username.
 * It also contains a graphical interface that allows to better control the chat
 */
public class EchoClient {

    private MulticastSocket socket;
    private int port;
    private InetAddress address;
    private String username;
    private ClientInterface gui;

    /**
     * Constructor of EchoClient
     * Initializes the connection and the graphic user interface
     *
     * @param address  ip of the connection
     * @param port     port of the connection
     * @param username username of the user
     */
    EchoClient(String address, int port, String username) {
        this.username = username;
        this.port = port;
        try {
            this.address = InetAddress.getByName(address);
            socket = new MulticastSocket(port);
            socket.joinGroup(this.address);
        } catch (IOException e) {
            System.out.println(e);
        }
        gui = new ClientInterface(this, address, username);


    }

    /**
     * Checks constantly if something is written in the terminal and in that case it sends
     * a message to all users connected.
     * It stops everything when the user sends "."
     *
     * @throws IOException error reading the user input via the terminal
     */
    public void startClient() throws IOException {
        ReadServer r = new ReadServer();
        r.start();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while (true) {
            line = stdIn.readLine();
            if (line.equals(".")) break;
            SendMessage(line);
        }
    }

    /**
     * sends a message to all user with the prefix of the actual time and the name
     * of the user sending it
     *
     * @param msg message to be sent
     */
    public void SendMessage(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        try {
            msg = time + " " + username + ": " + msg;
            DatagramPacket message = new DatagramPacket(msg.getBytes(), msg.length(),
                    address, port);
            socket.send(message);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * changes the name of the user
     *
     * @param username new name for the user
     */
    public void setName(String username) {
        this.username = username;
    }

    /**
     * Thread that listens to messages sent by other users
     */
    public class ReadServer extends Thread {
        /**
         * constantly checks if another user sent a message and in case that of recieving a message it
         * prints it in the terminal and in the GUI.
         */
        public void run() {
            while (true) {
                try {
                    byte[] buf = new byte[10000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket.receive(recv);
                    String msg = new String(recv.getData(), 0, 1024);
                    System.out.println(msg);
                    gui.printMessage(msg);
                } catch (IOException e) {
                    break;

                }
            }
        }
    }


    /**
     * starts the client with a name selected or with the name Anonymous as default
     *
     * @param args ip_address, port, username
     * @throws IOException error creating the sockets
     */
    public static void main(String[] args) throws IOException {


        if (args.length != 3 && args.length != 2) {
            System.out.println("Usage: java Client.EchoClient <Group address> <Group port> <Username>");
            System.out.println("or");
            System.out.println("Usage: java Client.EchoClient <Group address> <Group port>");
            System.exit(1);
        }

        String address = args[0];
        int port = Integer.valueOf(args[1]);
        String username;
        if (args.length == 3) {
            username = args[2];
        } else {
            username = "Anonymous";
        }
        EchoClient c = new EchoClient(address, port, username);
        c.startClient();


    }

}


