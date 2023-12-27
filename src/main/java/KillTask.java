import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
        System.out.println("bashFileContent: " + bashFileContent);
        writeStrToFile(bashFileContent, new File("/bk/my-shell/kill1.sh"));

        ExecuteShellTimer.scheduleOnce(() -> {
            ExecuteShell.main(new String[]{"sh /bk/my-shell/kill1.sh"});
            System.out.println("my task 20231227 ok!!!");
            ExecuteShellTimer.shutdownScheduledThreadPoolExecutor();
        }, 60 * 60 * 2L);

    }

    private static void ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void writeStrToFile(String str, File file) {
        ensureDirExists(file.getParentFile());

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
                StandardCharsets.UTF_8)) {
            outputStreamWriter.write(str);
            outputStreamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
