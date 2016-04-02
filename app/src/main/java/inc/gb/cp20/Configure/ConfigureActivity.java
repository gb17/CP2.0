package inc.gb.cp20.Configure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.trncic.library.DottedProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.ChangePwd.ChangePasswordAcitvity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Landing.LandingPage;
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
import inc.gb.cp20.Models.UPW;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.Connectivity;
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
public class ConfigureActivity extends Activity implements DownloadInterface {
    NumberProgressBar numberProgressBar;
    String UsernameString = "";
    String PasswordString = "";
    String clientidString = "";
    String instaneceId = "";
    String Repcode = "";
    String RoleCode = "";
    String Version = "";
    String ClientID = "";
    DBHandler dbHandler;
    SweetAlertDialog configAlertDialog;
    DottedProgressBar progressBar;
    private List<TBCVR> CvrList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_layout);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.progress_config);
        dbHandler = DBHandler.getInstance(this);
        progressBar = (DottedProgressBar) findViewById(R.id.progress);
        progressBar.startProgress();


        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                UsernameString = bundle.getString("USERNAME");
                PasswordString = bundle.getString("PASSWORD");
                clientidString = bundle.getString("CLIENTID");
            }
        } catch (Exception e) {

        }
        if (Connectivity.isConnected(this))
            callUpw(UsernameString, PasswordString, clientidString);
        else {
            Utility.showSweetAlertofFnish(this, CmsInter.AL_NETERROR, CmsInter.WARNING_TYPE);
        }

    }

    public void callUpw(String UserName, String Password, String ClientId) {

        UPW upw1 = new UPW();
        upw1.setCLIENTID(ClientId);
        upw1.setVALUE("");
        upw1.setVERSION(getResources().getString(R.string.version));
        upw1.setCONTROLNO("6");
        upw1.setOLDPWD(Password);
        upw1.setUSERNAME(UserName);


        //   final Call<UPW> call = api.CallUPW(upw1);
        RestClient.GitApiInterface service = RestClient.getClient();
        final Call<UPW> call = service.CallUPW(upw1);
        call.enqueue(new Callback<UPW>() {
            @Override
            public void onResponse(Response<UPW> response, Retrofit retrofit) {
                if (response.body() != null) {
                    numberProgressBar.setProgress(10);
                    UPW upw = response.body();
                    if (upw.getMSG().contains("Success")) {
                        SQLiteDatabase db = dbHandler.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("USERNAME", upw.getUSERNAME());
                        values.put("VAL", upw.getVALUE());
                        values.put("CLIENTID", upw.getCLIENTID());
                        values.put("CONTROLNO", upw.getCONTROLNO());
                        values.put("OLDPWD", upw.getOLDPWD());
                        values.put("VERSION", upw.getVERSION());
                        dbHandler.genricDelete(DBHandler.TBUPW);
                        db.insert(DBHandler.TBUPW, null, values);
                        db.close();
                        if (Connectivity.isConnected(ConfigureActivity.this))
                            CallCVR(false);
                        else {
                            Utility.showSweetAlertofFnish(ConfigureActivity.this, CmsInter.AL_NETERROR, CmsInter.WARNING_TYPE);
                        }


                    } else {
                        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfigureActivity.this, CmsInter.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(upw.getMSG())
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                    }
                                })
                                .show();
                    }

                } else {
                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfigureActivity.this, CmsInter.ERROR_TYPE);
                    sweetAlertDialog.setTitleText(CmsInter.AL_NETERROR)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .show();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfigureActivity.this, CmsInter.ERROR_TYPE);
                sweetAlertDialog.setTitleText(CmsInter.AL_NETERROR)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                            }
                        })
                        .show();

            }
        });


    }

    public void CallCVR(final boolean configflag) {
        numberProgressBar.setProgress(20);
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
                        numberProgressBar.setProgress(30);
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
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(ConfigureActivity.this);
                                alertbox.setMessage(datavalues[1]).setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent ChangePwdIntent = new Intent(ConfigureActivity.this, ChangePasswordAcitvity.class);
                                                ChangePwdIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ChangePwdIntent);
                                                dialog.dismiss();

                                            }
                                        });
                                alertbox.create();
                                alertbox.show();
                            } else {
                                if (configflag) {

                                    if (cvrvalues[0].equals(CmsInter.SUCESSS)) {

                                        Intent LandingIntent = new Intent(ConfigureActivity.this, LandingPage.class);
                                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        LandingIntent.putExtra("CALLSYNC", "1");
                                        startActivity(LandingIntent);
                                    } else {
                                        if (cvrvalues[0].equals(CmsInter.INVALID_INSTANCE)) {

                                            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfigureActivity.this, CmsInter.ERROR_TYPE);
                                            sweetAlertDialog.setTitleText(word.getMSG())
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dbHandler.deletealltable();
                                                            Utility.recursiveDelete(ConfigureActivity.this.getFilesDir());
                                                            Intent LandingIntent = new Intent(ConfigureActivity.this, ConfigureActivity.class);
                                                            LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(LandingIntent);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Utility.showSweetAlert(ConfigureActivity.this, word.getMSG(), CmsInter.ERROR_TYPE);
                                        }

                                    }


                                } else callFileDownload();
                            }
                        }

                    } else {
                        Utility.showSweetAlert(ConfigureActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Utility.showSweetAlert(ConfigureActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);

                }


            });
        }


    }

    public void callFileDownload() {

        final String[][] CVRdata = dbHandler.genericSelect("SELECT * FROM TBCVR", 4);
        numberProgressBar.setProgress(40);
        if (CVRdata != null) {
            String cvrsplit[] = CVRdata[0][2].split("\\^");
            if (cvrsplit[26].equals("J")) {
                RestClient.GitApiInterface service = RestClient.getClient();
                TAG tag = new TAG();
                tag.setClientid(ClientID);
                String url = CVRdata[0][3];//"https://pocworkerrole.blob.core.windows.net/ctdscript/CTDH0001201603111918.cpz";
                tag.setUrl(url);//  CVRdata[0][3] https://pocworkerrole.blob.core.windows.net/ctdscript/CTDH0001201603111918.cpz
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
                        Utility.showSweetAlert(ConfigureActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                    }
                });

            } else {
                new DemoAsync(ConfigureActivity.this, CVRdata[0][3], ConfigureActivity.this).execute(CVRdata[0][3]);
            }

        } else {
            Utility.showSweetAlert(ConfigureActivity.this, "Url Not Found", CmsInter.ERROR_TYPE);

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
        numberProgressBar.setProgress(80);
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
        numberProgressBar.setProgress(90);


        RestClient.GitApiInterface service = RestClient.getClient();

        final Call<ACKTAG> call = service.CallACK(acktag);
        call.enqueue(new Callback<ACKTAG>() {
            @Override
            public void onResponse(Response<ACKTAG> response, Retrofit retrofit) {
                numberProgressBar.setProgress(100);
                progressBar.stopProgress();

                configAlertDialog = new SweetAlertDialog(ConfigureActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("System Configured.")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                configAlertDialog.dismiss();
                                Intent LandingIntent = new Intent(ConfigureActivity.this, MainActivity.class);
                                LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(LandingIntent);
                            }
                        });
                configAlertDialog.show();
                new TBImgClass().execute();
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.showSweetAlert(ConfigureActivity.this, t.toString() + "ACKtagd", CmsInter.ERROR_TYPE);

            }
        });

    }

    @Override
    public void mainBody(List<IRCSFResponsePOJO> list) {

    }

    @Override
    public void onTaskCompleted(boolean flag) {
        if (flag) {
            numberProgressBar.setProgress(70);
            ChkAck();

        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Configuration Fail")
                    .show();


        }
    }

    class DemoAsync extends AsyncTask<String, Integer, Boolean> {

        String index = "0";
        private DownloadInterface listener;

        public DemoAsync(DownloadInterface listener, String Url, Context mContext) {
            this.listener = listener;
            numberProgressBar.setProgress(50);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Mai yaha hu", "on pre exceutr");

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
                        String directory = ConfigureActivity.this.getFilesDir().getAbsolutePath()
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
            numberProgressBar.setProgress(60);
            onTaskCompleted(bool);
            Log.d("Mai yaha hu", " onPostExecute");
        }
    }

    class TBImgClass extends AsyncTask<Void, Integer, Void> {
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
                            String directory = ConfigureActivity.this.getFilesDir().getAbsolutePath()
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
}
