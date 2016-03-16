package inc.gb.cp20.AlphaList;

import java.util.Locale;

public class DrList_POJO implements Comparable<DrList_POJO> {

    private String COL0;//  partycode
    private String COL1;// Partyname
    private String COL2;//patchcode
    private String COL3;//  null
    private String COL4;//  partytype
    private String COL5;//  spclcode
    private String COL6;//  plan_status
    private String COL7;// patchname
    private String COL8;// territory_name
    private String COL9;// trcode
    private String COL10;//  specialty name
    private String COL11;//doctor_class
    private String COL12;// doctorFREQMET
    private String COL13;//TERR Code
    private String COL14;// without Intial
    private String COL15;// flag

    public String getCOL16() {
        return COL16;
    }

    public void setCOL16(String COL16) {
        this.COL16 = COL16;
    }

    private String COL16;// Cat TYpe

    public String getCOL14() {
        return COL14;
    }

    public void setCOL14(String COL14) {
        this.COL14 = COL14;
    }

    public String getCOL15() {
        return COL15;
    }

    public void setCOL15(String COL15) {
        this.COL15 = COL15;
    }




    public DrList_POJO() {

    }

    public DrList_POJO(String COL14, String COL15,String COL16,String COL13, String COL1, String COL2, String COL3, String COL4, String COL5, String COL6, String COL7, String COL8, String COL9, String COL10, String COL11, String COL12, String COL0) {
        this.COL14 = COL14;
        this.COL16 = COL16;
        this.COL15 = COL15;
        this.COL13 = COL13;
        this.COL1 = COL1;
        this.COL2 = COL2;
        this.COL3 = COL3;
        this.COL4 = COL4;
        this.COL5 = COL5;
        this.COL6 = COL6;
        this.COL7 = COL7;
        this.COL8 = COL8;
        this.COL9 = COL9;
        this.COL10 = COL10;
        this.COL11 = COL11;
        this.COL12 = COL12;
        this.COL0 = COL0;
    }

    public String getCOL5() {
        return COL5;
    }

    public void setCOL5(String COL5) {
        this.COL5 = COL5;
    }

    public String getCOL0() {
        return COL0;
    }

    public void setCOL0(String COL0) {
        this.COL0 = COL0;
    }

    public String getCOL1() {
        return COL1;
    }

    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    public String getCOL2() {
        return COL2;
    }

    public void setCOL2(String COL2) {
        this.COL2 = COL2;
    }

    public String getCOL3() {
        return COL3;
    }

    public void setCOL3(String COL3) {
        this.COL3 = COL3;
    }

    public String getCOL4() {
        return COL4;
    }

    public void setCOL4(String COL4) {
        this.COL4 = COL4;
    }

    public String getCOL6() {
        return COL6;
    }

    public void setCOL6(String COL6) {
        this.COL6 = COL6;
    }

    public String getCOL7() {
        return COL7;
    }

    public void setCOL7(String COL7) {
        this.COL7 = COL7;
    }

    public String getCOL8() {
        return COL8;
    }

    public void setCOL8(String COL8) {
        this.COL8 = COL8;
    }

    public String getCOL9() {
        return COL9;
    }

    public void setCOL9(String COL9) {
        this.COL9 = COL9;
    }

    public String getCOL10() {
        return COL10;
    }

    public void setCOL10(String COL10) {
        this.COL10 = COL10;
    }

    public String getCOL11() {
        return COL11;
    }

    public void setCOL11(String COL11) {
        this.COL11 = COL11;
    }

    public String getCOL12() {
        return COL12;
    }

    public void setCOL12(String COL12) {
        this.COL12 = COL12;
    }

    public String getCOL13() {
        return COL13;
    }

    public void setCOL13(String COL13) {
        this.COL13 = COL13;
    }


    @Override
    public int compareTo(DrList_POJO drListPOJO) {
        return this.getCOL14().toLowerCase(Locale.ENGLISH).compareTo(drListPOJO.getCOL14().toLowerCase(Locale.ENGLISH));
    }
}
