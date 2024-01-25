import util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author bkunzh
 * @date 2023/12/27
 */
public class ExecuteShell {

    public static void main(String[] args) {
        try {
            System.out.println(DateTimeUtil.getSimpleUTCTimeStrOfNow() + "  thread ("
                    + Thread.currentThread().getName() + ") exec shell:");
            String cmd = "ps -ef";
            if (isWin()) {
                cmd = "tasklist /?";
            }
            Charset charset = StandardCharsets.UTF_8;

            if (args != null && args.length >= 1) {
                cmd = args[0];
            }

            String[] cmds = {"bash", "-c", cmd};
            if (isWin()) {
                cmds = new String[] {"cmd.exe", "/c", cmd};
                charset = Charset.forName("GBK");
            }

            Process process = new ProcessBuilder(cmds)
                    .redirectErrorStream(true)
                    .start();

            ArrayList<String> output = new ArrayList<>();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), charset));
            String line;
            while ((line = br.readLine()) != null)
                output.add(line);

            //There should really be a timeout here.
            if (0 != process.waitFor())
                System.err.println("process error, process=" + process);

            System.out.println("output: ---------------------\n"
                    + String.join(System.lineSeparator(), output.toArray(new String[output.size()])));
        } catch (Exception e) {
            System.err.println("err...\n");
            e.printStackTrace();
        }
        System.out.println("----------------------------------");
    }

    public static boolean isWin() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT).startsWith("win");
    }
}
