package utb.fai;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class TelnetClient {

    private String serverIp;
    private int port;
    private static final int THREAD_POOL_SIZE = 10;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            Socket socket = new Socket(serverIp, port);
            executorService.submit(new Receiver(socket));
            executorService.submit(new Sender(socket));
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
                    }
                }
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
