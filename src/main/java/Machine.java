import java.util.UUID;

/**
 * @author huangganyan
 * @date 2019/12/27 16:57
 */
public class Machine {
    private UUID id;
    private String mainboard;
    private String cpuid;
    private String mac;
    private String virtualID;
    private String others;
    private String activationCode;
    private String data;

    public Machine(String mainboard, String cpuid, String mac, String others) {
        this.id = UUID.randomUUID();
        this.mainboard = mainboard;
        this.cpuid = cpuid;
        this.mac = mac;
        this.virtualID = null;
        this.others = others;
        this.activationCode = null;
        this.data = null;
    }

    public Machine(String virtualID, String mac, String others) {
        this.id = UUID.randomUUID();
        this.mainboard = null;
        this.cpuid = null;
        this.mac = mac;
        this.virtualID = virtualID;
        this.others = others;
        this.activationCode = null;
        this.data = null;
    }

    public String getMainboard() {
        return mainboard;
    }

    public void setMainboard(String mainboard) {
        this.mainboard = mainboard;
    }

    public String getCpuid() {
        return cpuid;
    }

    public void setCpuid(String cpuid) {
        this.cpuid = cpuid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getVirtualID() {
        return virtualID;
    }

    public void setVirtualID(String virtualID) {
        this.virtualID = virtualID;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
