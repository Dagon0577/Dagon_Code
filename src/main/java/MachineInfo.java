import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2019/12/27 17:46
 */
public class MachineInfo {
    /**
     * linux
     * cpuid : dmidecode -t processor | grep 'ID'
     * mainboard : dmidecode |grep 'Serial Number'
     * disk : fdisk -l
     * mac : ifconfig -a
     */
    public static void generateInfo() {
        Map<String, String> result = SerialNumberUtil.getAllSn();
        try {
            String path = System.getProperty("user.dir");
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

    public static Machine getMachineFromInfo() throws Exception {
        String path = System.getProperty("user.dir");
        String encoding = "UTF-8";
        File file = new File(path + "/Info.txt");
        Map<String, String> result = new HashMap<>();
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                String[] args = lineTxt.split(":");
                if (args.length != 2) {
                    throw new Exception("授权文件格式错误");
                }
                args[0] = args[0].trim();
                args[1] = args[1].trim();
                result.put(args[0], args[1]);
            }
            bufferedReader.close();
            read.close();
        } else {
            System.out.println("找不到指定的授权文件");
        }
        Machine machine = null;
        if (result.containsKey("virtualID")) {
            machine = new Machine(result.get("virtualID"), result.get("mac"), "");
        } else {
            machine = new Machine(result.get("mainboard"), result.get("cpuid"), result.get("mac"),
                    "");
        }
        return machine;
    }
}
