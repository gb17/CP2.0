package inc.gb.cp20.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.CNTACKPOJO;
import inc.gb.cp20.Models.ContainerPOJO;
import inc.gb.cp20.Models.IRCSFPOJO;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.OutputPOJO;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.RestClient;
import inc.gb.cp20.interfaces.DownloadInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Shubham on 3/10/16.
 */
public class Sync {

    private Context context;
    String[] upwData = null;
    DBHandler dbHandler;


    public Sync(Context context) {
        this.context = context;

        dbHandler = DBHandler.getInstance(context);
        final String[][] psdf = dbHandler.genericSelect("SELECT * FROM TBUPW", 6);
        if (psdf != null) {
            upwData = psdf[0][3].split("\\^");
        }

    }

    public void prepareRequest(final int index) {
        List<ContainerPOJO> containerLs = new ArrayList<>();

        if (index == 1) {
            String[][] containerData = dbHandler.genericSelect("select a.*, '' pcode from TXN102 a where col11 = 0 \n" +
                    "and not exists \n" +
                    "( select 1 from TBPHTAG B WHERE b.col0 = a.col18\n" +
                    ")\n" +
                    "union select a.*,  b.col1 pcode from TXN102 a , tbphtag b\n" +
                    "where  a.col18 = b.col0\n" +
                    "AND A.COL11 = 0", 22);

            for (int i = 0; i < containerData.length; i++) {
                ContainerPOJO pojo = new ContainerPOJO(context.getResources().getString(R.string.clientid), upwData[3], containerData[i][0], containerData[i][1], containerData[i][2], containerData[i][3], containerData[i][4], containerData[i][5], containerData[i][6], containerData[i][7], containerData[i][8], containerData[i][14], containerData[i][13], containerData[i][10], containerData[i][9], containerData[i][16], containerData[i][15] + "^" + containerData[i][20], containerData[i][17], containerData[i][21], "", "", containerData[i][19]);
                containerLs.add(pojo);
            }
        } else if (index == 2) {
            String[][] updatedData = dbHandler.genericSelect( "Select * From TBDPS3 where COL12 = '0'", 16);

            for (int i = 0; i < updatedData.length; i++) {
                ContainerPOJO pojo = new ContainerPOJO(context.getResources().getString(R.string.clientid), upwData[3], upwData[9], upwData[10], null, updatedData[i][13], updatedData[i][10], "", updatedData[i][1], updatedData[i][5], updatedData[i][2], null, "-1", null, null, null, null, null, updatedData[i][15], "", "", updatedData[i][14]);
                containerLs.add(pojo);
            }

        }
        RestClient.GitApiInterface service = RestClient.getClient();
        Call<List<OutputPOJO>> lCall = service.uploadContainerData(containerLs);

        lCall.enqueue(new Callback<List<OutputPOJO>>() {
            @Override
            public void onResponse(Response<List<OutputPOJO>> response, Retrofit retrofit) {
                if (response.body() != null) {
                    DBHandler handler = DBHandler.getInstance(context);
                    SQLiteDatabase db = handler.getWritableDatabase();
                    List<OutputPOJO> strData = response.body();
                    for (OutputPOJO pojo : strData) {
                        if (pojo.getOUT().equals("1")) {
                            ContentValues cv = new ContentValues();
                            if (index == 1) {
                                cv.put("COL11", "1");
                                db.update("TXN102", cv, "COL19 = '" + pojo.getTXNID() + "'", null);
                            } else if (index == 2) {
                                cv.put("COL12", "1");
                                db.update("TBDPS3", cv, "COL14 = '" + pojo.getTXNID() + "'", null);
                            }
                        }
                    }
                }

                Log.d("Response", response + "");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Response", "error" + t);
            }
        });

    }


    public void downloadContentUrl(final DownloadInterface downloadInterface) {

        RestClient.GitApiInterface service = RestClient.getClient();
        IRCSFPOJO pojo = new IRCSFPOJO(context.getResources().getString(R.string.clientid), upwData[3], context.getResources().getString(R.string.version));

        Call<List<IRCSFResponsePOJO>> lCall = service.downloadContentUrl(pojo);

        lCall.enqueue(new Callback<List<IRCSFResponsePOJO>>() {
            @Override
            public void onResponse(Response<List<IRCSFResponsePOJO>> response, Retrofit retrofit) {
                downloadInterface.mainBody(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                downloadInterface.mainBody(null);
            }
        });
    }

    public void contentAcknowledge(String entryNumber) {
        CNTACKPOJO pojo = new CNTACKPOJO(context.getResources().getString(R.string.clientid), upwData[3], entryNumber);
        RestClient.GitApiInterface service = RestClient.getClient();
        Call<String> lCall = service.contentAcknowledge(pojo);

        lCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d("Response", response.toString());
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

}
