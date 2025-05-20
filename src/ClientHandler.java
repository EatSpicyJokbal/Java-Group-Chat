import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public ArrayList<ClientHandler> clientList = new ArrayList<>(); // List of clients connected to the server
    private Socket socket; // Socket that will be passed in the server
    private BufferedReader bufferedReader; // BufferedReader to read the messages from the client
    private BufferedWriter bufferedWriter; // BufferedWriter to write the messages to the client
    private String clientName; // Name of the client
    private String clientID; // ID of the client

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Create a new BufferedWriter to write the messages to the client
            this.bufferedReader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream())); // Create a new BufferedReader to read the messages from the client
            this.clientName = bufferedReader.readLine(); // Read the username of the client
            clientList.add(this);
            broadcastMessage("SERVER: " + clientName + " has entered the chat!"); // Broadcast the message to all clients
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader); // Close the socket and the streams
        }
    }

    @Override
    public void run() {
        String messaString;

        while(socket.isConnected()) {
            try {
                messaString = bufferedReader.readLine(); // Read the message from the client
                broadcastMessage(messaString); // Broadcast the message to all clients
            } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader); // Close the socket and the streams
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientList) { // Loop through all clients
            try {
                if (!clientHandler.clientName.equals(clientName)) { // If the client is not the sender
                    clientHandler.bufferedWriter.write(messageToSend); // Write the message to the client
                    clientHandler.bufferedWriter.newLine(); // Add a new line
                    clientHandler.bufferedWriter.flush(); // Flush the stream
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader); // Close the socket and the streams
            }
        }
    }

    public void removeClientHandler() {
        clientList.remove(this); // Remove the client from the list
        broadcastMessage("SERVER: " + clientName + " has left the chat!"); // Broadcast the message to all clients
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler(); // Remove the client from the list
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close(); // Close the BufferedWriter
            }
            if (bufferedReader != null) {
                bufferedReader.close(); // Close the BufferedReader
            }
            if (socket != null) {
                socket.close(); // Close the socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
