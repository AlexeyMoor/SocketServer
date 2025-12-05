package ait.socket.server.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ChatServerReceiver implements Runnable {
    private final BlockingQueue<String> messages;
    private final Socket socket;

    public ChatServerReceiver(Socket socket, BlockingQueue<String> messages) {
        this.messages = messages;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (socket) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = socketReader.readLine();
                if (message == null) {
                    System.out.println("Connection: " + socket.getInetAddress() + ":" + socket.getPort() + ", closed by client.");
                    break;
                }
                messages.put(message);
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
