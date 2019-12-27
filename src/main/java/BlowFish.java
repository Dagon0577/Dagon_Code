import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

/**
 * @author Dagon0577
 * @date 2019/12/27 17:08
 */
public class BlowFish {


    /**
     * @return
     * @throws Exception
     */
    public static Key keyGenerator() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * 加密
     *
     * @param key
     * @param text
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(Key key, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text.getBytes());
    }

    /**
     * 解密
     *
     * @param key
     * @param bt
     * @return
     * @throws Exception
     */
    public static String decrypt(Key key, byte[] bt) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(bt));
    }
}
