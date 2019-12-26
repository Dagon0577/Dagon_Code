import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dagon0577
 * @date 2019/12/26 15:11
 */
public class SerialNumberUtil {

    /**
     * 获取主板序列号
     *
     * @return
     */
    public static String getMotherboardSN() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取CPU序列号
     *
     * @return
     */
    public static String getCPUSerial() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }

    /**
     * 获取localhost的LANAddress
     *
     * @return
     */
    private static List<String> getLocalHostLANAddress()
            throws UnknownHostException, SocketException {
        List<String> ips = new ArrayList<String>();
        Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
        while (interfs.hasMoreElements()) {
            NetworkInterface interf = interfs.nextElement();
            Enumeration<InetAddress> addres = interf.getInetAddresses();
            while (addres.hasMoreElements()) {
                InetAddress in = addres.nextElement();
                if (in instanceof Inet4Address) {
                    System.out.println("v4:" + in.getHostAddress());
                    if (!"127.0.0.1".equals(in.getHostAddress())) {
                        ips.add(in.getHostAddress());
                    }
                }
            }
        }
        return ips;
    }

    /**
     * MAC
     * 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
     *
     * @return
     */
    public static String getMac() {
        try {
            String resultStr = "";
            List<String> ls = getLocalHostLANAddress();
            int num = 0;
            for (String str : ls) {
                InetAddress ia = InetAddress.getByName(str);// 获取本地IP对象
                // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
                byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
                // 下面代码是把mac地址拼装成String
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                if (num == ls.size() - 1) {
                    resultStr += sb.toString().toUpperCase();
                } else {
                    // 把字符串所有小写字母改为大写成为正规的mac地址并返回
                    resultStr += sb.toString().toUpperCase() + ",";
                }
                num++;
            }
            return resultStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /***************************linux*********************************/

    public static String executeLinuxCmd(String cmd) {
        try {
            System.out.println("got cmd job : " + cmd);
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            in.close();
            process.destroy();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */
    public static String getSerialNumber(String cmd, String record, String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");

        for (String info : infos) {
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }

        return null;
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */
    public static String getAllSerialNumber(String cmd, String record, String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");
        StringBuilder result = new StringBuilder();
        int k = 0;
        for (int i = 0; i < infos.length - 1; i++) {
            String info = infos[i];
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                if (k != 0) {
                    result.append(',');
                }
                result.append(sn[1]);
                k++;
            }
        }
        if (k != 0) {
            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * 判断是否为容器、虚拟机，返回虚拟ID
     *
     * @return
     */
    public static String getVirtualID() {
        String execResult = executeLinuxCmd("systemd-detect-virt");
        if (!execResult.contains("none")) {
            //docker容器
            String VirtualID = getSerialNumber("cat /proc/1/cgroup", "docker", "docker/");
            if (VirtualID != null) {
                return VirtualID;
            }
            //machine-rkt
            VirtualID = getSerialNumber("cat /proc/1/cgroup", "machine-rkt", "machine-rkt\\");
            if (VirtualID != null) {
                VirtualID.replaceAll("\\x2d", "-");
                return VirtualID;
            }
            //vmware
            VirtualID = getSerialNumber("dmidecode -t system", "UUID", ":");
            if (VirtualID != null) {
                return VirtualID;
            }
            return "UNKNOWN";
        }
        return null;
    }

    /**
     * 获取CPUID、硬盘序列号、MAC地址、主板序列号
     *
     * @return
     */
    public static Map<String, String> getAllSn() {
        String os = System.getProperty("os.name");
        os = os.toUpperCase();
        System.out.println(os);

        Map<String, String> snVo = new HashMap<String, String>();

        if ("LINUX".equals(os)) {
            snVo.put("operating system", "LINUX");
            System.out.println("=============>for linux");
            String virtualID = getVirtualID();
            if (virtualID != null) {
                if (virtualID.equals("UNKNOWN")) {
                    System.out.println("UNKNOWN VMWARE!");
                    return snVo;
                } else {
                    System.out.println("virtualID : " + virtualID);
                    snVo.put("virtualID", virtualID.toUpperCase().replace(" ", ""));
                    String mac = getAllSerialNumber("ifconfig -a", "ether", " ");
                    System.out.println("mac : " + mac);
                    snVo.put("mac", mac.toUpperCase().replace(" ", ""));
                }
            } else {
                String cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
                System.out.println("cpuid : " + cpuid);
                String mainboardNumber =
                        getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":");
                System.out.println("mainboardNumber : " + mainboardNumber);
                String mac = getAllSerialNumber("ifconfig -a", "ether", " ");
                System.out.println("mac : " + mac);

                snVo.put("cpuid", cpuid.toUpperCase().replace(" ", ""));
                snVo.put("mac", mac.toUpperCase().replace(" ", ""));
                snVo.put("mainboard", mainboardNumber.toUpperCase().replace(" ", ""));
            }
        } else {
            snVo.put("operating system", "windows");
            System.out.println("=============>for windows");
            String cpuid = SerialNumberUtil.getCPUSerial();
            String mainboard = SerialNumberUtil.getMotherboardSN();
            //String disk = SerialNumberUtil.getHardDiskSN("c");
            String mac = SerialNumberUtil.getMac();

            System.out.println("CPU  SN:" + cpuid);
            System.out.println("主板  SN:" + mainboard);
            //System.out.println("C盘   SN:" + disk);
            System.out.println("MAC  SN:" + mac);

            snVo.put("cpuid", cpuid.toUpperCase().replace(" ", ""));
            //snVo.put("diskid", disk.toUpperCase().replace(" ", ""));
            snVo.put("mac", mac.toUpperCase().replace(" ", ""));
            snVo.put("mainboard", mainboard.toUpperCase().replace(" ", ""));
        }
        return snVo;
    }
}
