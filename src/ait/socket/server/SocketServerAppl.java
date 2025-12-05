package ait.socket.server;

import ait.socket.server.task.ChatServerReceiver;
import ait.socket.server.task.ChatServerSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketServerAppl {
    public static void main(String[] args) {
        int port = 9000;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        BlockingQueue<String> messages = new LinkedBlockingQueue<>(10);
        ChatServerSender sender = new ChatServerSender(messages);
        Thread senderThread = new Thread(sender);
        senderThread.setDaemon(true);
        senderThread.start();

        try (ServerSocket serverSocket = new ServerSocket(port);
             ExecutorService executorService = Executors.newFixedThreadPool(7);) {
            while (true) {
                System.out.println("Server waiting on port " + port);
                Socket socket = serverSocket.accept(); // blocking method - waits for client connection
                System.out.println("Client connected");
                System.out.println("Client IP address: " + socket.getInetAddress() + ":" + socket.getPort());
                sender.addClient(socket);
                executorService.execute(new ChatServerReceiver(socket, messages));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
