import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OneReaderThread {
    public static void main(String[] args) throws IOException {
        @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(17291);
        new ReadingThreadA().start();
        Socket client = serverSocket.accept();
        OutputStream os = client.getOutputStream();
        byte[] writeData = new byte[2];
        for (;;) {
            waitingForA = true;
            long start = System.currentTimeMillis();
            while (waitingForA) {
                long now = System.currentTimeMillis();
                if (now > start + 500) {
                    // 500ms have passed, which is 10x the read timeout 
                    System.out.println("Should never happen: A is unresponsive");
                    os.write(writeData);
                    break;
                }
            }
        }
    }

    private static volatile boolean waitingForA;

    private static final class ReadingThreadA extends Thread {
        @Override
        public void run() {
            try {
                @SuppressWarnings("resource")
                Socket s = new Socket("localhost", 17291);
                s.setSoTimeout(50); // SO_TIMEOUT is set, meaning that reads should not block for more than 50ms
                final InputStream is = s.getInputStream();
                byte[] readDataA = new byte[2];
                for (;;) {
                    int n = 0;
                    try {
                        n = is.read(readDataA);
                    } catch (IOException e) {
                        // Ignore
                    }
                    System.out.println("A tick (" + n + ")");
                    waitingForA = false; // This assignment should happen at least once every 50ms
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
