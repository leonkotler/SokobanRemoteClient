
public class run {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java -jar SokobanRemoteClient.jar <IP> <PORT>");
            return;
        }

        Client client = new Client();
        client.start(args[0], Integer.parseInt(args[1]));

    }
}