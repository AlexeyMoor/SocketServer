package ait.socket.server.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ChatServerSender implements Runnable {
    private final BlockingQueue<String> messages;
    private final Set<PrintWriter> clients = new HashSet<>();

    public ChatServerSender(BlockingQueue<String> messages) {
        this.messages = messages;
    }

    public synchronized boolean addClient(Socket socket) throws IOException {
        return clients.add(new PrintWriter(socket.getOutputStream(), true));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = messages.take();
                synchronized (this) {

                    Iterator<PrintWriter> it = clients.iterator();
                    while (it.hasNext()) {
                        PrintWriter client = it.next();
                        if (client.checkError()) {
                            it.remove();
                        } else {
                            client.println(message);
                        }
                        System.out.println("size: " + clients.size());
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
