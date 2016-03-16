package inc.gb.cp20.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GB on 3/9/16.
 */
public class ACKTAG {


    @SerializedName("SUCCESSTAG")
    @Expose
    private String SUCCESSTAG;

    public String getFAILTAG() {
        return FAILTAG;
    }

    public void setFAILTAG(String FAILTAG) {
        this.FAILTAG = FAILTAG;
    }

    public String getREPCODE() {
        return REPCODE;
    }

    public void setREPCODE(String REPCODE) {
        this.REPCODE = REPCODE;
    }

    @SerializedName("FAILTAG")
    @Expose
    private String FAILTAG;


    @SerializedName("REPCODE")
    @Expose
    private String REPCODE;
    @SerializedName("CLIENTID")
    @Expose
    private String CLIENTID;



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



    public String getSUCCESSTAG() {
        return SUCCESSTAG;
    }

    public void setSUCCESSTAG(String SUCCESSTAG) {
        this.SUCCESSTAG = SUCCESSTAG;
    }
}
