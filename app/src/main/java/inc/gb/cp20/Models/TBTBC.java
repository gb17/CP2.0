
package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TBTBC {

    @SerializedName("TAG")
    @Expose
    private String TAG;
    @SerializedName("REC_COUNT")
    @Expose
    private String REC_COUNT;
    @SerializedName("MOB_COUNT")
    @Expose
    private String MOB_COUNT;
    @SerializedName("KEY_COL")
    @Expose
    private String KEY_COL;
    @SerializedName("OPTRN")
    @Expose
    private String OPTRN;

    /**
     * @return The TAG
     */
    public String getTAG() {
        return TAG;
    }

    /**
     * @param TAG The TAG
     */
    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    /**
     * @return The REC_COUNT
     */
    public String getREC_COUNT() {
        return REC_COUNT;
    }

    /**
     * @param REC_COUNT The REC_COUNT
     */
    public void setREC_COUNT(String REC_COUNT) {
        this.REC_COUNT = REC_COUNT;
    }

    /**
     * @return The MOB_COUNT
     */
    public String getMOB_COUNT() {
        return MOB_COUNT;
    }

    /**
     * @param MOB_COUNT The MOB_COUNT
     */
    public void setMOB_COUNT(String MOB_COUNT) {
        this.MOB_COUNT = MOB_COUNT;
    }

    /**
     * @return The KEY_COL
     */
    public String getKEY_COL() {
        return KEY_COL;
    }

    /**
     * @param KEY_COL The KEY_COL
     */
    public void setKEY_COL(String KEY_COL) {
        this.KEY_COL = KEY_COL;
    }

    /**
     * @return The OPTRN
     */
    public String getOPTRN() {
        return OPTRN;
    }

    /**
     * @param OPTRN The OPTRN
     */
    public void setOPTRN(String OPTRN) {
        this.OPTRN = OPTRN;
    }

}
