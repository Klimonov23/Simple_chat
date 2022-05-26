import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCP_Connection_Listener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCP_Connection> tcp_connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running");
        try (ServerSocket serverSocket = new ServerSocket(22)) {
            while (true) {
                try {
                    new TCP_Connection(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("Exception");

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCP_Connection tcp_connection) {
        tcp_connections.add(tcp_connection);
        send_all_message("Client connected:"+tcp_connection);
    }

    @Override
    public synchronized void onReceiveString(TCP_Connection tcp_connection, String val) {
        send_all_message(val);
    }

    @Override
    public synchronized void onDisconect(TCP_Connection tcp_connection) {
        tcp_connections.remove(tcp_connection);
        send_all_message("Client disconnected:"+tcp_connection);

    }

    @Override
    public synchronized void onException(TCP_Connection tcp_connection, Exception e) {
        System.out.println("TCP Connection exception" + e);
    }

    private void send_all_message(String val) {
        System.out.println(val);
        int m = tcp_connections.size();
        for (TCP_Connection tcp_connection : tcp_connections) {
            tcp_connection.send_message(val);
        }

    }
}
