package inc.gb.cp20.Models;

/**
 * Created by GB on 3/23/16.
 */
public class SearchData {

    String PageNamee;
    String Cat_Type;
    String Brand_code;
    String PageCode;
    String SubpageName;
    String Cat_Name;

    public String getCat_Name() {
        return Cat_Name;
    }

    public void setCat_Name(String cat_Name) {
        Cat_Name = cat_Name;
    }

    public String getSubpageName() {
        return SubpageName;
    }

    public void setSubpageName(String subpageName) {
        SubpageName = subpageName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    String ImagePath;

    public String getPageNamee() {
        return PageNamee;
    }

    public void setPageNamee(String pageNamee) {
        PageNamee = pageNamee;
    }

    public String getCat_Type() {
        return Cat_Type;
    }

    public void setCat_Type(String cat_Type) {
        Cat_Type = cat_Type;
    }

    public String getBrand_code() {
        return Brand_code;
    }

    public void setBrand_code(String brand_code) {
        Brand_code = brand_code;
    }

    public String getPageCode() {
        return PageCode;
    }

    public void setPageCode(String pageCode) {
        PageCode = pageCode;
    }
}
