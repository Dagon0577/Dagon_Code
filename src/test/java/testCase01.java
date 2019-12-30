import junit.framework.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dagon0577
 * @date 2019/12/27 18:21
 */
public class testCase01 {

    @Test public void testCase() throws Exception {
        MachineInfo.generateInfo();
        Machine machine = MachineInfo.getMachineFromInfo();
        String dataText = ActivationCode.getMachineDataText(machine);
        ActivationCode.setKeyFromInfo(machine);
        ActivationCode.encryData(machine);
        System.out.println("encryData : " + machine.getData());
        System.out.println("dataText : " + dataText);
        Assert.assertEquals(ActivationCode.decryData(machine), dataText);
        System.out.println("decryDate : " + ActivationCode.decryData(machine));
    }

    @Test public void testCaseCompare() throws Exception {
        MachineInfo.generateInfo();
        Machine machine = MachineInfo.getMachineFromInfo();
        String dataText = ActivationCode.getMachineDataText(machine);
        ActivationCode.setKeyFromInfo(machine);
        ActivationCode.encryData(machine);
        System.out.println("2025-01-01 00:00:00 encryData : " + machine.getData());
        System.out.println("dataText : " + dataText);

        //验证日期为当前日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        format.format(nowDate);
        long nowTime = nowDate.getTime();

        machine.setTime(nowTime);
        Assert.assertTrue(ActivationCode.getCompareResult(machine.getData(), machine));
        System.out.println("激活码属于该机器，且验证通过");
    }
}
