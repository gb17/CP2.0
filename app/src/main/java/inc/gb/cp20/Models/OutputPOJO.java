package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/11/16.
 */
public class OutputPOJO {
    private String OUT;
    private String MESSAGE;
    private String TXNID;

    public OutputPOJO(String OUT, String MESSAGE, String TXNID) {
        this.OUT = OUT;
        this.MESSAGE = MESSAGE;
        this.TXNID = TXNID;
    }

    public String getOUT() {
        return OUT;
    }

    public void setOUT(String OUT) {
        this.OUT = OUT;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getTXNID() {
        return TXNID;
    }

    public void setTXNID(String TXNID) {
        this.TXNID = TXNID;
    }
}
