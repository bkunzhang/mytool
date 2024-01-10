package util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author bkunzh
 * @date 2024/1/10
 */
public class DateTimeUtil {
    public static DateTimeFormatter DATETIME_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String getSimpleUTCTimeStrOfNow() {
        return DATETIME_DEFAULT_FORMATTER.format(LocalDateTime.now(Clock.systemUTC()));
    }
}
