package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqCVR {

    @SerializedName("REPCODE")
    @Expose
    private String REPCODE;
    @SerializedName("VERSION")
    @Expose
    private String VERSION;
    @SerializedName("INSTANCEID")
    @Expose
    private String INSTANCEID;
    @SerializedName("ROLECODE")
    @Expose
    private String ROLECODE;
    @SerializedName("CLIENTID")
    @Expose
    private String CLIENTID;
    @SerializedName("APPVERSION")
    @Expose
    private String APPVERSION;
    @SerializedName("Flag")
    @Expose
    private String Flag;

    /**
     * @return The REPCODE
     */
    public String getREPCODE() {
        return REPCODE;
    }

    /**
     * @param REPCODE The REPCODE
     */
    public void setREPCODE(String REPCODE) {
        this.REPCODE = REPCODE;
    }

    /**
     * @return The VERSION
     */
    public String getVERSION() {
        return VERSION;
    }

    /**
     * @param VERSION The VERSION
     */
    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    /**
     * @return The INSTANCEID
     */
    public String getINSTANCEID() {
        return INSTANCEID;
    }

    /**
     * @param INSTANCEID The INSTANCEID
     */
    public void setINSTANCEID(String INSTANCEID) {
        this.INSTANCEID = INSTANCEID;
    }

    /**
     * @return The ROLECODE
     */
    public String getROLECODE() {
        return ROLECODE;
    }

    /**
     * @param ROLECODE The ROLECODE
     */
    public void setROLECODE(String ROLECODE) {
        this.ROLECODE = ROLECODE;
    }

    /**
     * @return The CLIENTID
     */
    public String getCLIENTID() {
        return CLIENTID;
    }

    /**
     * @param CLIENTID The CLIENTID
     */
    public void setCLIENTID(String CLIENTID) {
        this.CLIENTID = CLIENTID;
    }

    /**
     * @return The APPVERSION
     */
    public String getAPPVERSION() {
        return APPVERSION;
    }

    /**
     * @param APPVERSION The APPVERSION
     */
    public void setAPPVERSION(String APPVERSION) {
        this.APPVERSION = APPVERSION;
    }

    /**
     * @return The Flag
     */
    public String getFlag() {
        return Flag;
    }

    /**
     * @param Flag The Flag
     */
    public void setFlag(String Flag) {
        this.Flag = Flag;
    }

}