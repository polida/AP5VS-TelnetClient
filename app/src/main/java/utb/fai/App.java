package utb.fai;

public class App {

    public static void main(String[] args) {
        // TODO: Implement input parameter processing
        if (args.length == 2) {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            TelnetClient telnetClient = new TelnetClient(host, port);
            telnetClient.run(); // run telnet client
        }
        else {
            System.out.println("Usage: java -jar telnet-client.jar <host> <port>");
        }
    }
}
