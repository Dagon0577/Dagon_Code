import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2019/12/26 10:11
 */
public class Main {
    /**
     * linux
     * cpuid : dmidecode -t processor | grep 'ID'
     * mainboard : dmidecode |grep 'Serial Number'
     * disk : fdisk -l
     * mac : ifconfig -a
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> result = SerialNumberUtil.getAllSn();
        try {
            String path = System.getProperty("user.dir");
            System.out.print(path);
            File writename = new File(path + "/Info.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            if (result.size() > 0) {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    out.write(entry.getKey() + " : " + entry.getValue() + "\r\n");
                }
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Successful access to information!");
    }
}
