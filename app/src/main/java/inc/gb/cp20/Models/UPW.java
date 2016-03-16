package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UPW {

    @SerializedName("USERNAME")
    @Expose
    private String USERNAME;
    @SerializedName("VERSION")
    @Expose
    private String VERSION;
    @SerializedName("OLDPWD")
    @Expose
    private String OLDPWD;
    @SerializedName("VALUE")
    @Expose
    private String VALUE;
    @SerializedName("CLIENTID")
    @Expose
    private String CLIENTID;
    @SerializedName("CONTROLNO")
    @Expose
    private String CONTROLNO;
    @SerializedName("MSG")
    @Expose
    private String MSG;

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }



    /**
     * @return The USERNAME
     */
    public String getUSERNAME() {
        return USERNAME;
    }

    /**
     * @param USERNAME The USERNAME
     */
    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
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
     * @return The OLDPWD
     */
    public String getOLDPWD() {
        return OLDPWD;
    }

    /**
     * @param OLDPWD The OLDPWD
     */
    public void setOLDPWD(String OLDPWD) {
        this.OLDPWD = OLDPWD;
    }

    /**
     * @return The VALUE
     */
    public String getVALUE() {
        return VALUE;
    }

    /**
     * @param VALUE The VALUE
     */
    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
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
     * @return The CONTROLNO
     */
    public String getCONTROLNO() {
        return CONTROLNO;
    }

    /**
     * @param CONTROLNO The CONTROLNO
     */
    public void setCONTROLNO(String CONTROLNO) {
        this.CONTROLNO = CONTROLNO;
    }

}
