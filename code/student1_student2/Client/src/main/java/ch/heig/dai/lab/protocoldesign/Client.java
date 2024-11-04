package ch.heig.dai.lab.protocoldesign;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    final String SERVER_ADDRESS = "172.20.10.13";
    final int SERVER_PORT = 5656;

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try {
            // Connect to the server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server");

            // Get input and output streams
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // Get user input "ADD <Number1> <Number2>"
            System.out.print("> ");
            String request = System.console().readLine();
            out.write(request.getBytes());
            out.flush();

            // Close the socket
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}