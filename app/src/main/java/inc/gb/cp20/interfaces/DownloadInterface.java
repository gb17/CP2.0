package inc.gb.cp20.interfaces;


import java.util.List;

import inc.gb.cp20.Models.IRCSFResponsePOJO;

/**
 * Created by Shubham on 3/12/16.
 */
public interface DownloadInterface {

    void mainBody(List<IRCSFResponsePOJO> list);
     void onTaskCompleted(boolean b);
}
