package socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author bkunzh
 * @date 2024/1/10
 */
public class ClientOfHttpReqT {
    static String httpReqStr = "POST /login HTTP/1.1\n" +
            "Accept: */*\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 66\n" +
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n" +
            "Cookie: JSESSIONID=cc9dee90-8f15-401d-ba9e-49b19654829d\n" +
            "Host: 39.108.173.218:30001\n" +
            "Origin: http://39.108.173.218:30001\n" +
            "Referer: http://39.108.173.218:30001/login\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0\n" +
            "X-Requested-With: XMLHttpRequest\n"
            + "\n"
            + "username=admin&password=admin123222&validateCode=&rememberMe=false";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("39.108.173.218", 30001);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            outputStream.write(httpReqStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[10];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                System.out.println("len=" + len);
                byteArrayOutputStream.write(buffer, 0, len);
                System.out.println("read info=" + new String(byteArrayOutputStream.toByteArray(),
                        StandardCharsets.UTF_8));
                // 结束，并到最后close输入输出流和socket，可以指定一个结束符
                //if (buffer[len - 1] == 0) {
                //    break;
                //}
            }
            // 去掉结束符byte 0
            //System.out.println(new String(byteArrayOutputStream.toByteArray(),
            //        0, byteArrayOutputStream.size() - 1, StandardCharsets.UTF_8));

            System.out.println("close socket");
        }
    }
}
