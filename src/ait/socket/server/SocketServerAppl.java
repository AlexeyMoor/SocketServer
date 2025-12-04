package ait.socket.server;

import ait.socket.server.task.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerAppl {
    public static void main(String[] args) {
        int port = 9000;

        try (ServerSocket serverSocket = new ServerSocket(port);
             ExecutorService executorService = Executors.newFixedThreadPool(2);) {
            while (true) {
                System.out.println("Server waiting on port " + port);
                Socket socket = serverSocket.accept(); // blocking method - waits for client connection
                System.out.println("Client connected");
                System.out.println("Client IP address: " + socket.getInetAddress() + ":" + socket.getPort());
                executorService.execute(new ClientHandler(socket)); // submit task to thread pool
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
