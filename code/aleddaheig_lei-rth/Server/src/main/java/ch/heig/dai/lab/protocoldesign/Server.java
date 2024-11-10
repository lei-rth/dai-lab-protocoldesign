package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.*;

public class Server {
    private final int SERVER_PORT = 5656;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private String formatResult(double result) {
        BigDecimal decimal = BigDecimal.valueOf(result).stripTrailingZeros();
        return "RESULT " + decimal.toPlainString() + "\n";
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {

                    System.out.println("Client connected");

                    out.write(">> WELCOME TO THE CALCULATOR PROTOCOL <<\n" +
                            "==============================================\n" +
                            "Usage      :  <OPERATION> <Number1> <Number2>\n" +
                            "Operations :  ADD, SUB, MUL, DIV\n" +
                            "QUIT       :  Close the connection\n" +
                            "HELP       :  Show this message\n" +
                            "EOF\n");
                    out.flush();

                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.equals("QUIT")) {
                            out.write("BYE\n");
                            out.flush();
                            break;
                        } else if (line.split(" ").length != 3) {
                            out.write(
                                    "ERROR PROVIDE AN OPERATION IN THE FOLLOWING FORMAT: <OPERATION> <Number1> <Number2>\n");
                            out.flush();
                            continue;
                        }

                        String operator = line.split(" ")[0];
                        double var1, var2;

                        try {
                            var1 = Double.parseDouble(line.split(" ")[1]);
                            var2 = Double.parseDouble(line.split(" ")[2]);
                        } catch (NumberFormatException e) {
                            out.write("ERROR NON-NUMERIC ARGUMENTS\n");
                            out.flush();
                            continue;
                        }

                        switch (operator) {
                            case "ADD":
                                out.write(formatResult(var1 + var2));
                                break;
                            case "SUB":
                                out.write(formatResult(var1 - var2));
                                break;
                            case "MUL":
                                out.write(formatResult(var1 * var2));
                                break;
                            case "DIV":
                                if (var2 == 0) {
                                    out.write("ERROR DIVISION BY ZERO\n");
                                    break;
                                }

                                out.write(formatResult(var1 / var2));
                                break;
                            default:
                                out.write("ERROR INVALID OPERATION\n");
                                break;
                        }

                        out.flush();
                    }

                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }

                System.out.println("Server: connection to client closed");
            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }
}
