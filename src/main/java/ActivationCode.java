import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author huangganyan
 * @date 2019/12/27 17:12
 */
public class ActivationCode {

    public static void encryData(Machine machine) throws Exception {
        if (machine.getActivationCode() == null) {
            System.out.println("machine's activationCode is not set");
            return;
        }
        SecretKey blowfishSecretKey =
                new SecretKeySpec(machine.getActivationCode().getBytes(), "Blowfish");
        String dataText = null;
        if (machine.getVirtualID() != null) {
            dataText = appendInfo(machine.getVirtualID(), machine.getMac(), machine.getOthers());
        } else {
            dataText = appendInfo(machine.getMainboard(), machine.getCpuid(), machine.getMac(),
                    machine.getOthers());
        }
        String CipherText = SHA1Util.encrypt_Base64(BlowFish.encrypt(blowfishSecretKey, dataText));
        machine.setData(CipherText);
        return;
    }

    public static String decryData(Machine machine) throws Exception {
        if (machine.getActivationCode() == null) {
            System.out.println("machine's activationCode is not set");
            return null;
        }
        if (machine.getData() == null) {
            System.out.println("machine's cipherData is not set");
            return null;
        }
        String activationCode = machine.getActivationCode();
        SecretKey blowfishSecretKey = new SecretKeySpec(activationCode.getBytes(), "Blowfish");
        return BlowFish.decrypt(blowfishSecretKey, SHA1Util.decrypt_Base64(machine.getData()));
    }

    public static void setKeyFromInfo(Machine machine) throws Exception {
        String activationCode = null;
        if (machine.getVirtualID() != null) {
            activationCode = appendInfo(machine.getVirtualID(), machine.getMac());
        } else {
            activationCode =
                    appendInfo(machine.getMainboard(), machine.getCpuid(), machine.getMac());
        }
        activationCode = SHA1Util.getSHA1Str(activationCode);
        machine.setActivationCode(activationCode);
        return;
    }

    public static String appendInfo(String... args) {
        if (args == null || args.length < 1) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        result.append(args[0]);
        for (int i = 1; i < args.length - 1; i++) {
            result.append(',');
            result.append(args[i]);
        }
        return result.toString();
    }
}
