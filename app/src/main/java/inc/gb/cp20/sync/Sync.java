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
import inc.gb.cp20.Models.SyncDetailingAckPOJO;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.RestClient;
import inc.gb.cp20.Util.Utility;
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
    String ClientID = "";


    public Sync(Context context) {
        this.context = context;
        dbHandler = DBHandler.getInstance(context);
        checkConfigOrNot();
        final String[][] psdf = dbHandler.genericSelect("SELECT * FROM TBUPW", 6);
        if (psdf != null) {
            upwData = psdf[0][3].split("\\^");
        }
    }

    public void prepareRequest(final int index) {

        String batchNumber = Utility.getUniqueNo() + "^" + index;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("COL0", batchNumber);
        cv.put("COL1", "P");
        db.insert("TBBNO", null, cv);

        List<ContainerPOJO> containerLs = new ArrayList<>();
        String containerData[][] = null;

        try {


        if (index == 1) {
            containerData = dbHandler.genericSelect("select a.*, ' ' pcode , ' ' pname , ' ' patch, ' ' spec, ' ' class, ' ' hqcode  from TXN102 a where col11 = 0 and not exists ( select 1 from TBPHTAG B WHERE b.col0 = a.col18 ) union \n" +
                    "select a.*,  b.col1 pcode, b.col2 pname, b.col3 patch, b.col4 spec, b.col5 class, b.col6 hqcode  from TXN102 a , tbphtag b where  a.col18 = b.col0 AND A.COL11 = 0", 28);

            if (containerData != null)
                for (int i = 0; i < containerData.length; i++) {
                    ContainerPOJO pojo = new ContainerPOJO(ClientID, upwData[3], containerData[i][0], containerData[i][1],
                            containerData[i][2], containerData[i][3], containerData[i][4], containerData[i][5], containerData[i][6], containerData[i][7],
                            containerData[i][8], containerData[i][14], containerData[i][13], containerData[i][10], containerData[i][9], containerData[i][16], containerData[i][15] + "^" + containerData[i][20],
                            containerData[i][17], containerData[i][22], "", "", containerData[i][19], containerData[i][21], batchNumber, containerData[i][23], containerData[i][24], containerData[i][25], containerData[i][26], containerData[i][27]);
                    containerLs.add(pojo);
                }
        } else if (index == 2) {
            containerData = dbHandler.genericSelect("Select * From TBDPS3 where COL12 = '0'", 16);

            if (containerData != null)
                for (int i = 0; i < containerData.length; i++) {
                    ContainerPOJO pojo = new ContainerPOJO(ClientID, upwData[3], upwData[9], upwData[10], null, containerData[i][13], containerData[i][10], "", containerData[i][1], containerData[i][5], containerData[i][2], null, "-1", null, null, null, null, null, containerData[i][15], "", "", containerData[i][14], "", batchNumber, "", "", "", "", "");
                    containerLs.add(pojo);
                }
        }

        } catch (Exception e) {

        }
        if (containerData != null) {
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
                                              if (index == 1) {
                                                  String whereClause = "COL19=?";
                                                  String[] whereArgs = new String[]{pojo.getTXNID()};
                                                  db.delete("TXN102", whereClause, whereArgs);
                                              } else if (index == 2) {
                                                  String whereClause = "COL14=?";
                                                  String[] whereArgs = new String[]{pojo.getTXNID()};
                                                  db.delete("TBDPS3", whereClause, whereArgs);
                                              }
                                          }
                                      }
                                      if (index == 1)
                                          db.execSQL("delete from TBPHTAG  where not exists(Select 1 from TXN102 b where TBPHTAG.COL0 = b.COL18)");
                                      acknowledgeBatchCode();
                                  }
                                  Log.d("Response", response + "");
                              }

                              @Override
                              public void onFailure(Throwable t) {
                                  Log.d("Response", "error" + t);
                              }
                          }
            );
        }
    }

    public void acknowledgeBatchCode() {
        String[][] batchData = dbHandler.genericSelect("Select * FROM TBBNO", 2);
        if (batchData != null) {
            List<SyncDetailingAckPOJO> containerLs = new ArrayList<>();
            for (int i = 0; i < batchData.length; i++) {
                SyncDetailingAckPOJO pojo = new SyncDetailingAckPOJO(ClientID, upwData[3], batchData[i][0], "");
                containerLs.add(pojo);

                RestClient.GitApiInterface service = RestClient.getClient();
                Call<List<SyncDetailingAckPOJO>> lCall = service.CallSyncDetailingAcknowledge(containerLs);

                lCall.enqueue(new Callback<List<SyncDetailingAckPOJO>>() {
                    @Override
                    public void onResponse(Response<List<SyncDetailingAckPOJO>> response, Retrofit retrofit) {
                        if (response.body() != null) {
                             DBHandler handler = DBHandler.getInstance(context);
                            SQLiteDatabase db = handler.getWritableDatabase();
                            List<SyncDetailingAckPOJO> strData = response.body();
                            for (SyncDetailingAckPOJO pojo : strData) {
                                if (pojo.getOUT().equals("1")) {
                                    String whereClause = "COL0=?";
                                    String[] whereArgs = new String[]{pojo.getBATCHCODE()};
                                    db.delete("TBBNO", whereClause, whereArgs);
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
        }
    }


    public void downloadContentUrl(final DownloadInterface downloadInterface, int mode, String CATEGORYTYPE, String CATEGORYCODE) {

        RestClient.GitApiInterface service = RestClient.getClient();
        IRCSFPOJO pojo = null;
        if (mode == 1)
            pojo = new IRCSFPOJO(ClientID, upwData[3], context.getResources().getString(R.string.version), CATEGORYTYPE, CATEGORYCODE);
        else if (mode == 2)
            pojo = new IRCSFPOJO(ClientID, upwData[3], context.getResources().getString(R.string.version), CATEGORYTYPE, CATEGORYCODE);


        Call<List<IRCSFResponsePOJO>> lCall = service.downloadContentUrl(pojo);

        lCall.enqueue(new Callback<List<IRCSFResponsePOJO>>() {
            @Override
            public void onResponse(Response<List<IRCSFResponsePOJO>> response, Retrofit retrofit) {
                if (response.body().size() != 0)
                    downloadInterface.mainBody(response.body());
                else {
                    downloadInterface.mainBody(null);
                    Utility.showSweetAlert(context, "No content to download", CmsInter.NORMAL_TYPE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.showSweetAlert(context, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                downloadInterface.mainBody(null);
            }
        });
    }

    public void contentAcknowledge(String entryNumber) {
        CNTACKPOJO pojo = new CNTACKPOJO(ClientID, upwData[3], entryNumber);
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

    private boolean checkConfigOrNot() {
        boolean mission;
        dbHandler = DBHandler.getInstance(context);

        String data[][] = dbHandler.genericSelect("*", DBHandler.TBUPW,
                "", "", "", 6);
        if (data != null) {


            //data[0][2];
            String datavalues[] = data[0][3].split("\\^");
//                instaneceId = datavalues[4];
//                Repcode = datavalues[3];
//                RoleCode = datavalues[5].substring(0, 1);
//                Version = data[0][1];
            ClientID = data[0][4];
            mission = true;
        } else {

            mission = false;
        }


        return mission;

    }
}
