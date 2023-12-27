import java.io.File;

/**
 * @author bkunzh
 * @date 2023/12/8
 */
public class T {
    public static void main(String[] args) {
        File file = new File("H:\\mi11-photos\\20210920" +
                "\\Pictures\\2019铁仔山\\IMG_20190713_191821.jpg");
        System.out.println(file.length() * 1.0 / 1024 / 1024);
    }
}
