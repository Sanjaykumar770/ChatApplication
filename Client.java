import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    BufferedReader br;
    PrintWriter out;
    Socket socket;

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("192.168.29.117", 1111);  // Server IP and port
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg == null || msg.equalsIgnoreCase("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started");
            try (BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String content = br1.readLine();
                    out.println(content);
                    if (content.equalsIgnoreCase("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Client (Sender)");
        new Client();
    }
}
