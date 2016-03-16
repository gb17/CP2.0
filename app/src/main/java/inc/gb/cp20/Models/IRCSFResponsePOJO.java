package inc.gb.cp20.Models;

/**
 * Created by Shubham on 3/12/16.
 */
public class IRCSFResponsePOJO {

    private String PATH;
    private String FILE_SIZE;
    private String ENTRYNO;
    private String CNT_CATEGORY;
    private String SUB_CATEGORY;
    private String CNT_TYPE;
    private String PAGE_NAME;
    private String EFFECT_DATE;

    public IRCSFResponsePOJO(String PATH, String FILE_SIZE, String ENTRYNO, String CNT_CATEGORY, String SUB_CATEGORY, String CNT_TYPE, String PAGE_NAME, String EFFECT_DATE) {
        this.PATH = PATH;
        this.FILE_SIZE = FILE_SIZE;
        this.ENTRYNO = ENTRYNO;
        this.CNT_CATEGORY = CNT_CATEGORY;
        this.SUB_CATEGORY = SUB_CATEGORY;
        this.CNT_TYPE = CNT_TYPE;
        this.PAGE_NAME = PAGE_NAME;
        this.EFFECT_DATE = EFFECT_DATE;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getFILE_SIZE() {
        return FILE_SIZE;
    }

    public void setFILE_SIZE(String FILE_SIZE) {
        this.FILE_SIZE = FILE_SIZE;
    }

    public String getENTRYNO() {
        return ENTRYNO;
    }

    public void setENTRYNO(String ENTRYNO) {
        this.ENTRYNO = ENTRYNO;
    }

    public String getCNT_CATEGORY() {
        return CNT_CATEGORY;
    }

    public void setCNT_CATEGORY(String CNT_CATEGORY) {
        this.CNT_CATEGORY = CNT_CATEGORY;
    }

    public String getSUB_CATEGORY() {
        return SUB_CATEGORY;
    }

    public void setSUB_CATEGORY(String SUB_CATEGORY) {
        this.SUB_CATEGORY = SUB_CATEGORY;
    }

    public String getCNT_TYPE() {
        return CNT_TYPE;
    }

    public void setCNT_TYPE(String CNT_TYPE) {
        this.CNT_TYPE = CNT_TYPE;
    }

    public String getPAGE_NAME() {
        return PAGE_NAME;
    }

    public void setPAGE_NAME(String PAGE_NAME) {
        this.PAGE_NAME = PAGE_NAME;
    }

    public String getEFFECT_DATE() {
        return EFFECT_DATE;
    }

    public void setEFFECT_DATE(String EFFECT_DATE) {
        this.EFFECT_DATE = EFFECT_DATE;
    }
}
