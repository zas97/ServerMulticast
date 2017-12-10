package Client;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EchoClient {

    private MulticastSocket socket;
    private int port;
    private InetAddress address;
    private String username;
    private ClientInterface gui;

    EchoClient(String address, int port,String username){
        this.username=username;
        this.port = port;
        try {
            this.address = InetAddress.getByName(address);
            socket = new MulticastSocket(port);
            socket.joinGroup(this.address);
        }
        catch(IOException e){
            System.out.println(e);
        }
        gui = new ClientInterface(this,address,username);


    }

    public void startClient() throws IOException{
        ReadServer r = new ReadServer();
        r.start();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            SendMessage(line);
        }
    }

    public void SendMessage(String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        try {
            msg = time+" "+username + ": " + msg;
            DatagramPacket message = new DatagramPacket(msg.getBytes(), msg.length(),
                    address, port);
            socket.send(message);
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public void setName(String username){
        this.username = username;
    }

    public class ReadServer extends Thread {
        public void run() {
            while (true) {
                try {
                    byte[] buf = new byte[10000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket.receive(recv);
                    String msg = new String(recv.getData(),0,1024);
                    System.out.println(msg);
                    gui.printMessage(msg);
                } catch (IOException e) {
                    break;

                }
            }
        }
    }



    public static void main(String[] args) throws IOException {



        if (args.length != 3 && args.length != 2) {
            System.out.println("Usage: java Client.EchoClient <Group address> <Group port> <username>");
            System.out.println("or");
            System.out.println("Usage: java Client.EchoClient <Group address> <Group port>");
            System.exit(1);
        }

        String address = args[0];
        int port = Integer.valueOf(args[1]);
        String username;
        if(args.length == 3){
            username=args[2];
        }
        else{
            username="Anonymous";
        }
        EchoClient c = new EchoClient(address,port,username);
        c.startClient();



    }

}


