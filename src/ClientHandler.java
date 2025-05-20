import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        
    }

}
