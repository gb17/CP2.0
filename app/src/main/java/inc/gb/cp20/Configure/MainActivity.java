package inc.gb.cp20.Configure;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.ChangePwd.ChangePasswordAcitvity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Landing.LandingPage;
import inc.gb.cp20.Models.ACKTAG;
import inc.gb.cp20.Models.CVR;
import inc.gb.cp20.Models.ChangePassword;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.ReqCVR;
import inc.gb.cp20.Models.TAG;
import inc.gb.cp20.Models.TBCVR;
import inc.gb.cp20.Models.TablesConfig;
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

public class MainActivity extends AppCompatActivity implements DownloadInterface {


    EditText UsereditText;
    EditText PasswordeditText;
    EditText ClientideditText;


    String UsernameString = "";
    String PasswordString = "";
    String ClientidString = "";

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
    TextView forgotpwd;
    TextView loginButton;
    String msg = "";
    TextView displaytext, display_text_network;

    final int finalacknowledgeTag = 96;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        UsereditText = (EditText) findViewById(R.id.userid);
        PasswordeditText = (EditText) findViewById(R.id.password);
        ClientideditText = (EditText) findViewById(R.id.client_id);
        loginButton = (TextView) findViewById(R.id.loginclick);
        forgotpwd = (TextView) findViewById(R.id.forgotpwd);
        displaytext = (TextView) findViewById(R.id.display_text);
        display_text_network = (TextView) findViewById(R.id.display_text_network);

        //To login Please enter your password

