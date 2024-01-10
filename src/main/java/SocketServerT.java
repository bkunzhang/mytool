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
 * @date 2023/6/17
 *
 * 浏览器访问响应:ok，postman不行
 *
 * 可以用curl验证响应:
 *  curl --location --request POST 'http://127.0.0.1:30055/akdjf/tt' --header 'Content-Type: application/json' --data-raw '{
 *     "todo_info": "2222222",
 *     "source": "postman_test1"
 * }' -v
 *
 *
 * curl --location --request POST 'http://39.108.173.218:30003/akdjf/tt' \
 * --header 'Content-Type: application/json' \
 * --data-raw '{
 *     "todo_info": "2222222",
 *     "source": "postman_test1"
 * }'
 *
 */
public class SocketServerT implements Runnable {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());

    private static ThreadLocal<AtomicInteger> countThreadLocal = new ThreadLocal<AtomicInteger>() {
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };

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
            System.out.println(DateTimeUtil.getSimpleUTCTimeStrOfNow()
                    + " who connected:" + socket.getInetAddress().getHostAddress());
            StringBuilder readInfo = new StringBuilder();
            while (true) {
                char[] buffer = new char[10];
                int len = reader.read(buffer);
                if (len == -1) { // inputstream closed
                    System.out.println(DateTimeUtil.getSimpleUTCTimeStrOfNow()
                            + " inputstream closed: threadId=" + Thread.currentThread().getId());
                    break;
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
                outputStream.write(new byte[] {0}); // finish flag
                outputStream.flush();
            }
            System.out.println("threadId=" + Thread.currentThread().getId() + ", count=" + countThreadLocal.get().incrementAndGet());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
