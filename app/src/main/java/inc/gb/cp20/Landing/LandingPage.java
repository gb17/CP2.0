package inc.gb.cp20.Landing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.AlphaList.AlphaListActivity;
import inc.gb.cp20.AlphaList.AlphabetsList;
import inc.gb.cp20.AlphaList.DrList_POJO;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.List_Utilities.ContentAdapter;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.HorizontalRecylerView;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.changepwd.ChangePasswordAcitvity;
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
    Button drawerButton;

    String CALLSYNC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_landing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        drawerButton = (Button) findViewById(R.id.dr_btn);

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
            lhsLinearLayout.addView(alphabetsList.getAlphabestListView("TBPARTY", false, false, true));
            alphabetsList.setSidepannel(View.VISIBLE);
            alphabetsList.SerachViewVis(View.VISIBLE);
            Updategbdb();
            defaultLayout();
            //   CallDownloadIRCSF();


        } catch (Exception e) {

        }


    }

    public void CallDownloadContainer() {
        sync = new Sync(this);
        sync.downloadContentUrl(this);
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

        String[][] menudata = dbHandler.genericSelect("Select * From TBDMENU where COL4='0'", 3);
        for (int i = 0; i < menudata.length; i++) {
            final TextView textView = new TextView(this);

            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setPadding(10, 17, 0, 0);
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
                    break;
                case 197://LogOut
                    drawerLinearLayout.setVisibility(View.GONE);
                    finish();

                    break;
                case 198://Download Container
                    drawerLinearLayout.setVisibility(View.GONE);
                    CallDownloadIRCSF();
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
                    new LinearLayout.LayoutParams(params));
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                databaseAdapter.open();
//
//                if (!s.equals("")) {
//                    String[][] pdatat = databaseAdapter.genericSelect("SELECT * FROM TBBRND where col_3 like '%" + s + "%' group by col_2", 9);
//                    RHS_Deatailing.removeAllViews();
//                    if (pdatat != null)
//                        for (int i = 0; i < pdatat.length; i++) {
//                            HorizontalRecylerView horizontalRecylerView = new HorizontalRecylerView(LandingPage.this, pdatat[i][1], pdatat[i][0], s);
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 230);
//                            params.setMargins(0, 0, 0, 10);
//
//                            RHS_Deatailing.addView(horizontalRecylerView.getHorizontalRecylerView(getSupportFragmentManager()),
//                                    new LinearLayout.LayoutParams(params));
//                        }
//                } else {
//                    defaultLayout();
//                }
//
//                return true;
//            }
//        });
//        return true;
//
//
//    }
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
                .getSystemService(LandingPage.this.WINDOW_SERVICE))
                .getDefaultDisplay();
        content_dialog.setCancelable(false);
        int width = display.getWidth();
        int height = display.getHeight();
        content_dialog.getWindow().setLayout((10 * width) / 12, (4 * height) / 6);
        content_dialog.setContentView(R.layout.list);
        TextView header = (TextView) content_dialog.findViewById(R.id.header);
        header.setText(getResources().getString(R.string.refresh) + " Content update available (" + list.size() + " update)");
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
//                        LinearLayout itemLayout = (LinearLayout) listView.getChildAt(i);
//                        CheckBox cb = (CheckBox) itemLayout.getChildAt(0);
//                        cb.setChecked(true);
                        adapter.mCheckedState[i] = compoundButton.isChecked();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    compoundButton.setChecked(false);
                    for (int i = 0; i < adapter.getCount(); i++) {
//                        LinearLayout itemLayout = (LinearLayout) listView.getChildAt(i);
//                        CheckBox cb = (CheckBox) itemLayout.getChildAt(0);
//                        cb.setChecked(false);
                        adapter.mCheckedState[i] = compoundButton.isChecked();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        final TextView download_now = (TextView) content_dialog.findViewById(R.id.download_now);
        final TextView download_later = (TextView) content_dialog.findViewById(R.id.download_later);
        final TextView cancel = (TextView) content_dialog.findViewById(R.id.cancel);
        final TextView Status_bar = (TextView) content_dialog.findViewById(R.id.status_tag);

        total = (TextView) content_dialog.findViewById(R.id.total);
        files = (TextView) content_dialog.findViewById(R.id.files);
        progress = (ProgressBar) content_dialog.findViewById(R.id.progress);

        download_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentAdapter adapter = (ContentAdapter) listView.getAdapter();
                ArrayList<String[]> urlString = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (adapter.mCheckedState[i]) {
                        IRCSFResponsePOJO pojo = list.get(i);
                        urlString.add(new String[]{pojo.getENTRYNO(), pojo.getPATH(), pojo.getFILE_SIZE(), i + ""});
                        totalSize = totalSize + Integer.parseInt(pojo.getFILE_SIZE());
                    }
                }
                if (urlString.size() > 0) {
                    total.setVisibility(View.VISIBLE);
                    files.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    download_now.setVisibility(View.GONE);
                    download_later.setVisibility(View.GONE);
                    cancel.setVisibility(View.VISIBLE);
                    Status_bar.setVisibility(View.VISIBLE);
                    downloadAsync = new DownloadContentAsync(urlString);
                    downloadAsync.execute();
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
                downloadAsync.cancel(true);
                content_dialog.dismiss();
            }
        });
        content_dialog.show();
    }


    @Override
    public void onItemListClick(DrList_POJO objDrListPOJO, View view) {
        super.onItemListClick(objDrListPOJO, view);
        Intent intent = new Intent(this, LightContainer.class);
        Bundle bundle = new Bundle();
        bundle.putString("customer_id", objDrListPOJO.getCOL0());
        bundle.putString("customer_name", objDrListPOJO.getCOL1());
        bundle.putString("category_code", objDrListPOJO.getCOL17());
        bundle.putString("category_name", objDrListPOJO.getCOL16());
        bundle.putString("thumbnail_category", "B");
        bundle.putString("index", "1");
        intent.putExtras(bundle);
        startActivity(intent);


    }

    @Override
    public void onItemListMenuClick(final DrList_POJO objDrListPOJO, View view) {
        super.onItemListMenuClick(objDrListPOJO, view);

        PopupMenu popup = new PopupMenu(LandingPage.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(LandingPage.this, Playlist.class);
                Bundle bundle = new Bundle();
                bundle.putString("customer_id", objDrListPOJO.getCOL0());
                bundle.putString("customer_name", objDrListPOJO.getCOL1());
                bundle.putString("category_code", objDrListPOJO.getCOL5());
                bundle.putString("category_name", objDrListPOJO.getCOL10());
                bundle.putString("thumbnail_category", objDrListPOJO.getCOL16());
                bundle.putString("index", "3");
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });

        popup.show();
    }


    @Override
    public void onItemClick(TBBRAND item, View view, int position) {
        Intent intent = new Intent(LandingPage.this, LightContainer.class);
        Bundle bundle = new Bundle();
        bundle.putString("category_code", item.getCOL3());
        bundle.putString("category_name", item.getCOL0());
        bundle.putString("thumbnail_category", "B");
        bundle.putString("index", "2");
        intent.putExtras(bundle);
        startActivity(intent);
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
                                flag = true;
                                downloadedSize = downloadedSize + Integer.parseInt(urls[2]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!flag)
                        pojo.setSTATUS("Failed");
                    else
                        pojo.setSTATUS("Completed");
                    LandingPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
            if (content_dialog != null)
                content_dialog.dismiss();
        }
    }

    public void CallDownloadIRCSF() {
        String dataCVR[][] = dbHandler.genericSelect("*", DBHandler.TBCVR,
                "", "", "", 4);
        if (dataCVR != null) {
            String dataCVRSplitCol2[] = dataCVR[0][2].split("\\^");
            if (!dataCVRSplitCol2[27].equals("0"))
                CallDownloadContainer();

        }
    }


    void Updategbdb() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String Query = "update TBBRAND set col5 = ( select count(distinct b.col0) from TBDPS a , TBDPG b where a.col5 = b.col0 and a.col3 =  TBBRAND.col3 and a.COL9 =  TBBRAND.col0 and a.COL10 = 'IPL'  and b.COL7 ='0' and b.col8 = 'P'), col9 = ( select count (distinct b.col3) from TBDPS a , TBDRG b where a.col5 = b.col0 and a.col3 =  TBBRAND.col3 and a.COL9 = TBBRAND.col0 and a.COL10 = 'IPL'), col10  = ( select  count(distinct b.col0) from TBDPS a , TBDPG b where a.col5 = b.col0 and a.col3 =  TBBRAND. col3 and a.COL9 = TBBRAND.col0 and a.COL10 = 'IPL'  and b.COL7 ='0' and  b.col7 = 0 and b.col8 = 'P')";
                dbHandler.ExecuteQuery(Query);
            }
        }).start();


    }


}
