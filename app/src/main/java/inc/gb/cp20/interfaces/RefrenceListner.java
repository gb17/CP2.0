package inc.gb.cp20.interfaces;

import inc.gb.cp20.Models.RefrenceContent;
import inc.gb.cp20.Models.SearchData;

/**
 * Created by GB on 4/16/16.
 */
public interface RefrenceListner {

    void onRefrenceClick(RefrenceContent brandList);

    void onRefrenceClickFromSrch(SearchData brandList);
}
