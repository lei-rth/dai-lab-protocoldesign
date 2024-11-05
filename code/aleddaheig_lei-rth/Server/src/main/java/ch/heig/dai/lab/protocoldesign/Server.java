package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.*;

public class Server {
    final int SERVER_PORT = 5656;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private String formatResult(double result) {
        BigDecimal decimal = BigDecimal.valueOf(result).stripTrailingZeros();
        return decimal.toPlainString();
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    String line;
                    while ((line = in.readLine()) != null) {
                        String operator = line.split(" ")[0];
                        double var1 = Double.parseDouble(line.split(" ")[1]);
                        double var2 = Double.parseDouble(line.split(" ")[2]);
                        
                        switch (operator) {
                            case "ADD":
                                out.write("> RESULT " + formatResult(var1 + var2) + "\n");
                                break;
                            case "SUB":
                                out.write("> RESULT " + formatResult(var1 - var2) + "\n");
                                break;
                            case "MUL":
                                out.write("> RESULT " + formatResult(var1 * var2) + "\n");
                                break;
                            case "DIV":
                                if (var2 == 0) {
                                    out.write("> ERROR DIVISION BY ZERO\n");
                                    break;
                                }
                                out.write("> RESULT " + formatResult(var1 / var2) + "\n");
                                break;
                            default:
                                out.write("> ERROR INVALID OPERATION\n");
                                break;
                        }

                        out.flush();
                    }

                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }
}
