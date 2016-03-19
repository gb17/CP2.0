package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GB on 3/10/16.
 */
public class ChangePassword {
    @SerializedName("BU")
    @Expose
    private String BU;
    @SerializedName("REPCODE")
    @Expose
    private String REPCODE;
    @SerializedName("OPWD")
    @Expose
    private String OPWD;
    @SerializedName("CLIENTID")
    @Expose
    private String CLIENTID;
    @SerializedName("NPWD")
    @Expose
    private String NPWD;

    @SerializedName("MOBILENO")
    @Expose
    private String MOBILENO;

    @SerializedName("OUT")
    @Expose
    private String OUT;

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    @SerializedName("MSG")
    @Expose
    private String MSG;

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }

    @SerializedName("CVR")
    @Expose
    private String CVR;

    public String getMOBILENO() {
        return MOBILENO;
    }

    public void setMOBILENO(String MOBILENO) {
        this.MOBILENO = MOBILENO;
    }

    public String getOUT() {
        return OUT;
    }

    public void setOUT(String OUT) {
        this.OUT = OUT;
    }

    /**
     * @return The BU
     */
    public String getBU() {
        return BU;
    }

    /**
     * @param BU The BU
     */
    public void setBU(String BU) {
        this.BU = BU;
    }

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
     * @return The OPWD
     */
    public String getOPWD() {
        return OPWD;
    }

    /**
     * @param OPWD The OPWD
     */
    public void setOPWD(String OPWD) {
        this.OPWD = OPWD;
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
     * @return The NPWD
     */
    public String getNPWD() {
        return NPWD;
    }

    /**
     * @param NPWD The NPWD
     */
    public void setNPWD(String NPWD) {
        this.NPWD = NPWD;
    }

}

