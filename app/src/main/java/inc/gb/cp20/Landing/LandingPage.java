package inc.gb.cp20.Landing;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.AlphaList.AlphaListActivity;
import inc.gb.cp20.AlphaList.AlphabetsList;
import inc.gb.cp20.AlphaList.DrList_POJO;
import inc.gb.cp20.ChangePwd.ChangePasswordAcitvity;
import inc.gb.cp20.Configure.MainActivity;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.List_Utilities.ContentAdapter;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.HorizontalRecylerView;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.Connectivity;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.container.LightContainer;
import inc.gb.cp20.interfaces.DownloadInterface;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;
import inc.gb.cp20.playlist.Playlist;
import inc.gb.cp20.sync.Sync;
import inc.gb.cp20.sync.TagDownloading;


public class LandingPage extends AlphaListActivity implements RecyclerViewClickListener, DownloadInterface {

    View view;
    LinearLayout RHS_Deatailing;
    List<IRCSFResponsePOJO> list;

    Sync sync;
    SweetAlertDialog dialog;
    Dialog content_dialog;
    Typeface font;

    DownloadContentAsync downloadAsync;
    ProgressBar progress;
    TextView total, files;
    int totalSize = 0;
    ListView listView;

    DBHandler dbHandler;

    LinearLayout drawerLinearLayout;
    LinearLayout complLinearLayout;
    ImageView drawerButton;

    String CALLSYNC = "";
    TextView download_now, download_later, cancel, status_bar;

