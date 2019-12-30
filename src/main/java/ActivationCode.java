import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Dagon0577
 * @date 2019/12/27 17:12
 */
public class ActivationCode {

    public static boolean getCompareResult(String cipherTextStr, Machine machine) throws Exception {
        if (cipherTextStr == null || machine == null) {
            System.out.println("cipherTextStr data or machine is null");
            return false;
        }
        String cipherTexts[] = decryData(cipherTextStr, machine).split(",");
        String time = cipherTexts[cipherTexts.length - 1];
        if (Long.valueOf(time) < machine.getTime()) {
            return false;
        }
        return true;
    }

    public static SecretKey getKey(Machine machine) {
        String activationCode = machine.getActivationCode();
        if (activationCode == null) {
            return null;
        }
        SecretKey blowfishSecretKey = new SecretKeySpec(activationCode.getBytes(), "Blowfish");
        return blowfishSecretKey;
    }

    public static void encryData(Machine machine) throws Exception {
        if (machine.getActivationCode() == null) {
            System.out.println("machine's activationCode is not set");
            return;
        }
        SecretKey blowfishSecretKey = getKey(machine);
        String dataText = getMachineDataText(machine);
        String CipherText = SHA1Util.encrypt_Base64(BlowFish.encrypt(blowfishSecretKey, dataText));
        machine.setData(CipherText);
        return;
    }

    public static String decryData(String cipherTextStr, Machine machine) throws Exception {
        if (machine.getActivationCode() == null) {
            System.out.println("machine's activationCode is not set");
            return null;
        }
        SecretKey blowfishSecretKey = getKey(machine);
        return BlowFish.decrypt(blowfishSecretKey, SHA1Util.decrypt_Base64(cipherTextStr));
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
        SecretKey blowfishSecretKey = getKey(machine);
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

    public static String getMachineDataText(Machine machine) {
        if (machine == null) {
            return null;
        }
        String dataText = null;
        if (machine.getVirtualID() != null) {
            dataText = appendInfo(machine.getVirtualID(), machine.getMac(),
                    String.valueOf(machine.getTime()));
        } else {
            dataText = appendInfo(machine.getMainboard(), machine.getCpuid(), machine.getMac(),
                    String.valueOf(machine.getTime()));
        }
        return dataText;
    }

    public static String appendInfo(String... args) {
        if (args == null || args.length < 1) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        result.append(args[0]);
        for (int i = 1; i < args.length; i++) {
            result.append(',');
            result.append(args[i]);
        }
        return result.toString();
    }
}
