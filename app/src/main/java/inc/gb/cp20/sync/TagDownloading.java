package inc.gb.cp20.sync;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.ChangePwd.ChangePasswordAcitvity;
import inc.gb.cp20.Configure.MainActivity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.ACKTAG;
import inc.gb.cp20.Models.CVR;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.ReqCVR;
import inc.gb.cp20.Models.TAG;
import inc.gb.cp20.Models.TBCVR;
import inc.gb.cp20.Models.TablesConfig;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.RestClient;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.interfaces.DownloadInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by GB on 3/19/16.
 */
public class TagDownloading implements DownloadInterface {


    String instaneceId = "";
    String Repcode = "";
    String RoleCode = "";
    String Version = "";
    String ClientID = "";

    SweetAlertDialog dialog;
    DBHandler dbHandler;
    private List<TBCVR> CvrList = new ArrayList<>();

    public boolean CONFIG_FLAG = false;
    SweetAlertDialog configAlertDialog = null;
    final int finalacknowledgeTag = 96;
    Context mContext;

    public TagDownloading(Context mContext) {
        this.mContext = mContext;
        dbHandler = DBHandler.getInstance(mContext);
        CallCVR(true);
    }

    public void getprogressDialog(String msg) {
        dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void CallCVR(final boolean configflag) {
        getprogressDialog("Please Wait...");
        final String[][] psdf = dbHandler.genericSelect("SELECT * FROM TBUPW", 6);
        if (psdf != null) {
            final String datavalues[] = psdf[0][3].split("\\^");
            instaneceId = datavalues[4];
            Repcode = datavalues[3];
            RoleCode = datavalues[6];
            Version = psdf[0][1];
            ClientID = psdf[0][4];
            //valueArr[5];
            ReqCVR reqCVR = new ReqCVR();
            reqCVR.setVERSION(Version);
            reqCVR.setCLIENTID(ClientID);
            reqCVR.setAPPVERSION(CmsInter.AppVerison);
            reqCVR.setFlag(CmsInter.FLAG);
            reqCVR.setINSTANCEID(instaneceId);
            reqCVR.setROLECODE(RoleCode);
            reqCVR.setREPCODE(Repcode);


            RestClient.GitApiInterface service = RestClient.getClient();
            final Call<CVR> cvr = service.CallCVR(reqCVR);
            cvr.enqueue(new Callback<CVR>() {
                @Override
                public void onResponse(Response<CVR> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        CvrList = response.body().getTBCVR();
                        for (int i = 0; i < CvrList.size(); i++) {
                            TBCVR word = CvrList.get(i);
                            SQLiteDatabase db = dbHandler.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("COL0", word.getCOL0());
                            values.put("COL1", word.getCOL1());
                            values.put("COL2", word.getCOL2());
                            values.put("COL3", word.getCOL3());
                            dbHandler.genricDelete(DBHandler.TBCVR);
                            db.insert(DBHandler.TBCVR, null, values);
                            db.close();
                        }

                        if (CvrList.size() != 0) {
                            TBCVR word = CvrList.get(0);
                            String[] cvrvalues = word.getCOL2().split("\\^");
                            if (cvrvalues[0].equals(CmsInter.Change_PWD)) {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                                alertbox.setMessage(datavalues[1]).setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent ChangePwdIntent = new Intent(mContext, ChangePasswordAcitvity.class);
                                                ChangePwdIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                mContext.startActivity(ChangePwdIntent);
                                                dialog.dismiss();

                                            }
                                        });
                                alertbox.create();
                                alertbox.show();
                            } else {
                                if (configflag) {
                                    if (cvrvalues[0].equals(CmsInter.SUCESSS)) {
                                        if (!word.getCOL3().equals("")) {
                                            callFileDownload();
                                        } else {
                                            dialog.dismiss();
                                            final SweetAlertDialog configAlertDialogv = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                                            configAlertDialogv.setConfirmText("Ok")
                                                    .setTitleText(CmsInter.AL_SYNC_SUC)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {

                                                            configAlertDialogv.dismiss();
                                                        }
                                                    });
                                            configAlertDialogv.show();
                                        }

                                    } else {
                                        if (cvrvalues[0].equals(CmsInter.INVALID_INSTANCE)) {

                                            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, CmsInter.ERROR_TYPE);
                                            sweetAlertDialog.setTitleText(word.getMSG())
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dbHandler.deletealltable();
                                                            Intent LandingIntent = new Intent(mContext, MainActivity.class);
                                                            LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            mContext.startActivity(LandingIntent);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Utility.showSweetAlert(mContext, word.getMSG(), CmsInter.ERROR_TYPE);
                                        }

                                    }


                                } else callFileDownload();
                            }
                        }

                    } else {
                        Utility.showSweetAlert(mContext, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Utility.showSweetAlert(mContext, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                    dialog.dismiss();
                }


            });
        }


    }

    public void callFileDownload() {

        final String[][] CVRdata = dbHandler.genericSelect("SELECT * FROM TBCVR", 4);
        if (CVRdata != null) {
            String cvrsplit[] = CVRdata[0][2].split("\\^");
            if (cvrsplit[26].equals("J")) {
                RestClient.GitApiInterface service = RestClient.getClient();
                TAG tag = new TAG();
                tag.setClientid(ClientID);
                String url = CVRdata[0][3];
                tag.setUrl(url);
                tag.setRepcode(Repcode);

                final Call<TablesConfig> TagReq = service.CallTagDownload(tag);
                TagReq.enqueue(new Callback<TablesConfig>() {
                    @Override
                    public void onResponse(final Response<TablesConfig> response, Retrofit retrofit) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Utility.dataInsert(response, dbHandler);
                                acknowledgeTag(Utility.ChkAcknowledgeTag(Repcode, ClientID, dbHandler), finalacknowledgeTag);
                            }
                        }).start();


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("TAG", t.toString());
                    }
                });

            } else {
                new DemoAsync(this, CVRdata[0][3], mContext).execute(CVRdata[0][3]);
            }

        } else {
            Utility.showSweetAlert(mContext, "Url Not Found", CmsInter.ERROR_TYPE);
            dialog.dismiss();
        }
    }


    public void acknowledgeTag(ACKTAG acktag, final int mode) {

        RestClient.GitApiInterface service = RestClient.getClient();

        final Call<ACKTAG> call = service.CallACK(acktag);
        call.enqueue(new Callback<ACKTAG>() {
            @Override
            public void onResponse(Response<ACKTAG> response, Retrofit retrofit) {
                if (mode == finalacknowledgeTag) {
                    configAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(CmsInter.AL_SYNC_SUC)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    configAlertDialog.dismiss();

                                }
                            });
                    configAlertDialog.show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.showSweetAlert(mContext, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void mainBody(List<IRCSFResponsePOJO> list) {

    }

    @Override
    public void onTaskCompleted(boolean flag) {

        if (flag) {
            acknowledgeTag(Utility.ChkAcknowledgeTag(Repcode, ClientID, dbHandler), finalacknowledgeTag);
        } else {
            dialog.dismiss();


        }
    }

    class DemoAsync extends AsyncTask<String, Integer, Boolean> {

        String index = "0";
        private DownloadInterface listener;

        public DemoAsync(DownloadInterface listener, String Url, Context mContext) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new DownloadImageTask(dbHandler, mContext);

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean aBoolean = false;

            String Urls[] = strings[0].split("\\,");

            for (int i = 0; i < Urls.length; i++) {
                String msg = Utility.downloadZipFile(Urls[i]);
                if (!msg.startsWith("fail")) {
                    try {
                        File zipfile = new File(msg);
                        String directory = mContext.getFilesDir().getAbsolutePath()
                                + "/";
                        Utility.unZipFile(zipfile, directory);
                        aBoolean = dbHandler.executscript();
                        if (Urls.length == i - 1) {
                        } else
                            acknowledgeTag(Utility.ChkAcknowledgeTag(Repcode, ClientID, dbHandler), i);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            return aBoolean;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            onTaskCompleted(bool);

        }
    }
}