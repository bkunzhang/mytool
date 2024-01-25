package socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author bkunzh
 * @date 2024/1/24
 */
public class LongConnectClient {
    public static void main(String[] args) {
        int port = 30055;
        String ip = "127.0.0.1";
        if (args.length > 1) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        try (Socket socket = new Socket(ip, port);
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
                outputStream.write(hello.getBytes(StandardCharsets.UTF_8));
                byteArrayOutputStream.write(buffer, 0, len);
                // 结束，并到最后close输入输出流和socket
                if (buffer[len - 1] == 0) {
                    System.out.println("last byte is 0");
                    //break;
                }
                // todo rm
                try {
                    TimeUnit.MILLISECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
