import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;

    public Client(Socket socket, String clientName) {
        try {
            this.socket = socket;
            this.clientName = clientName;
            this.bufferedWriter = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(clientName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            
        } catch (Exception e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    } catch (Exception e) {
                        closeEverything(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String clientName = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234); // Connect to the server
        Client client = new Client(socket, clientName); // Create a new client
        client.listenForMessage(); // Listen for messages from the server
        client.sendMessage(clientName); // Send messages to the server
    }
}
