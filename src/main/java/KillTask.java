import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author bkunzh
 * @date 2023/12/27
 */
public class KillTask {
    public static void main(String[] args) {

        String bashFileContent = "#!/bin/bash" + System.lineSeparator()
                + "pid=$(ps -ef | grep 'docker build' | grep Dockerfile | awk '{ print $2 }')" + System.lineSeparator()
                + "echo kill $pid" + System.lineSeparator()
                + "kill $pid";
        System.out.println(bashFileContent);
        writeStrToFile(bashFileContent, new File("/bk/my-tool/kill1.sh"));

        ExecuteShellTimer.scheduleOnce(() -> {
            ExecuteShell.main(new String[] {"sh /bk/my-tool/kill1.sh"});
            System.out.println("my task 20231227 ok!!!");
            ExecuteShellTimer.shutdownScheduledThreadPoolExecutor();
        }, 60 * 60 * 2L);

    }

    public static void writeStrToFile(String str, File file) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
                StandardCharsets.UTF_8)) {
            outputStreamWriter.write(str);
            outputStreamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
