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
import inc.gb.cp20.ChnagePwd.ChangePasswordAcitvity;
import inc.gb.cp20.Configure.MainActivity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.ACKTAG;
import inc.gb.cp20.Models.CVR;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.ReqCVR;
import inc.gb.cp20.Models.TAG;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.Models.TBCVR;
import inc.gb.cp20.Models.TBDBL;
import inc.gb.cp20.Models.TBDCP;
import inc.gb.cp20.Models.TBDMENU;
import inc.gb.cp20.Models.TBDPG;
import inc.gb.cp20.Models.TBDPS;
import inc.gb.cp20.Models.TBDSP;
import inc.gb.cp20.Models.TBDTH;
import inc.gb.cp20.Models.TBPARTY;
import inc.gb.cp20.Models.TBTBC;
import inc.gb.cp20.Models.TablesConfig;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.RestClient;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.interfaces.DownloadInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

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
        getprogressDialog("Please Wait");
        final String[][] psdf = dbHandler.genericSelect("SELECT * FROM TBUPW", 6);
        if (psdf != null) {
            final String datavalues[] = psdf[0][3].split("\\^");
            instaneceId = datavalues[4];
            Repcode = datavalues[3];
            RoleCode = datavalues[5].substring(0, 1);
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
                            if (datavalues[0].equals(CmsInter.Change_PWD)) {
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
                                    TBCVR word = CvrList.get(0);
                                    if (word.getMSG().contains("Success")) {
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
//                                        Intent LandingIntent = new Intent(mContext, LandingPage.class);
//                                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        LandingIntent.putExtra("CALLSYNC", "1");
//                                        mContext.startActivity(LandingIntent);
                                    } else {
                                        if (word.getMSG().contains("Invalid Instance")) {

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
                        Utility.showSweetAlert(mContext, "OOps!", CmsInter.ERROR_TYPE);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Utility.showSweetAlert(mContext, t.toString(), CmsInter.ERROR_TYPE);
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
                tag.setClientid(mContext.getResources().getString(R.string.clientid));
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
                                dataInsert(response);
                                ChkAck();
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

    public void dataInsert(Response<TablesConfig> response) {
        List<TBDCP> tbl = response.body().getTBDCP();
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        for (int i = 0; i < tbl.size(); i++) {
            TBDCP tbdcp = tbl.get(i);
            cupboard().withDatabase(db).put(tbdcp);
        }

        List<TBDTH> tbl2 = response.body().getTBDTH();

        for (int i = 0; i < tbl2.size(); i++) {
            TBDTH tbdth = tbl2.get(i);
            cupboard().withDatabase(db).put(tbdth);
        }

        List<TBDBL>
                tbl3 = response.body().getTBDBL();

        for (int i = 0; i < tbl3.size(); i++) {
            TBDBL tbdbl = tbl3.get(i);
            cupboard().withDatabase(db).put(tbdbl);
        }

        List<TBDSP>
                tbl4 = response.body().getTBDSP();

        for (int i = 0; i < tbl4.size(); i++) {
            TBDSP tbdsp = tbl4.get(i);
            cupboard().withDatabase(db).put(tbdsp);
        }

        List<TBTBC>
                tbl5 = response.body().getTBTBC();

        for (int i = 0; i < tbl5.size(); i++) {
            TBTBC tbtbc = tbl5.get(i);
            cupboard().withDatabase(db).put(tbtbc);
        }


        List<TBDPG>
                tbl6 = response.body().getTBDPG();

        for (int i = 0; i < tbl6.size(); i++) {
            TBDPG tbdpg = tbl6.get(i);
            cupboard().withDatabase(db).put(tbdpg);
        }
        List<TBPARTY>
                tbl7 = response.body().getTBPARTY();

        for (int i = 0; i < tbl7.size(); i++) {
            TBPARTY tbparty = tbl7.get(i);
            cupboard().withDatabase(db).put(tbparty);
        }

        List<TBDPS>
                tbl8 = response.body().getTBDPS();

        for (int i = 0; i < tbl8.size(); i++) {
            TBDPS tbdps = tbl8.get(i);
            cupboard().withDatabase(db).put(tbdps);
        }

        List<TBBRAND>
                tbl9 = response.body().getTBBRAND();

        for (int i = 0; i < tbl9.size(); i++) {
            TBBRAND tbbrand = tbl9.get(i);
            cupboard().withDatabase(db).put(tbbrand);
        }

        List<TBDMENU>
                tbl10 = response.body().getTBDMENU();

        for (int i = 0; i < tbl10.size(); i++) {
            TBDMENU tbdmenu = tbl10.get(i);
            cupboard().withDatabase(db).put(tbdmenu);
        }


    }

    public void ChkAck() {

        dbHandler.check_control_table();
        String suc = "";
        String fail = "";
        String s[][] = dbHandler.genericSelect("Select * from TBTBC where MOB_COUNT !='0' ", 2);
        if (s != null)
            for (int i = 0; i < s.length; i++) {
                fail = fail + s[i][1] + ",";
            }
        String s2[][] = dbHandler.genericSelect("Select * from TBTBC where MOB_COUNT ='0' ", 2);
        if (s2 != null) {
            for (int i = 0; i < s2.length; i++) {
                suc = suc + s2[i][1] + ",";
            }
        }

        ACKtagd(fail, suc);


    }

    public void ACKtagd(String fail, String Success) {
        ACKTAG acktag = new ACKTAG();
        acktag.setREPCODE(Repcode);
        acktag.setCLIENTID(ClientID);
        acktag.setFAILTAG(fail);
        acktag.setSUCCESSTAG(Success);


        RestClient.GitApiInterface service = RestClient.getClient();

        final Call<ACKTAG> call = service.CallACK(acktag);
        call.enqueue(new Callback<ACKTAG>() {
            @Override
            public void onResponse(Response<ACKTAG> response, Retrofit retrofit) {

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

            @Override
            public void onFailure(Throwable t) {
                Utility.showSweetAlert(mContext, t.toString(), CmsInter.ERROR_TYPE);
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

            ChkAck();

        } else {
            dialog.dismiss();

//            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("Configuration Fail")
//                    .show();


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
            Log.d("Mai yaha hu", "on pre exceutr");

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean aBoolean = false;

            String Urls[] = strings[0].split(",");

            for (int i = 0; i < Urls.length; i++) {
                String msg = Utility.downloadZipFile(Urls[0]);
                if (!msg.startsWith("fail")) {
                    try {
                        File zipfile = new File(msg);
                        String directory = mContext.getFilesDir().getAbsolutePath()
                                + "/";
                        Utility.unZipFile(zipfile, directory);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            aBoolean = dbHandler.executscript();


            return aBoolean;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            onTaskCompleted(bool);
            Log.d("Mai yaha hu", " onPostExecute");
        }
    }
}