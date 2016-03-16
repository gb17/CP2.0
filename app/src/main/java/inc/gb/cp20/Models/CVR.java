package inc.gb.cp20.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CVR {

    @SerializedName("TBCVR")
    @Expose
    private List<TBCVR> TBCVR = new ArrayList<TBCVR>();

    /**
     * @return The TBCVR
     */
    public List<TBCVR> getTBCVR() {
        return TBCVR;
    }

    /**
     * @param TBCVR The TBCVR
     */
    public void setTBCVR(List<TBCVR> TBCVR) {
        this.TBCVR = TBCVR;
    }

}