
package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TBDCP {

    @SerializedName("COL0")
    @Expose
    private String COL0;
    @SerializedName("COL1")
    @Expose
    private String COL1;
    @SerializedName("COL2")
    @Expose
    private String COL2;
    @SerializedName("KEY_COL")
    @Expose
    private String KEY_COL;
    @SerializedName("OPTRN")
    @Expose
    private String OPTRN;

    /**
     *
     * @return
     *     The COL0
     */
    public String getCOL0() {
        return COL0;
    }

    /**
     *
     * @param COL0
     *     The COL0
     */
    public void setCOL0(String COL0) {
        this.COL0 = COL0;
    }

    /**
     *
     * @return
     *     The COL1
     */
    public String getCOL1() {
        return COL1;
    }

    /**
     *
     * @param COL1
     *     The COL1
     */
    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    /**
     *
     * @return
     *     The COL2
     */
    public String getCOL2() {
        return COL2;
    }

    /**
     *
     * @param COL2
     *     The COL2
     */
    public void setCOL2(String COL2) {
        this.COL2 = COL2;
    }

    /**
     *
     * @return
     *     The KEY_COL
     */
    public String getKEY_COL() {
        return KEY_COL;
    }

    /**
     *
     * @param KEY_COL
     *     The KEY_COL
     */
    public void setKEY_COL(String KEY_COL) {
        this.KEY_COL = KEY_COL;
    }

    /**
     *
     * @return
     *     The OPTRN
     */
    public String getOPTRN() {
        return OPTRN;
    }

    /**
     *
     * @param OPTRN
     *     The OPTRN
     */
    public void setOPTRN(String OPTRN) {
        this.OPTRN = OPTRN;
    }

}
