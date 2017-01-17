import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private void readInputsAndSend(BufferedReader in, PrintWriter out, String exitStr) {
        try {
            String line;
            while (!(line = in.readLine()).equals(exitStr)) {
                out.println(line);
                out.flush();
            }
            out.println(line);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Server terminated");
        }
    }

    private Thread aSyncReadInputsAndSend(BufferedReader in, PrintWriter out, String exitStr) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                readInputsAndSend(in, out, exitStr);
            }
        });
        t.start();
        return t;
    }

    public void start(String ip, int port) {
        try {
            System.out.println("Trying to connect to " + ip + ":" + port);
            Socket theServer = new Socket(ip, port);
            System.out.println("Connection established!");
            System.out.println();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(theServer.getInputStream()));

            PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
            PrintWriter outToScreen = new PrintWriter(System.out);

            Thread t1 = aSyncReadInputsAndSend(userInput, outToServer, "exit"); // different thread
            Thread t2 = aSyncReadInputsAndSend(serverInput, outToScreen, "bye"); // different thread

            t1.join();
            t2.join(); // wait for threads to end

            userInput.close();
            serverInput.close();
            outToServer.close();
            outToScreen.close();
            theServer.close();

        } catch (IOException | InterruptedException e) {System.out.println(e.getMessage());}
    }
}