        CONFIG_FLAG = checkConfigOrNot();
        if (!CONFIG_FLAG) {
            displaytext.setText("Please enter the client id,username and password given to\nyou to configure the system.");
            display_text_network.setVisibility(View.VISIBLE);
            loginButton.setText("CONFIGURE");
            forgotpwd.setVisibility(View.GONE);
            dbHandler = DBHandler.getInstance(this);
            dbHandler.createTables();
        } else if (CONFIG_FLAG) {
            displaytext.setText("To login please enter your password");
            display_text_network.setVisibility(View.GONE);
            ClientideditText.setVisibility(View.GONE);
            UsereditText.setText(UsernameString);
            ClientideditText.setText(ClientID);
            UsereditText.setEnabled(false);
            loginButton.setText("LOGIN");
            forgotpwd.setVisibility(View.VISIBLE);
        }


        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForgotPassword();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (UsereditText.getText().toString().equalsIgnoreCase("")
                        || PasswordeditText.getText().toString().equals("") || ClientideditText.getText().toString().equals("")) {
                    if (CONFIG_FLAG)
                        Utility.showSweetAlert(MainActivity.this, "Password is mandatory.", CmsInter.ERROR_TYPE);
                    else
                        Utility.showSweetAlert(MainActivity.this, "Client ID/Username/Password is mandatory.", CmsInter.ERROR_TYPE);

                } else {
                    if (!CONFIG_FLAG) {
                        Intent LandingIntent = new Intent(MainActivity.this, ConfigureActivity.class);
                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        LandingIntent.putExtra("USERNAME", UsereditText.getText().toString());
                        LandingIntent.putExtra("PASSWORD", PasswordeditText.getText().toString());
                        LandingIntent.putExtra("CLIENTID", ClientideditText.getText().toString());
                        startActivity(LandingIntent);

                    } else if (CONFIG_FLAG) {
                        if (PasswordString.equals(PasswordeditText.getText().toString())) {

                            if (Connectivity.isConnected(MainActivity.this)) {
                                getprogressDialog("Please Wait..");
                                CallCVR(CONFIG_FLAG);
                            } else {
                                Intent LandingIntent = new Intent(MainActivity.this, LandingPage.class);
                                LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                LandingIntent.putExtra("CALLSYNC", "1");
                                startActivity(LandingIntent);
                            }

                            PasswordeditText.setText("");
                        } else {
                            Utility.showSweetAlert(MainActivity.this, "Invalid Password.", CmsInter.ERROR_TYPE);
                        }
                    }

                }
            }
        });

        String url1 = "file://" + getFilesDir().getAbsolutePath() + "/welcome/welcome.htm";
        File welcomeFile = new File(url1);
        if (!welcomeFile.exists())
            copyAsset("welcome");

        String url2 = "file://" + getFilesDir().getAbsolutePath() + "/thank/thank.htm";
        File thankFile = new File(url2);
        if (!thankFile.exists())
            copyAsset("thank");
    }

    public void getprogressDialog(String msg) {
        dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean checkConfigOrNot() {
        boolean mission;
        dbHandler = DBHandler.getInstance(MainActivity.this);
        try {
            String data[][] = dbHandler.genericSelect("*", DBHandler.TBUPW,
                    "", "", "", 6);
            if (data != null) {


                //data[0][2];
                String datavalues[] = data[0][3].split("\\^");
                instaneceId = datavalues[4];
                Repcode = datavalues[3];
                RoleCode = datavalues[6];
                Version = data[0][1];
                ClientID = data[0][4];
                mission = true;
            } else {

                mission = false;
            }

            String dataCVR[][] = dbHandler.genericSelect("*", DBHandler.TBCVR,
                    "", "", "", 4);
            if (dataCVR != null) {

                String dataCVRSplitCol2[] = dataCVR[0][2].split("\\^");
                UsernameString = dataCVRSplitCol2[9];
                PasswordString = dataCVRSplitCol2[3];
                mission = true;
            } else {

                mission = false;
            }

        } catch (Exception e) {
            mission = false;
        }
        return mission;

    }


    public void CallCVR(final boolean configflag) {

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
                        TBCVR word = CvrList.get(0);
                        String[] cvrvalues = word.getCOL2().split("\\^");


                        if (CvrList.size() != 0) {
                            if (cvrvalues[0].equals(CmsInter.Change_PWD)) {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
                                alertbox.setMessage(datavalues[1]).setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent ChangePwdIntent = new Intent(MainActivity.this, ChangePasswordAcitvity.class);
                                                ChangePwdIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ChangePwdIntent);
                                                dialog.dismiss();

                                            }
                                        });
                                alertbox.create();
                                alertbox.show();
                            } else {
                                if (configflag) {


                                    if (word.getMSG().contains("Success")) {
                                        backupDatabase();
                                        if (!word.getCOL3().equals("")) {
                                            callFileDownload();
                                        } else {
                                            Intent LandingIntent = new Intent(MainActivity.this, LandingPage.class);
                                            LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            LandingIntent.putExtra("CALLSYNC", "1");
                                            startActivity(LandingIntent);
                                        }

                                    } else {
                                        if (cvrvalues[0].equals(CmsInter.INVALID_INSTANCE)) {

                                            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, CmsInter.ERROR_TYPE);
                                            sweetAlertDialog.setTitleText(word.getMSG())
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dbHandler.deletealltable();
                                                            Utility.recursiveDelete(MainActivity.this.getFilesDir());
                                                            Intent LandingIntent = new Intent(MainActivity.this, MainActivity.class);
                                                            LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(LandingIntent);
                                                        }
                                                    })
                                                    .show();
                                        } else if (cvrvalues[0].equals(CmsInter.Change_PWD)) {

                                            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, CmsInter.ERROR_TYPE);
                                            sweetAlertDialog.setTitleText(word.getMSG())
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            Intent ChangePwdIntent = new Intent(MainActivity.this, ChangePasswordAcitvity.class);
                                                            ChangePwdIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(ChangePwdIntent);
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            Utility.showSweetAlert(MainActivity.this, word.getMSG(), CmsInter.ERROR_TYPE);
                                        }

                                    }
                                } else callFileDownload();
                            }
                        }

                    } else {
                        Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                        Intent LandingIntent = new Intent(MainActivity.this, LandingPage.class);
                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        LandingIntent.putExtra("CALLSYNC", "1");
                        startActivity(LandingIntent);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Intent LandingIntent = new Intent(MainActivity.this, LandingPage.class);
                    LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    LandingIntent.putExtra("CALLSYNC", "1");
                    startActivity(LandingIntent);
                    Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
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
                                Utility.dataInsert(response, dbHandler);
                                acknowledgeTag(Utility.ChkAcknowledgeTag(Repcode, ClientID, dbHandler), finalacknowledgeTag);

                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                    }
                });

            } else {
                new DemoAsync(MainActivity.this, CVRdata[0][3], MainActivity.this).execute(CVRdata[0][3]);
            }

        } else {
            Utility.showSweetAlert(MainActivity.this, "Url Not Found", CmsInter.ERROR_TYPE);
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
                    if (!CONFIG_FLAG) {
                        configAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("System Configured.")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        configAlertDialog.dismiss();
                                        Intent LandingIntent = new Intent(MainActivity.this, MainActivity.class);
                                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(LandingIntent);
                                    }
                                });
                        configAlertDialog.show();
                        dialog.dismiss();
                    } else {

                        dialog.dismiss();
                        Intent LandingIntent = new Intent(MainActivity.this, LandingPage.class);
                        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        LandingIntent.putExtra("CALLSYNC", "1");
                        startActivity(LandingIntent);


                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                dialog.dismiss();
            }
        });

    }

    public void CallForgotPassword() {
        getprogressDialog("Requesting...");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setCLIENTID(ClientID);
        changePassword.setREPCODE(Repcode);
        changePassword.setBU("2");
        changePassword.setNPWD("");
        changePassword.setOPWD("");

        RestClient.GitApiInterface service = RestClient.getClient();

        Call<ChangePassword> forgotpassowrd = service.CallForgotPassword(changePassword);
        forgotpassowrd.enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Response<ChangePassword> response, Retrofit retrofit) {
                if (response != null) {
                    final ChangePassword chg = response.body();
                    if (chg.getOUT().equals("1")) {
                        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, CmsInter.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText(chg.getMSG())
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismiss();
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        dialog.dismiss();
                        Utility.showSweetAlert(MainActivity.this, chg.getMSG(), CmsInter.ERROR_TYPE);
                    }
                } else {
                    dialog.dismiss();
                    Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                Utility.showSweetAlert(MainActivity.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
            }
        });

    }


    public final boolean backupDatabase() {
        File from = MainActivity.this.getDatabasePath("CWEPV8");
        File to = this.getBackupDatabaseFile();
        try {
            FileUtils.copyFile(from, to);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Oho", "Error backuping up database: " + e.getMessage(), e);
        }
        return false;
    }

    public File getBackupDatabaseFile() {
        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/backupCW");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, "THE" + "_" + "CWEPV8");
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


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
                        String directory = MainActivity.this.getFilesDir().getAbsolutePath()
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

    private void copyAsset(String path) {
        AssetManager manager = MainActivity.this.getAssets();
        try {
            String[] contents = manager.list(path);

            // The documentation suggests that list throws an IOException, but
            // doesn't
            // say under what conditions. It'd be nice if it did so when the
            // path was
            // to a file. That doesn't appear to be the case. If the returned
            // array is
            // null or has 0 length, we assume the path is to a file. This means
            // empty
            // directories will get turned into files.
            if (contents == null || contents.length == 0)
                throw new IOException();

            // Make the directory.
            File dir = new File(MainActivity.this.getFilesDir(), path);
            dir.mkdirs();

            // Recurse on the contents.
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        } catch (IOException e) {
            copyFileAsset(path);
        }
    }

    private void copyFileAsset(String path) {
        File file = new File(MainActivity.this.getFilesDir(), path);
        try {
            InputStream in = MainActivity.this.getAssets().open(path);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.close();
            in.close();
        } catch (IOException e) {

        }
        if (path.contains(".zip")) {
            String directory = MainActivity.this.getFilesDir().getAbsolutePath() + "/";
            Utility.unZipFile(file, directory);
        }
    }
}
