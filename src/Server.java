import java.io.IOException;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void serverStart() {

        try {
            
            while(!serverSocket.isClosed()) {                                   // Run the server indefinitely
                Socket socket = serverSocket.accept();                          // The program will wait here until a client connects
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);                      // Create a new thread for the client
                thread.start();                                                 // Start the thread
            }
        } catch (Exception e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null){
                serverSocket.close();                                           // Close the server socket
                System.out.println("Server socket closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);            // Create a server socket on port 1234
        System.out.println("Server started on port 1234");
        Server server = new Server(serverSocket);
        server.serverStart();                                               // Start the server
    }
}
