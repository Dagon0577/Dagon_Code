import junit.framework.Assert;
import org.junit.Test;

/**
 * @author huangganyan
 * @date 2019/12/27 18:21
 */
public class testCase01 {

    @Test public void testCase() throws Exception {
        MachineInfo.generateInfo();
        Machine machine = MachineInfo.getMachineFromInfo();
        String dataText = null;
        if (machine.getVirtualID() != null) {
            dataText = ActivationCode
                    .appendInfo(machine.getVirtualID(), machine.getMac(), machine.getOthers());
        } else {
            dataText = ActivationCode
                    .appendInfo(machine.getMainboard(), machine.getCpuid(), machine.getMac(),
                            machine.getOthers());
        }
        ActivationCode.setKeyFromInfo(machine);
        ActivationCode.encryData(machine);
        System.out.println("encryData : " + machine.getData());
        System.out.println("dataText : " + dataText);
        Assert.assertEquals(ActivationCode.decryData(machine), dataText);
        System.out.println("decryDate : " + ActivationCode.decryData(machine));
    }
}
