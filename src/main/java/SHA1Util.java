import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author huangganyan
 * @date 2019/12/27 17:05
 */
public class SHA1Util {
    /**
     * 对字符串sha1加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String getSHA1Str(String str) throws Exception {
        try {
            // 生成一个SHA1加密计算摘要
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            // 计算sha1函数
            sha1.update(str.getBytes());
            // digest()最后确定返回sha1 hash值，返回值为8为字符串。因为sha1 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, sha1.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("Encryption error，" + e.toString());
        }
    }

    /***
     * Base64加密
     * @param text 需要加密的byte[]
     * @return
     * @throws Exception
     */
    public static String encrypt_Base64(byte[] text) throws Exception {
        String result = Base64.getEncoder().encodeToString(text);
        return result;
    }

    /***
     * Base64解密
     * @param str 需要解密的参数
     * @return
     * @throws Exception
     */
    public static byte[] decrypt_Base64(String str) throws Exception {
        byte[] result = Base64.getDecoder().decode(str);
        return result;
    }
}
