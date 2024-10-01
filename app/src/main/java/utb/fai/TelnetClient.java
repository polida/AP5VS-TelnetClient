package utb.fai;

import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(serverIp, port);
            Thread receiverThread = new Thread(new Receiver(socket));
            Thread senderThread = new Thread(new Sender(socket));
            receiverThread.start();
            senderThread.start();

            // Implementation of receiving and sending data
            // Implement processing of input from the user and sending data to the server
            // Implement response processing from the server and output to the console
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class Sender implements Runnable {
        private Socket socket;

        public Sender(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String InputLine;
                while (true) {
                    while (true){
                        InputLine = reader.readLine();
                        if (InputLine.equals("/QUIT")) {
                            socket.close();
                            System.exit(0);
                        }
                        if (InputLine != null) {
                            out.write(InputLine + "\r\n");
                            out.flush();
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Implement the Receiver class for receiving data from the server
    private class Receiver implements Runnable {
        private Socket socket;

        public Receiver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    if ((line= in.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (SocketException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}