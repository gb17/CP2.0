package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/12/16.
 */
public class IRCSFPOJO {
    private String CLIENTID;
    private String REPCODE;
    private String VERSION;

    public IRCSFPOJO(String CLIENTID, String REPCODE, String VERSION) {
        this.CLIENTID = CLIENTID;
        this.REPCODE = REPCODE;
        this.VERSION = VERSION;
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

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }
}
