import util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
            if (args != null && args.length >= 1) {
                cmd = args[0];
            }
            Process process = new ProcessBuilder(new String[]{"bash", "-c", cmd})
                    .redirectErrorStream(true)
                    .start();

            ArrayList<String> output = new ArrayList<>();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
                output.add(line);

            //There should really be a timeout here.
            if (0 != process.waitFor())
                System.err.println("process error, process=" + process);

            System.out.println("output: " + output);
        } catch (Exception e) {
            System.err.println("err...");
            e.printStackTrace();
        }
    }
}
