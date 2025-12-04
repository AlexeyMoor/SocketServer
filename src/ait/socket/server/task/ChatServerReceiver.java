package ait.socket.server.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatServerReceiver implements Runnable {
    private final Socket socket;

    public ChatServerReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String clientName = "Anonymous";
        try (socket) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

            clientName = socketReader.readLine();
            if (clientName == null || clientName.isBlank()) {
                System.out.println("The client connected without a name and will be disconnected.");
                return;
            }
            System.out.println(clientName + " connected.");

            String message;
            while ((message = socketReader.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println(formattedTime + " [" + clientName + "]: " + message);
                String response = formattedTime + " [" + clientName + "]: " + message;
                socketWriter.println(response);
            }
        } catch (SocketException e) {
            System.out.println("Connection with " + clientName + " was reset.");
        } catch (IOException e) {
            System.out.println("An error occurred with client " + clientName + ": " + e.getMessage());
        }
    }
}
