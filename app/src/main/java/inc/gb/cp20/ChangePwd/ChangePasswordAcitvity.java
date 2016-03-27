package inc.gb.cp20.ChangePwd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.Configure.MainActivity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.ChangePassword;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.RestClient;
import inc.gb.cp20.Util.Utility;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by GB on 3/17/16.
 */
public class ChangePasswordAcitvity extends Activity implements View.OnClickListener {
    private EditText edtPwd;
    private EditText edtNewPwd;
    private EditText edtCnfPwd;
    private Button btncancel;
    private Button btnchangepwd;

    String UsernameString = "";
    String PasswordString = "";

    String instaneceId = "";
    String Repcode = "";
    String RoleCode = "";
    String Version = "";
    String ClientID = "";
    Context context;

    SweetAlertDialog dialog;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        context = ChangePasswordAcitvity.this;
        dbHandler = DBHandler.getInstance(context);
        findViews();
        getdata();
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-03-17 15:49:04 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        edtNewPwd = (EditText) findViewById(R.id.edt_new_pwd);
        edtCnfPwd = (EditText) findViewById(R.id.edt_cnf_pwd);
        btncancel = (Button) findViewById(R.id.btncancel);
        btnchangepwd = (Button) findViewById(R.id.btnchangepwd);

        btncancel.setOnClickListener(this);
        btnchangepwd.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-03-17 15:49:04 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btncancel) {
            finish();
            //  CallForgotPassword();
        } else if (v == btnchangepwd) {

            passwordpolicy(edtPwd.getText().toString(), edtNewPwd.getText().toString(), edtCnfPwd.getText().toString());
        }
    }


    void passwordpolicy(String strCpwOldPwd, String strCpwNewPwd,
                        String strCpwConfirmPwd) {

        try {
            if (Utility.isEmpty(strCpwOldPwd) ||
                    Utility.isEmpty(strCpwNewPwd)
                    || Utility.isEmpty(strCpwConfirmPwd)) {
                Utility.showSweetAlert(context, CmsInter.AL_BLANK, CmsInter.ERROR_TYPE);
                return;
            } else if (Utility.isSpecial(strCpwOldPwd, "#$%^&")
                    || Utility.isSpecial(strCpwNewPwd, "#$%^&")
                    || Utility.isSpecial(strCpwConfirmPwd, "#$%^&")) {
                Utility.showSweetAlert(context, CmsInter.AL_IS_SPECIAL, CmsInter.ERROR_TYPE);
                return;
            } else if (Utility.isSpace(strCpwOldPwd)
                    || Utility.isSpace(strCpwNewPwd)
                    || Utility.isSpace(strCpwConfirmPwd)) {
                Utility.showSweetAlert(context, CmsInter.AL_SPACE, CmsInter.ERROR_TYPE);
                return;
            } else if (!strCpwOldPwd.equals(PasswordString)) {
                Utility.showSweetAlert(context, CmsInter.AL_OLD_PASS_INVALID, CmsInter.ERROR_TYPE);
                return;
            } else if (!strCpwNewPwd.equals(strCpwConfirmPwd)) {
                Utility.showSweetAlert(context, CmsInter.AL_CONFIRM_PWD, CmsInter.ERROR_TYPE);
                return;
            } else if (Utility.lengthCheck(strCpwNewPwd,
                    Integer.parseInt("4"))
                    || Utility.lengthCheck(strCpwConfirmPwd,
                    Integer.parseInt("4"))) {
                Utility.showTempAlert(context,
                        "New password Length should not be less than 4");

            } else {
                ChangePassword(strCpwNewPwd);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void CallForgotPassword() {

        getprogressDialog("Requesting...");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setCLIENTID(getResources().getString(R.string.clientid));
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
                        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, CmsInter.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText(chg.getMSG())
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent loginIntent = new Intent(ChangePasswordAcitvity.this, MainActivity.class);
                                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(loginIntent);
                                    }
                                })
                                .show();
                    } else {
                        Utility.showSweetAlert(context, chg.getMSG(), CmsInter.ERROR_TYPE);
                    }
                } else {
                    Utility.showSweetAlert(context, "Network Error", CmsInter.ERROR_TYPE);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    public void ChangePassword(String newpassword) {

        getprogressDialog("Changing Pasword...");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setCLIENTID(getResources().getString(R.string.clientid));
        changePassword.setREPCODE(Repcode);
        changePassword.setBU("2");
        changePassword.setNPWD(newpassword);
        changePassword.setOPWD(PasswordString);

        RestClient.GitApiInterface service = RestClient.getClient();

        Call<ChangePassword> changePasswordCall = service.CallChangePassword(changePassword);
        changePasswordCall.enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Response<ChangePassword> response, Retrofit retrofit) {
                dialog.dismiss();
                final ChangePassword chg = response.body();
                if (chg.getOUT().equals("0")) {
                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, CmsInter.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText(chg.getMSG())
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dbHandler.GenricUpdates(DBHandler.TBCVR, "COL2", chg.getCVR(), "COL1", "CVR");
                                    Intent loginIntent = new Intent(ChangePasswordAcitvity.this, MainActivity.class);
                                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(loginIntent);
                                }
                            })
                            .show();
                } else {
                    Utility.showSweetAlert(context, chg.getMSG(), CmsInter.ERROR_TYPE);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });

    }


    public void getprogressDialog(String msg) {
        dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean getdata() {
        {
            boolean mission;
            dbHandler = DBHandler.getInstance(context);
            try {
                String data[][] = dbHandler.genericSelect("*", DBHandler.TBUPW,
                        "", "", "", 6);
                if (data != null) {
                    //data[0][2];
                    String datavalues[] = data[0][3].split("\\^");
                    instaneceId = datavalues[4];
                    Repcode = datavalues[3];
                    RoleCode = datavalues[5].substring(0, 1);
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
                    UsernameString = dataCVRSplitCol2[25];
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
    }

    public void GetDialog(String Msg, int ALERT_TYPE) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, ALERT_TYPE)
                .setTitleText(Msg)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                    }
                });
        sweetAlertDialog.show();
    }
}
