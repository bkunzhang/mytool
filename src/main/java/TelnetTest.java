import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 模仿telnet/curl看端口是否通
 * @author bkunzh
 * @date 2023/12/20
 */
public class TelnetTest {
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("should run: java TelnetTest _ip _port");
            return;
        }
        long start = System.currentTimeMillis();
        // 模仿telnet/curl看端口是否通
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])),  2000);
            System.out.println("ok");
        } catch (IOException e) {
            System.err.println("cannot connect");
        }
        System.out.println("costTime=" + (System.currentTimeMillis() - start));
    }
}
