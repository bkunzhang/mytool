package socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author bkunzh
 * @date 2023/6/17
 */
public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("39.108.173.218", 30003);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            String hello = "hello, server, my local time=" + LocalDateTime.now(Clock.systemDefaultZone());
            outputStream.write(hello.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[10];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                System.out.println("len=" + len);
                byteArrayOutputStream.write(buffer, 0, len);
                // 结束，并到最后close输入输出流和socket
                if (buffer[len - 1] == 0) {
                    break;
                }
            }
            // 去掉结束符byte 0
            System.out.println(new String(byteArrayOutputStream.toByteArray(),
                    0, byteArrayOutputStream.size() - 1, StandardCharsets.UTF_8));

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("close socket");
        }
    }
}
