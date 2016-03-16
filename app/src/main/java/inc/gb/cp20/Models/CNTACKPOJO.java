package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/12/16.
 */
public class CNTACKPOJO {

    private String CLIENTID;
    private String REPCODE;
    private String ENTRYNO;

    public CNTACKPOJO(String CLIENTID, String REPCODE, String ENTRYNO) {
        this.CLIENTID = CLIENTID;
        this.REPCODE = REPCODE;
        this.ENTRYNO = ENTRYNO;
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

    public String getENTRYNO() {
        return ENTRYNO;
    }

    public void setENTRYNO(String ENTRYNO) {
        this.ENTRYNO = ENTRYNO;
    }
}
