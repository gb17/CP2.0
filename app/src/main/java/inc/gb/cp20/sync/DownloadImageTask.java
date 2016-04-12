package inc.gb.cp20.sync;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Util.Utility;

/**
 * Created by GB on 4/12/16.
 */
public class DownloadImageTask extends AsyncTask<Void, Integer, Void> {
    DBHandler dbHandler;
    Context context;

    public DownloadImageTask(DBHandler dbHandler, Context context) {
        this.dbHandler = dbHandler;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String[][] tbImg = dbHandler.genericSelect("Select COL1 from TBIMG WHERE COL2 = 'Y'", 1);
        if (tbImg != null) {

            for (int i = 0; i < tbImg.length; i++) {
                String url = tbImg[i][0];
                String msg = Utility.downloadZipFile(url);
                if (!msg.startsWith("fail")) {
                    try {
                        File zipfile = new File(msg);
                        String directory = context.getFilesDir().getAbsolutePath()
                                + "/";
                        String str = Utility.unZipFile(zipfile, directory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dbHandler.ExecuteQuery("UPDATE TBIMG SET COL2 = 'N' where COL1 = '" + url + "'");

                }
            }
        }
        return null;
    }

}
