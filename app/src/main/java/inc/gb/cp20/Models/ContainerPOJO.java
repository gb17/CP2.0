package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/10/16.
 */
public class ContainerPOJO {
    private String CLIENTID;
    private String REPCODE;
    private String BU;
    private String GEOCODE;
    private String PATCHCODE;
    private String TXNDATE;
    private String CODE;
    private String NAME;
    private String BRNDCODE;
    private String CNTNT_ID;
    private String DURATION;
    private String STARTTIME;
    private String EMAIL_FLAG;
    private String LONGITUDE;
    private String LATITUDE;
    private String LIKE_FLAG;
    private String SOURCE;
    private String REFPAGEID;
    private String PCODE;
    private String OUT;
    private String MESSAGE;
    private String TXNID;
    private String PLAYLISTID;
    private String BATCHCODE;
    private String PARAM1;
    private String PARAM2;
    private String PARAM3;
    private String PARAM4;


    public ContainerPOJO(String CLIENTID, String REPCODE, String BU, String GEOCODE, String PATCHCODE, String TXNDATE, String CODE, String NAME, String BRNDCODE, String CNTNT_ID, String DURATION, String STARTTIME, String EMAIL_FLAG, String LONGITUDE, String LATITUDE, String LIKE_FLAG, String SOURCE, String REFPAGEID, String PCODE, String OUT, String MESSAGE, String TXNID, String PLAYLISTID, String BATCHCODE, String PARAM1, String PARAM2, String PARAM3, String PARAM4) {
        this.CLIENTID = CLIENTID;
        this.REPCODE = REPCODE;
        this.BU = BU;
        this.GEOCODE = GEOCODE;
        this.PATCHCODE = PATCHCODE;
        this.TXNDATE = TXNDATE;
        this.CODE = CODE;
        this.NAME = NAME;
        this.BRNDCODE = BRNDCODE;
        this.CNTNT_ID = CNTNT_ID;
        this.DURATION = DURATION;
        this.STARTTIME = STARTTIME;
        this.EMAIL_FLAG = EMAIL_FLAG;
        this.LONGITUDE = LONGITUDE;
        this.LATITUDE = LATITUDE;
        this.LIKE_FLAG = LIKE_FLAG;
        this.SOURCE = SOURCE;
        this.REFPAGEID = REFPAGEID;
        this.PCODE = PCODE;
        this.OUT = OUT;
        this.MESSAGE = MESSAGE;
        this.TXNID = TXNID;
        this.PLAYLISTID = PLAYLISTID;
        this.BATCHCODE = BATCHCODE;
        this.PARAM1 = PARAM1;
        this.PARAM2 = PARAM2;
        this.PARAM3 = PARAM3;
        this.PARAM4 = PARAM4;
    }

    public String getBATCHCODE() {
        return BATCHCODE;
    }

    public void setBATCHCODE(String BATCHCODE) {
        this.BATCHCODE = BATCHCODE;
    }

    public String getPARAM1() {
        return PARAM1;
    }

    public void setPARAM1(String PARAM1) {
        this.PARAM1 = PARAM1;
    }

    public String getPARAM2() {
        return PARAM2;
    }

    public void setPARAM2(String PARAM2) {
        this.PARAM2 = PARAM2;
    }

    public String getPARAM3() {
        return PARAM3;
    }

    public void setPARAM3(String PARAM3) {
        this.PARAM3 = PARAM3;
    }

    public String getPARAM4() {
        return PARAM4;
    }

    public void setPARAM4(String PARAM4) {
        this.PARAM4 = PARAM4;
    }

    public String getPLAYLISTID() {
        return PLAYLISTID;
    }

    public void setPLAYLISTID(String PLAYLISTID) {
        this.PLAYLISTID = PLAYLISTID;
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

    public String getBU() {
        return BU;
    }

    public void setBU(String BU) {
        this.BU = BU;
    }

    public String getGEOCODE() {
        return GEOCODE;
    }

    public void setGEOCODE(String GEOCODE) {
        this.GEOCODE = GEOCODE;
    }

    public String getPATCHCODE() {
        return PATCHCODE;
    }

    public void setPATCHCODE(String PATCHCODE) {
        this.PATCHCODE = PATCHCODE;
    }

    public String getTXNDATE() {
        return TXNDATE;
    }

    public void setTXNDATE(String TXNDATE) {
        this.TXNDATE = TXNDATE;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getBRNDCODE() {
        return BRNDCODE;
    }

    public void setBRNDCODE(String BRNDCODE) {
        this.BRNDCODE = BRNDCODE;
    }

    public String getCNTNT_ID() {
        return CNTNT_ID;
    }

    public void setCNTNT_ID(String CNTNT_ID) {
        this.CNTNT_ID = CNTNT_ID;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getSTARTTIME() {
        return STARTTIME;
    }

    public void setSTARTTIME(String STARTTIME) {
        this.STARTTIME = STARTTIME;
    }

    public String getEMAIL_FLAG() {
        return EMAIL_FLAG;
    }

    public void setEMAIL_FLAG(String EMAIL_FLAG) {
        this.EMAIL_FLAG = EMAIL_FLAG;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLIKE_FLAG() {
        return LIKE_FLAG;
    }

    public void setLIKE_FLAG(String LIKE_FLAG) {
        this.LIKE_FLAG = LIKE_FLAG;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getREFPAGEID() {
        return REFPAGEID;
    }

    public void setREFPAGEID(String REFPAGEID) {
        this.REFPAGEID = REFPAGEID;
    }

    public String getPCODE() {
        return PCODE;
    }

    public void setPCODE(String PCODE) {
        this.PCODE = PCODE;
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