    int fail_download_count = 0;
    int sucess_dowload_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_landing_screen);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                CALLSYNC = bundle.getString("CALLSYNC");
            else
                CALLSYNC = "";
        } catch (Exception e) {
            CALLSYNC = "";
        }

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        dbHandler = DBHandler.getInstance(this);
        drawerLinearLayout = (LinearLayout) findViewById(R.id.drawer);
        drawerButton = (ImageView) findViewById(R.id.dr_btn);

        complLinearLayout = (LinearLayout) findViewById(R.id.compl);

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDrawer();
            }
        });

        //  backupDatabase();
        view = findViewById(R.id.viewid);

        RHS_Deatailing = (LinearLayout) view.findViewById(R.id.rhsdetaling);
        LinearLayout lhsLinearLayout = (LinearLayout) view.findViewById(R.id.lhs);



        try {
            AlphabetsList alphabetsList = new AlphabetsList(this);
            if (dbHandler.genericSelect("Select * from TBPARTY", 2) != null) {
                lhsLinearLayout.addView(alphabetsList.getAlphabestListView("TBPARTY", false, false, true, 1, "", 11));
                alphabetsList.setSidepannel(View.VISIBLE);
                alphabetsList.SerachViewVis(View.VISIBLE);
            } else {
                lhsLinearLayout.setVisibility(View.GONE);
            }


            defaultLayout();
            CallDownloadIRCSF(0);
        } catch (Exception e) {
            //  lhsLinearLayout.setVisibility(View.GONE);
            new TagDownloading(LandingPage.this);
            e.printStackTrace();
        }
        checkSharedPreference();
    }

    public void CallDownloadContainer(int mode, String CATEGORYTYPE, String CATEGORYCODE) {
        sync = new Sync(this);
        sync.downloadContentUrl(this, mode, CATEGORYTYPE, CATEGORYCODE);
        dialog = new SweetAlertDialog(LandingPage.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading....");
        dialog.setContentText("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
    }


    public void GetDrawer() {
        drawerLinearLayout.removeAllViews();
        if (drawerLinearLayout.getVisibility() == View.GONE) {
            TranslateAnimation animate = new TranslateAnimation(complLinearLayout.getHeight(), 0, 0, 0);
            animate.setDuration(700);
            animate.setFillAfter(true);
            complLinearLayout.setAnimation(animate);
            drawerLinearLayout.setVisibility(View.VISIBLE);
        } else {

            TranslateAnimation animate = new TranslateAnimation(0, complLinearLayout.getWidth(), 0, 0);
            animate.setDuration(700);
            animate.setFillAfter(true);
            //   complLinearLayout.setAnimation(animate);
            drawerLinearLayout.setVisibility(View.GONE);
        }
        int prevTextViewId = 0;

        String[][] menudata = dbHandler.genericSelect("Select * From TBDMENU where COL4='0'  order by  CAST (COL3 AS INTEGER) ASC", 3);

        if (menudata != null) {
            for (int i = 0; i < menudata.length; i++) {
                final TextView textView = new TextView(this);

                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setPadding(10, 17, 0, 10);
                textView.setTextSize(20);
                textView.setId(Integer.parseInt(menudata[i][1]));
                textView.setTypeface(font);
                switch (Integer.parseInt(menudata[i][1])) {
                    case 142://Change Password
                        textView.setText(LandingPage.this.getResources().getString(R.string.icon_pen) + "  " + menudata[i][2]);
                        break;
                    case 4://Synchronize
                        textView.setText(LandingPage.this.getResources().getString(R.string.refresh) + "  " + menudata[i][2]);
                        break;
                    case 197://LogOut
                        textView.setText(LandingPage.this.getResources().getString(R.string.icon_power) + "  " + menudata[i][2]);
                        break;
                    case 198://Download Container
                        textView.setText(LandingPage.this.getResources().getString(R.string.icon_download) + "  " + menudata[i][2]);
                        break;
                }
                textView.setOnClickListener(DrawerListner);
                int curTextViewId = prevTextViewId + 1;
                // textView.setId(curTextViewId);
                final RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.BELOW, prevTextViewId);
                //    params.setMargins(10, 80, 10, 10);
                textView.setLayoutParams(params);

                prevTextViewId = curTextViewId;
                drawerLinearLayout.addView(textView);
            }
        }
    }


    private View.OnClickListener DrawerListner = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId())

            {
                case 142://Change Password
                    drawerLinearLayout.setVisibility(View.GONE);
                    Intent ChangePwdIntent = new Intent(LandingPage.this, ChangePasswordAcitvity.class);
                    startActivity(ChangePwdIntent);
                    break;
                case 4://Synchronize
                    drawerLinearLayout.setVisibility(View.GONE);
                    new TagDownloading(LandingPage.this);
                    Sync sync = new Sync(LandingPage.this);
                    sync.prepareRequest(1);
                    sync.prepareRequest(0);
                    break;
                case 197://LogOut
                    showAlertForLogout();
                    break;
                case 198://Download Container
                    drawerLinearLayout.setVisibility(View.GONE);
                    if (Connectivity.isConnected(LandingPage.this))
                        CallDownloadIRCSF(1);
                    else
                        Utility.showSweetAlert(LandingPage.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                    break;
            }
        }
    };


    public void defaultLayout() {
        String[][] pdatat = dbHandler.genericSelect("SELECT * FROM " + DBHandler.TBBRAND + " group by COL1", 9);
        if (RHS_Deatailing != null)
            RHS_Deatailing.removeAllViews();

        for (int i = 0; i < pdatat.length; i++) {
            HorizontalRecylerView horizontalRecylerView = new HorizontalRecylerView(this, pdatat[i][2], pdatat[i][1], "", this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 240);
            params.setMargins(0, 0, 0, 10);
            RHS_Deatailing.addView(horizontalRecylerView.getHorizontalRecylerView(getSupportFragmentManager()),
                    params);
        }
    }


    void showContentDownloadDialog(List<IRCSFResponsePOJO> listP) {
        list = listP;
        content_dialog = new Dialog(LandingPage.this);
        content_dialog.getWindow();
        content_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        content_dialog.setContentView(R.layout.list);
        content_dialog.setCancelable(false);
        content_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.WHITE));
        Display display = ((WindowManager) LandingPage.this
                .getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        content_dialog.setCancelable(false);
        int width = display.getWidth();
        int height = display.getHeight();
        content_dialog.getWindow().setLayout((10 * width) / 12, (5 * height) / 8);
        content_dialog.setContentView(R.layout.list);
        TextView header = (TextView) content_dialog.findViewById(R.id.header);
        header.setText(getResources().getString(R.string.refresh) + " Content update available (" + list.size() + " update(s))");
        header.setTypeface(font);
        listView = (ListView) content_dialog.findViewById(R.id.list);
        listView.setAdapter(new ContentAdapter(LandingPage.this, list));

        CheckBox checkall = (CheckBox) content_dialog.findViewById(R.id.checkall);
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContentAdapter adapter = (ContentAdapter) listView.getAdapter();
                if (compoundButton.isChecked()) {
                    compoundButton.setChecked(true);
                    for (int i = 0; i < adapter.getCount(); i++) {
                        adapter.mCheckedState[i] = compoundButton.isChecked();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    compoundButton.setChecked(false);
                    for (int i = 0; i < adapter.getCount(); i++) {
                        adapter.mCheckedState[i] = compoundButton.isChecked();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        download_now = (TextView) content_dialog.findViewById(R.id.download_now);
        download_later = (TextView) content_dialog.findViewById(R.id.download_later);
        cancel = (TextView) content_dialog.findViewById(R.id.cancel);
        status_bar = (TextView) content_dialog.findViewById(R.id.status_tag);

        total = (TextView) content_dialog.findViewById(R.id.total);
        files = (TextView) content_dialog.findViewById(R.id.files);
        progress = (ProgressBar) content_dialog.findViewById(R.id.progress);

        download_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(LandingPage.this)) {
                    ContentAdapter adapter = (ContentAdapter) listView.getAdapter();
                    totalSize = 0;
                    ArrayList<String[]> urlString = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (adapter.mCheckedState[i] && !adapter.mEnableState[i]) {
                            IRCSFResponsePOJO pojo = list.get(i);
                            urlString.add(new String[]{pojo.getENTRYNO(), pojo.getPATH(), pojo.getFILE_SIZE(), i + ""});

                            try {
                                totalSize = totalSize + Integer.parseInt(pojo.getFILE_SIZE());
                            } catch (NumberFormatException e) {

                            }
                        }
                    }
                    if (urlString.size() > 0) {
                        total.setVisibility(View.VISIBLE);
                        files.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        download_now.setVisibility(View.GONE);
                        download_later.setVisibility(View.GONE);
                        cancel.setVisibility(View.VISIBLE);
                        status_bar.setVisibility(View.VISIBLE);
                        downloadAsync = new DownloadContentAsync(urlString);
                        downloadAsync.execute();
                    } else {
                        Utility.showSweetAlert(LandingPage.this, "Please select at least one content.", CmsInter.NORMAL_TYPE);
                    }
                } else {
                    Utility.showSweetAlert(LandingPage.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                }
            }
        });
        download_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sDialog = new SweetAlertDialog(LandingPage.this, SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("Are you sure you want to cancel the ongoing download ?")
                        .setCancelText("No!")
                        .setConfirmText("Yes!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                downloadAsync.cancel(true);
                                content_dialog.dismiss();
                            }
                        })
                        .show();


            }
        });
        content_dialog.show();
    }


    @Override
    public void onItemListClick(DrList_POJO objDrListPOJO, View view) {
        super.onItemListClick(objDrListPOJO, view);
        String brandQuery = "SELECT COL0, COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8 FROM TBBRAND b where b.COL0 = 'B' and exists (select 1 from TBDPS s, TBDPG t where s.col5 = t.col0 and s.col9= b.col0 and s.col1 =  b.col3 and t.col7 = '1' )";
        String[][] brandData = dbHandler.genericSelect(brandQuery, 9);

        String[][] countData = dbHandler.genericSelect("select count(1)  from TBDPS A , TBDPG B WHERE A.COL3 = '" + objDrListPOJO.getCOL17() + "' and A.COL9 = '" + objDrListPOJO.getCOL16() + "' and B.COL7 = '1' and A.COL5 = B.COL0", 1);

        String[][] playstData = dbHandler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL12 from TBDPS2 a , TBDPG b\n" +
                "        where a.col5 = b.col0\n" +
                "        and a.COL10 = '" + objDrListPOJO.getCOL0() + "' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);

        if (brandData != null && (!countData[0][0].equals("0") || playstData != null)) {
            Intent intent = new Intent(this, LightContainer.class);
            Bundle bundle = new Bundle();
            bundle.putString("customer_id", objDrListPOJO.getCOL0());
            bundle.putString("customer_name", objDrListPOJO.getCOL1());
            bundle.putString("category_code", objDrListPOJO.getCOL17());
            bundle.putString("category_name", objDrListPOJO.getCOL16());
            bundle.putString("thumbnail_category", "B");
            bundle.putString("index", "1");
            bundle.putString("patch", objDrListPOJO.getCOL2());
            intent.putExtras(bundle);
            startActivity(intent);
        } else
            Utility.showSweetAlert(LandingPage.this, "No pages available.", CmsInter.WARNING_TYPE);

    }

    @Override
    public void onItemListMenuClick(final DrList_POJO objDrListPOJO, View view) {
        super.onItemListMenuClick(objDrListPOJO, view);
        String[][] menudata = dbHandler.genericSelect("Select * From TBDMENU where COL4='1'", 3);
        PopupMenu popup = new PopupMenu(LandingPage.this, view);
        if (menudata != null) {
            popup.getMenu().add(1, 1, 1, menudata[0][2]);
        } else {
            popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
        }

        //  popup.getMenu().add(1, 1, 1, menudata[0][0]);
        //Inflating the Popup using xml file

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (objDrListPOJO.getCOL15() != null && objDrListPOJO.getCOL15().equals("0")) {
                    Intent intent = new Intent(LandingPage.this, Playlist.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("customer_id", objDrListPOJO.getCOL0());
                    bundle.putString("customer_name", objDrListPOJO.getCOL1());
                    bundle.putString("category_code", objDrListPOJO.getCOL17());
                    bundle.putString("category_name", objDrListPOJO.getCOL16());
                    bundle.putString("thumbnail_category", "B");
                    bundle.putString("index", "3");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
//
                    String brandQuery = "SELECT COL0, COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8 FROM TBBRAND b where b.COL0 = 'B' and exists (select 1 from TBDPS s, TBDPG t where s.col5 = t.col0 and s.col9= b.col0 and s.col1 =  b.col3 and t.col7 = '1' )";
                    String[][] brandData = dbHandler.genericSelect(brandQuery, 9);

                    String[][] countData = dbHandler.genericSelect("select count(1)  from TBDPS A , TBDPG B WHERE A.COL3 = '" + objDrListPOJO.getCOL17() + "' and A.COL9 = '" + objDrListPOJO.getCOL16() + "' and B.COL7 = '1' and A.COL5 = B.COL0", 1);

                    String[][] playstData = dbHandler.genericSelect("select count(1) from TBDPS2 a , TBDPG b\n" +
                            "        where a.col5 = b.col0\n" +
                            "        and a.COL10 = '" + objDrListPOJO.getCOL0() + "' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 1);

                    if (brandData != null && (!countData[0][0].equals("0") || !playstData[0][0].equals("0"))) {
                        Intent intent = new Intent(LandingPage.this, Playlist.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("customer_id", objDrListPOJO.getCOL0());
                        bundle.putString("customer_name", objDrListPOJO.getCOL1());
                        bundle.putString("category_code", objDrListPOJO.getCOL17());
                        bundle.putString("category_name", objDrListPOJO.getCOL16());
                        bundle.putString("thumbnail_category", "B");
                        bundle.putString("index", "3");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });

        popup.show();
    }


    @Override
    public void onItemClick(TBBRAND item, View view, int position) {

        String brandQuery = "SELECT COL0, COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8 FROM TBBRAND b where b.COL0 = 'B' and exists (select 1 from TBDPS s, TBDPG t where s.col5 = t.col0 and s.col9= b.col0 and s.col1 =  b.col3 and t.col7 = '1' )";
        String[][] brandData = dbHandler.genericSelect(brandQuery, 9);

        String[][] playstData = dbHandler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                "        where a.col5 = b.col0\n" +
                "        and a.COL3 = '" + item.getCOL3() + "' and a.COL9 = '" + item.getCOL0() + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);

        if (brandData != null && playstData != null) {
            Intent intent = new Intent(LandingPage.this, LightContainer.class);
            Bundle bundle = new Bundle();
            bundle.putString("category_code", item.getCOL3());
            bundle.putString("category_name", item.getCOL0());
            bundle.putString("thumbnail_category", "B");
            bundle.putString("index", "2");
            intent.putExtras(bundle);
            startActivity(intent);
        } else
            Utility.showSweetAlert(LandingPage.this, "No pages available.", CmsInter.WARNING_TYPE);
    }

    @Override
    public void onRetryClick(TBBRAND item, View v, int position) {
        CallDownloadContainer(2, item.getCOL0(), item.getCOL3());
    }

    @Override
    public void onPageClick(TBBRAND item, View v, int position, RelativeLayout layout, int total, ScrollView scrollView, TextView textView1) {
        String[][] pagename = dbHandler.genericSelect("select b.COL2 from TBDPS a , TBDPG b where a.col5 = b.col0 and a.col3 =  '" + item.getCOL3() + "' and a.COL9 = '" + item.getCOL0() + "'   and a.COL10 = 'IPL'", 1);
        if (pagename != null) {
            int prevTextViewId = 0;
            for (int i = 0; i < pagename.length; i++) {
                final TextView textView = new TextView(LandingPage.this);

                String temp = "";
                if (i < 10) {
                    if (i == 9)
                        temp = "0" + (i);
                    else
                        temp = "0" + (i + 1);
                } else {
                    temp = "" + (i + 1);
                }


                textView.setText(temp + " " + pagename[i][0]);
                temp = "";
                textView.setTextColor(Color.parseColor("#FFFFFF"));

                int curTextViewId = prevTextViewId + 1;
                textView.setId(curTextViewId);
                final RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.BELOW, prevTextViewId);
                params.setMargins(4, 4, 4, 4);
                textView.setLayoutParams(params);

                prevTextViewId = curTextViewId;
                layout.addView(textView, params);
            }


            RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
            ScrollView scrollView1 = (ScrollView) relativeLayout.getChildAt(8);
            RecyclerView recyclerView = (RecyclerView) relativeLayout.getParent();
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            TextView clsTextView = (TextView) relativeLayout.getChildAt(10);

            for (int i = 0; i < visibleItemCount; i++) {
                RelativeLayout parent = (RelativeLayout) recyclerView.getChildAt(i);
                ScrollView scrollView11 = (ScrollView) parent.getChildAt(8);
                scrollView11.setVisibility(View.GONE);
                TextView clsTextViewc = (TextView) parent.getChildAt(10);
                clsTextViewc.setVisibility(View.GONE);
            }
            scrollView1.setVisibility(View.VISIBLE);
            clsTextView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onRefClick(TBBRAND item, View v, int position, RelativeLayout layout, int total, ScrollView scrollView, TextView textView1) {
        String Query = " select DISTINCT l.COL1,l.COL2,l.COL15  from TBDRG l\n" +
                "        where exists(\n" +
                "        select 1\n" +
                "        from TBDPS a\n" +
                "        where a.col9='" + item.getCOL0() + "'\n" +
                "        and a.col3='" + item.getCOL3() + "'" +
                "        and a.col10='IPL'\n" +
                "        and a.col5=l.col0)";

        String[][] pagename = dbHandler.genericSelect(Query, 3);
        if (pagename != null) {
            int prevTextViewId = 0;
            for (int i = 0; i < pagename.length; i++) {
                final TextView textView = new TextView(this);
                String temp = "";
                if (i < 10) {
                    if (i == 9)
                        temp = "0" + (i);
                    else
                        temp = "0" + (i + 1);
                } else {
                    temp = "" + (i + 1);
                }


                textView.setText(temp + " " + pagename[i][0]);
                temp = "";
                textView.setTextColor(Color.parseColor("#FFFFFF"));

                int curTextViewId = prevTextViewId + 1;
                textView.setId(curTextViewId);
                final RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.BELOW, prevTextViewId);
                params.setMargins(4, 4, 4, 4);
                textView.setLayoutParams(params);

                prevTextViewId = curTextViewId;
                layout.addView(textView, params);

                RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
                ScrollView scrollView1 = (ScrollView) relativeLayout.getChildAt(8);

                TextView clsTextView = (TextView) relativeLayout.getChildAt(10);
                RecyclerView recyclerView = (RecyclerView) relativeLayout.getParent();
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                for (int k = 0; k < visibleItemCount; k++) {
                    RelativeLayout parent = (RelativeLayout) recyclerView.getChildAt(k);
                    ScrollView scrollView11 = (ScrollView) parent.getChildAt(8);
                    scrollView11.setVisibility(View.GONE);
                    TextView clsTextViewc = (TextView) parent.getChildAt(10);
                    clsTextViewc.setVisibility(View.GONE);
                }
                scrollView1.setVisibility(View.VISIBLE);
                clsTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void mainBody(List<IRCSFResponsePOJO> list) {
        if (dialog != null)
            dialog.dismiss();
        if (list != null)
            if (list.size() > 0)
                showContentDownloadDialog(list);
    }

    @Override
    public void onTaskCompleted(boolean b) {

    }


    class DownloadContentAsync extends AsyncTask<Void, Integer, Void> {
        ArrayList<String[]> urlString;
        boolean flag = false;
        int pos = 0;
        int downloadedSize = 0;

        public DownloadContentAsync(ArrayList<String[]> urlString) {
            this.urlString = urlString;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            try {
                for (int i = 0; i < urlString.size(); i++) {
                    if (isCancelled()) break;

                    String[] urls = urlString.get(i);
                    pos = Integer.parseInt(urls[3]);
                    IRCSFResponsePOJO pojo = list.get(pos);
                    pojo.setSTATUS("In Progress");
                    LandingPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ContentAdapter) listView.getAdapter()).notifyDataSetChanged();
                        }
                    });
                    if (Connectivity.isConnected(LandingPage.this)) {
                        String msg = Utility.downloadZipFile(urls[1]);
                        if (isCancelled()) break;
                        if (!msg.startsWith("fail")) {
                            try {
                                File zipfile = new File(msg);
                                String directory = LandingPage.this.getFilesDir().getAbsolutePath()
                                        + "/";
                                String str = Utility.unZipFile(zipfile, directory);
                                if (isCancelled()) break;
                                if (str.equalsIgnoreCase("success")) {
                                    sync.contentAcknowledge(urls[0]);
                                    db.execSQL("UPDATE TBDPG SET COL7 = '1' where COL0 = '" + urls[0] + "'");
                                    flag = true;
                                    downloadedSize = downloadedSize + Integer.parseInt(urls[2]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        pojo.setSTATUS(" ");
                        downloadAsync.cancel(true);
                        //content_dialog.dismiss();
                        LandingPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                total.setVisibility(View.GONE);
                                files.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                download_now.setVisibility(View.VISIBLE);
                                download_later.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.GONE);
                                status_bar.setVisibility(View.GONE);
                                Utility.showSweetAlert(LandingPage.this, CmsInter.AL_NETERROR, CmsInter.ERROR_TYPE);
                            }
                        });
                        break;
                    }
                    if (!flag) {
                        pojo.setSTATUS("Failed");
                        fail_download_count++;
                    } else {
                        pojo.setSTATUS("Completed");
                        sucess_dowload_count++;
                    }
                    LandingPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ContentAdapter) listView.getAdapter()).mEnableState[pos] = true;
                            ((ContentAdapter) listView.getAdapter()).notifyDataSetChanged();
                        }
                    });

                    publishProgress(i + 1);
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setProgress(0);
            progress.setMax(urlString.size());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            progress.setProgress(values[0]);
            files.setText(values[0] + " of " + urlString.size() + " files updates");
            total.setText(downloadedSize + " of " + totalSize + " size");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (content_dialog != null) {
                content_dialog.dismiss();
                dbHandler.UpdateTBRAND();
                defaultLayout();
                if (fail_download_count != 0) {
                    Utility.showSweetAlert(LandingPage.this, "Content downloading failed.", CmsInter.WARNING_TYPE);
                    fail_download_count = 0;
                } else {
                    sucess_dowload_count = 0;
                    Utility.showSweetAlert(LandingPage.this, "Content successfully downloaded.", CmsInter.SUCCESS_TYPE);
                }
            }

        }
    }

    public void CallDownloadIRCSF(int mode) {
        String dataCVR[][] = dbHandler.genericSelect("*", DBHandler.TBCVR,
                "", "", "", 4);
        if (dataCVR != null) {
            String dataCVRSplitCol2[] = dataCVR[0][2].split("\\^");
            if (!dataCVRSplitCol2[27].equals("0")) {
                if (CALLSYNC.equals("1") || mode == 1)
                    CallDownloadContainer(1, "", "");
            } else if (mode == 1)
                Utility.showSweetAlert(LandingPage.this, "No content to download.", CmsInter.NORMAL_TYPE);
        }
    }

    @Override
    public void onBackPressed() {
        showAlertForLogout();
    }

    private void showAlertForLogout() {
        SweetAlertDialog sDialog = new SweetAlertDialog(LandingPage.this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("Do you want to logout?")
                .setCancelText("No!")
                .setConfirmText("Yes!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        Intent intent = new Intent(LandingPage.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish(); // This call is missing.
                    }
                })
                .show();
    }

    private void checkSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("CP20", MODE_PRIVATE);
        String value = preferences.getString("unlisted_key", null);
        if (value == null) {
            String data[][] = dbHandler.genericSelect("Select VAL FROM TBUPW", 1);
            String[] upwData = data[0][0].split("\\^");
            String rnumber = upwData[8];

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("unlisted_key", rnumber);
            editor.commit();
        }
    }
}
