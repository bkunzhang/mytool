import java.io.File;

/**
 * @author bkunzh
 * @date 2023/12/8
 */
public class T {
    public static void main(String[] args) {
        ensureDirExists(new File("f:/d/e/1.txt").getParentFile());
    }

    private static void ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
