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
           Thread sender = new Thread(new Sender(socket));
            Thread receiver = new Thread(new Receiver(socket));
            sender.start();
            receiver.start();
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
                String inputLine;
                while (true) {
                    if (System.in.available() > 0) {
                        inputLine = reader.readLine();
                        if (inputLine.equals("/QUIT")) {
                            socket.close();
                            System.exit(0);
                            break;
                        }
                        out.write(inputLine + "\r\n");
                        out.flush();
                    } else {
                        Thread.sleep(2);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private class Receiver implements Runnable {
            private Socket socket;

            public Receiver(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    while (true) {
                        if (in.available() > 0) {
                            byte[] buffer = new byte[1024];
                            System.out.write(buffer, 0, in.read(buffer));

                        } else {
                            Thread.sleep(2);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }