package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {
    final String SERVER_ADDRESS = "localhost";
    final int SERVER_PORT = 5656;

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                var out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            System.out.println("Connected to server");
            System.out.println(">> WELCOME TO THE CALCULATOR PROTOCOL <<");
            System.out.println("==============================================");
            System.out.println("Usage      :  <OPERATION> <Number1> <Number2>");
            System.out.println("Operations :  ADD, SUB, MUL, DIV");
            System.out.println("QUIT       :  Close the connection");

            var userInputReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            while (true) {
                System.out.print("> ");
                String userInput = userInputReader.readLine();

                out.write(userInput + "\n");
                out.flush();

                String response = in.readLine();
                System.out.println("> " + response);

                if (response.contains("BYE")) {
                    return;
                }
            }

        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}
