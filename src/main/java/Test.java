import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author bkunzh
 * @date 2023/12/27
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(ExecuteShell.DATETIME_DEFAULT_FORMATTER.format(LocalDateTime.now(Clock.systemUTC())));

    }
}
