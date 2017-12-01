
import java.io.*;
import java.net.*;




public class Client {

    MulticastSocket socket;
    int port;
    InetAddress address;
    String username;

    Client(String address, int port,String username){
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


    }



    public void startClient() throws IOException{
        ReadServer r = new ReadServer();
        r.start();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            line = username + ": " + line;
            DatagramPacket msg = new DatagramPacket(line.getBytes(), line.length(),
                    address, port);
            socket.send(msg);
        }



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
                } catch (IOException e) {
                    break;

                }
            }
        }
    }



    public static void main(String[] args) throws IOException {



        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <Group address> <Group port> <username>");
            System.exit(1);
        }

        String address = args[0];
        int port = Integer.valueOf(args[1]);

        Client c = new Client(address,port,args[2]);
        c.startClient();



    }

    public class listenServer extends Thread{

    }
}


