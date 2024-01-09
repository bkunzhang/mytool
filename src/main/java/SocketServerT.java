import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bkunzh
 * @date 2023/6/17
 */
public class SocketServerT implements Runnable {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());

    private AtomicInteger count = new AtomicInteger(0);

    private Socket socket;

    public SocketServerT(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        int port = 30055;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            threadPoolExecutor.execute(new SocketServerT(socket));
        }

    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStreamReader reader = new InputStreamReader(socket.getInputStream())) {
            System.out.println("连接上:" + socket.getInetAddress().getHostAddress());
            StringBuilder readInfo = new StringBuilder();
            while (true) {
                char[] buffer = new char[10];
                int len = reader.read(buffer);
                if (len == -1) {
                    System.out.println("exit: threadId=" + Thread.currentThread().getId());
                    break;
                }
                readInfo.append(new String(buffer, 0, len));
                System.out.println("current read info=" + readInfo);
                String respStr = "收到，local time=" + LocalDateTime.now(Clock.systemDefaultZone());
                outputStream.write(respStr.getBytes(StandardCharsets.UTF_8));
                outputStream.write(new byte[] {0}); // finish flag
                outputStream.flush();
            }
            // todo 这里count为啥一直是1
            System.out.println("threadId=" + Thread.currentThread().getId() + ", count=" + count.incrementAndGet());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
