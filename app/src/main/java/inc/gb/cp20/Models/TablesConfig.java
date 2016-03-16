
package inc.gb.cp20.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class TablesConfig {

    @SerializedName("TBDBL")
    @Expose
    private List<TBDBL> TBDBL = new ArrayList<TBDBL>();
    @SerializedName("TBDCP")
    @Expose
    private List<TBDCP> TBDCP = new ArrayList<TBDCP>();
    @SerializedName("TBDSP")
    @Expose
    private List<TBDSP> TBDSP = new ArrayList<TBDSP>();
    @SerializedName("TBDTH")
    @Expose
    private List<TBDTH> TBDTH = new ArrayList<TBDTH>();
    @SerializedName("TBDPG")
    @Expose
    private List<TBDPG> TBDPG = new ArrayList<TBDPG>();
    @SerializedName("TBTBC")
    @Expose
    private List<TBTBC> TBTBC = new ArrayList<TBTBC>();


    @SerializedName("TBDPS")
    @Expose
    private List<TBDPS> TBDPS = new ArrayList<TBDPS>();

    public List<TBPARTY> getTBPARTY() {
        return TBPARTY;
    }

    @SerializedName("TBPARTY")
    @Expose
    private List<TBPARTY> TBPARTY = new ArrayList<TBPARTY>();

    public List<inc.gb.cp20.Models.TBBRAND> getTBBRAND() {
        return TBBRAND;
    }

    public void setTBBRAND(List<inc.gb.cp20.Models.TBBRAND> TBBRAND) {
        this.TBBRAND = TBBRAND;
    }

    @SerializedName("TBBRAND")
    @Expose
    private List<TBBRAND> TBBRAND = new ArrayList<TBBRAND>();

    public void setTBPARTY(List<inc.gb.cp20.Models.TBPARTY> TBPARTY) {
        this.TBPARTY = TBPARTY;
    }

    public List<inc.gb.cp20.Models.TBDPS> getTBDPS() {
        return TBDPS;
    }

    public void setTBDPS(List<inc.gb.cp20.Models.TBDPS> TBDPS) {
        this.TBDPS = TBDPS;
    }



    /**
     * @return The TBDBL
     */
    public List<TBDBL> getTBDBL() {
        return TBDBL;
    }

    /**
     * @param TBDBL The TBDBL
     */
    public void setTBDBL(List<TBDBL> TBDBL) {

        this.TBDBL = TBDBL;
    }

    /**
     * @return The TBDCP
     */
    public List<TBDCP> getTBDCP() {
        return TBDCP;
    }

    /**
     * @param TBDCP The TBDCP
     */
    public void setTBDCP(List<TBDCP> TBDCP) {
        this.TBDCP = TBDCP;
    }

    /**
     * @return The TBDSP
     */
    public List<TBDSP> getTBDSP() {
        return TBDSP;
    }

    /**
     * @param TBDSP The TBDSP
     */
    public void setTBDSP(List<TBDSP> TBDSP) {
        this.TBDSP = TBDSP;
    }

    /**
     * @return The TBDTH
     */
    public List<TBDTH> getTBDTH() {
        return TBDTH;
    }

    /**
     * @param TBDTH The TBDTH
     */
    public void setTBDTH(List<TBDTH> TBDTH) {
        this.TBDTH = TBDTH;
    }

    /**
     * @return The TBDPG
     */
    public List<TBDPG> getTBDPG() {
        return TBDPG;
    }

    /**
     * @param TBDPG The TBDPG
     */
    public void setTBDPG(List<TBDPG> TBDPG) {
        this.TBDPG = TBDPG;
    }

    /**
     * @return The TBTBC
     */
    public List<TBTBC> getTBTBC() {
        return TBTBC;
    }

    /**
     * @param TBTBC The TBTBC
     */
    public void setTBTBC(List<TBTBC> TBTBC) {
        this.TBTBC = TBTBC;
    }

    public List<inc.gb.cp20.Models.TBDMENU> getTBDMENU() {
        return TBDMENU;
    }

    public void setTBDMENU(List<inc.gb.cp20.Models.TBDMENU> TBDMENU) {
        this.TBDMENU = TBDMENU;
    }

    @SerializedName("TBDMENU")
    @Expose
    private List<TBDMENU> TBDMENU = new ArrayList<TBDMENU>();

}
