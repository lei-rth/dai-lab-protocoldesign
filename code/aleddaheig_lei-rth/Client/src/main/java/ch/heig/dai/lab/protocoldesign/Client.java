package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 5656;

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

            StringBuilder initialMessage = new StringBuilder();
            String line;
            System.out.println(in.readLine());
            while ((line = in.readLine()) != null && line.length() != 0) {
                initialMessage.append(line + "\n");
            }
            System.out.println(initialMessage.toString());

            var userInputReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            while (true) {
                System.out.print("> ");
                String userInput = userInputReader.readLine();

                if (userInput.equals("HELP")) {
                    System.out.println(initialMessage.toString());
                    continue;
                }

                out.write(userInput + "\n");
                out.flush();

                String response = in.readLine();
                System.out.println("> " + response);

                if (response.contains("BYE")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}
