import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<Socket> clientSockets = new HashSet<>();

    public static void main(String[] args) {
        int port = 12345; // Cổng server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                clientSockets.add(socket);
                System.out.println("New client connected: " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lớp xử lý từng client
    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                writer.println("Welcome to the chat!");
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcastMessage(message, socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientSockets.remove(socket);
                System.out.println("Client disconnected: " + socket.getInetAddress());
            }
        }

        private void broadcastMessage(String message, Socket sender) {
            for (Socket clientSocket : clientSockets) {
                if (clientSocket != sender) {
                    try {
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        writer.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
