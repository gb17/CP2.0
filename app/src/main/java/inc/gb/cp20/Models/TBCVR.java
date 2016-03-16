package inc.gb.cp20.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class TBCVR {

    @SerializedName("COL0")
    @Expose
    private String COL0;
    @SerializedName("COL1")
    @Expose
    private String COL1;
    @SerializedName("COL2")
    @Expose
    private String COL2;
    @SerializedName("COL3")
    @Expose
    private String COL3;

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    @SerializedName("MSG")
    @Expose
    private String MSG;

    /**
     * @return The COL0
     */
    public String getCOL0() {
        return COL0;
    }

    /**
     * @param COL0 The COL0
     */
    public void setCOL0(String COL0) {
        this.COL0 = COL0;
    }

    /**
     * @return The COL1
     */
    public String getCOL1() {
        return COL1;
    }

    /**
     * @param COL1 The COL1
     */
    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    /**
     * @return The COL2
     */
    public String getCOL2() {
        return COL2;
    }

    /**
     * @param COL2 The COL2
     */
    public void setCOL2(String COL2) {
        this.COL2 = COL2;
    }

    /**
     * @return The COL3
     */
    public String getCOL3() {
        return COL3;
    }

    /**
     * @param COL3 The COL3
     */
    public void setCOL3(String COL3) {
        this.COL3 = COL3;
    }

}

