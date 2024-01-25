package socket;

import util.DateTimeUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bkunzh
 * @date 2024/1/24
 *
 * java -Xms10m -Xmx50m socket.LongConnectServer
 * java -Xms1m -Xmx2m socket.LongConnectServer
 * test Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
 */
public class LongConnectServer implements Runnable {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());

    private static ThreadLocal<AtomicInteger> countThreadLocal = new ThreadLocal<AtomicInteger>() {
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };

    private Socket socket;

    public LongConnectServer(Socket socket) {
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
            threadPoolExecutor.execute(new LongConnectServer(socket));
        }

    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStreamReader reader = new InputStreamReader(socket.getInputStream())) {
            System.out.println(DateTimeUtil.getSimpleUTCTimeStrOfNow()
                    + " who connected:" + socket.getInetAddress().getHostAddress());
            StringBuilder readInfo = new StringBuilder();
            while (true) {
                char[] buffer = new char[10];
                int len = reader.read(buffer);
                if (len == -1) { // inputstream closed
                    System.out.println("len = -1");
                    break;
                    //if (false) {
                    //    break;
                    //}
                }
                readInfo.append(new String(buffer, 0, len));
                System.out.println(DateTimeUtil.getSimpleUTCTimeStrOfNow()
                        + " current read info=" + readInfo);
                String respStr = "HTTP/1.1 200 \n" +
                        "Set-Cookie: rememberMe=deleteMe; Path=/; Max-Age=0; Expires=Tue, 09-Jan-2024 12:29:19 GMT; SameSite=lax\n" +
                        "Content-Type: application/json\n" +
                        "Transfer-Encoding: chunked\n" +
                        "Date: Wed, 10 Jan 2024 12:29:19 GMT\n" +
                        "Keep-Alive: timeout=60\n" +
                        "Connection: keep-alive\n" +
                        "\n" +
                        "31\n" +
                        "{\"msg\":\"用户不存在/密码错误\",\"code\":500}\n" +
                        "0\n" +
                        "\n";

                outputStream.write(respStr.getBytes(StandardCharsets.UTF_8));
                //outputStream.write(new byte[] {0}); // finish flag
                outputStream.flush();
            }
            System.out.println("threadId=" + Thread.currentThread().getId() + ", count=" + countThreadLocal.get().incrementAndGet());
            System.out.println("will disconnect 10min");
            TimeUnit.MINUTES.sleep(10L);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
