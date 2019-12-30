import junit.framework.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Dagon0577
 * @date 2019/12/27 18:21
 */
public class testCase01 {

    @Test public void testCase() throws Exception {
        MachineInfo.generateInfo();

        //过期日期暂时定死为2025-01-01 00:00:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse("2025-01-01 00:00:00"));
        Date y = c.getTime();
        long time = y.getTime();

        Machine machine = MachineInfo.getMachineFromInfo(time);
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

        //过期日期暂时定死为2025-01-01 00:00:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse("2025-01-01 00:00:00"));
        Date y = c.getTime();
        long time = y.getTime();

        Machine machine = MachineInfo.getMachineFromInfo(time);
        String dataText = ActivationCode.getMachineDataText(machine);
        ActivationCode.setKeyFromInfo(machine);
        ActivationCode.encryData(machine);
        System.out.println("2025-01-01 00:00:00 encryData : " + machine.getData());
        System.out.println("dataText : " + dataText);

        //验证日期为当前日期
        Date nowDate = new Date();
        format.format(nowDate);
        long nowTime = nowDate.getTime();

        machine.setTime(nowTime);
        Assert.assertTrue(ActivationCode.getCompareResult(machine.getData(), machine));
        System.out.println("激活码属于该机器，且验证通过");
    }
}
