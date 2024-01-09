package socket;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author bkunzh
 * @date 2024/1/9
 */
public class ManyClient {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Client.main(args);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }

        TimeUnit.SECONDS.sleep(200);

    }
}
