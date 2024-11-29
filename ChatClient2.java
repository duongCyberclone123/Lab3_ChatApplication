import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient2 {
    public static void main(String[] args) {
        String host = "localhost"; // Địa chỉ server
        int port = 12345;          // Cổng server
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to the chat server!");

            // Thread nhận tin nhắn từ server
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed!");
                }
            });
            receiveThread.start();

            // Gửi tin nhắn đến server
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                writer.println("User 1: "+message);
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
