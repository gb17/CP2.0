package inc.gb.cp20.Models;

/**
 * Created by GB on 3/22/16.
 */
public class ContentPage {

//    bundle.putString("customer_id",objDrListPOJO.getCOL0());
//    bundle.putString("customer_name",objDrListPOJO.getCOL1());
//    bundle.putString("category_code",objDrListPOJO.getCOL5());
//    bundle.putString("category_name",objDrListPOJO.getCOL10());
//    bundle.putString("thumbnail_category",objDrListPOJO.getCOL16());
//    bundle.putString("index","3");

    String pageCode;
    String pageName;
    String category_code;
    String category_name;
    String newtag;

    public String getNewtag() {
        return newtag;
    }

    public void setNewtag(String newtag) {
        this.newtag = newtag;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    String imagepath;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }


}
