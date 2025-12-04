package ait.socket.server.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (socket) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String message = socketReader.readLine();
                if (message == null || "exit".equalsIgnoreCase(message)) {
                    System.out.println("Connection: " + socket.getInetAddress() + ":" + socket.getPort() + ", closed by client.");
                    break;
                }
                System.out.println("Server received message: " + message);
                socketWriter.println(String.format("Echo: " + message + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
