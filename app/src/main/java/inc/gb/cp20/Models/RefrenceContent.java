package inc.gb.cp20.Models;

/**
 * Created by GB on 3/22/16.
 */
public class RefrenceContent {

    String RefrenceCode;
    String RefrenceName;
    String category_code;
    String category_name;

    public String getRefrenceCode() {
        return RefrenceCode;
    }

    public void setRefrenceCode(String refrenceCode) {
        RefrenceCode = refrenceCode;
    }

    public String getRefrenceName() {
        return RefrenceName;
    }

    public void setRefrenceName(String refrenceName) {
        RefrenceName = refrenceName;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
