package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham on 3/16/16.
 */
public class TBIMG {

    @SerializedName("COL0")
    @Expose
    private String COL0;
    @SerializedName("COL1")
    @Expose
    private String COL1;

    public String getCOL0() {
        return COL0;
    }

    public void setCOL0(String COL0) {
        this.COL0 = COL0;
    }

    public String getCOL1() {
        return COL1;
    }

    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    public String getKEY_COL() {
        return KEY_COL;
    }

    public void setKEY_COL(String KEY_COL) {
        this.KEY_COL = KEY_COL;
    }

    public String getOPRTN() {
        return OPRTN;
    }

    public void setOPRTN(String OPRTN) {
        this.OPRTN = OPRTN;
    }

    @SerializedName("KEY_COL")
    @Expose
    private String KEY_COL;
    @SerializedName("OPRTN")
    @Expose
    private String OPRTN;
}
