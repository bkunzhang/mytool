import java.io.File;
import java.util.Locale;

/**
 * @author bkunzh
 * @date 2023/12/8
 */
public class T {
    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name").toLowerCase(Locale.ROOT).startsWith("win"));
    }

    private static void ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
