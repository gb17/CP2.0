package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/19/16.
 */
public class SyncDetailingAckPOJO {

    public String CLIENTID;
    public String REPCODE;
    public String BATCHCODE;
    public String OUT;

    public SyncDetailingAckPOJO(String CLIENTID, String REPCODE, String BATCHCODE, String OUT) {
        this.BATCHCODE = BATCHCODE;
        this.CLIENTID = CLIENTID;
        this.REPCODE = REPCODE;
        this.OUT = OUT;
    }

    public String getCLIENTID() {
        return CLIENTID;
    }

    public void setCLIENTID(String CLIENTID) {
        this.CLIENTID = CLIENTID;
    }

    public String getREPCODE() {
        return REPCODE;
    }

    public void setREPCODE(String REPCODE) {
        this.REPCODE = REPCODE;
    }

    public String getBATCHCODE() {
        return BATCHCODE;
    }

    public void setBATCHCODE(String BATCHCODE) {
        this.BATCHCODE = BATCHCODE;
    }

    public String getOUT() {
        return OUT;
    }

    public void setOUT(String OUT) {
        this.OUT = OUT;
    }
}
