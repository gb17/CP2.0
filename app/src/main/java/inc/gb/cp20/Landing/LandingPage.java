package inc.gb.cp20.Landing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.HorizontalRecylerView;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.container.LightContainer;
import inc.gb.cp20.interfaces.DownloadInterface;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;
import inc.gb.cp20.sync.Sync;


public class LandingPage extends AlphaListActivity implements RecyclerViewClickListener, DownloadInterface {

    View view;
    LinearLayout RHS_Deatailing;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_landing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        dbHandler = DBHandler.getInstance(this);

        //  backupDatabase();
        view = findViewById(R.id.viewid);


        RHS_Deatailing = (LinearLayout) view.findViewById(R.id.rhsdetaling);
        LinearLayout lhsLinearLayout = (LinearLayout) view.findViewById(R.id.lhs);

        AlphabetsList alphabetsList = new AlphabetsList(this);
        lhsLinearLayout.addView(alphabetsList.getAlphabestListView("TBPARTY", false, false, true));
        alphabetsList.setSidepannel(View.VISIBLE);
        alphabetsList.SerachViewVis(View.VISIBLE);
        defaultLayout();


        sync = new Sync(this);
        sync.downloadContentUrl(this);

        dialog = new SweetAlertDialog(LandingPage.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading....");
        dialog.setContentText("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();


    }


    public void defaultLayout() {
        String[][] pdatat = dbHandler.genericSelect("SELECT * FROM TBBRAND group by COL1", 9);
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
    void showContentDownloadDialog(final List<IRCSFResponsePOJO> list) {
        content_dialog = new Dialog(LandingPage.this);
        content_dialog.getWindow();
        content_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        content_dialog.setContentView(R.layout.list);

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
        for (int s = 0; s < list.size(); s++) {
            IRCSFResponsePOJO pojo = list.get(s);
            totalSize = totalSize + Integer.parseInt(pojo.getFILE_SIZE());
        }
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
                        LinearLayout itemLayout = (LinearLayout) listView.getChildAt(i);
                        CheckBox cb = (CheckBox) itemLayout.getChildAt(0);
                        cb.setChecked(true);
                        adapter.mCheckedState[i] = compoundButton.isChecked();
                    }
                } else {
                    compoundButton.setChecked(false);
                    for (int i = 0; i < adapter.getCount(); i++) {
                        LinearLayout itemLayout = (LinearLayout) listView.getChildAt(i);
                        CheckBox cb = (CheckBox) itemLayout.getChildAt(0);
                        cb.setChecked(false);
                        adapter.mCheckedState[i] = compoundButton.isChecked();
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
                total.setVisibility(View.VISIBLE);
                files.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                download_now.setVisibility(View.GONE);
                download_later.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                Status_bar.setVisibility(View.VISIBLE);
                ContentAdapter adapter = (ContentAdapter) listView.getAdapter();
                ArrayList<String[]> urlString = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (adapter.mCheckedState[i]) {
                        IRCSFResponsePOJO pojo = list.get(i);
                        urlString.add(new String[]{pojo.getENTRYNO(), pojo.getPATH(), pojo.getFILE_SIZE()});
                    }
                }
                if (urlString.size() > 0) {
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
        bundle.putString("category_code", objDrListPOJO.getCOL5());
        bundle.putString("category_name", objDrListPOJO.getCOL10());
        bundle.putString("thumbnail_category", objDrListPOJO.getCOL16());
        bundle.putString("index", "1");
        intent.putExtras(bundle);
        startActivity(intent);


    }

    @Override
    public void onItemListMenuClick(DrList_POJO objDrListPOJO) {
        super.onItemListMenuClick(objDrListPOJO);
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.hotlink);
        // Set dialog title

        dialog.show();
        Toast.makeText(LandingPage.this, "objDrListPOJO" + objDrListPOJO.getCOL7(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void recyclerViewListClicked(View v, int position) {

        Toast.makeText(LandingPage.this, "Coool--->" + position, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(LandingPage.this, ContentLibrary.class);
//        startActivity(intent);
    }

    @Override
    public void mainBody(List<IRCSFResponsePOJO> list) {
        if (dialog != null)
            dialog.dismiss();
        if (list != null)
            showContentDownloadDialog(list);
    }

    @Override
    public void onTaskCompleted(boolean b) {

    }


    class DownloadContentAsync extends AsyncTask<Void, Integer, Void> {
        ArrayList<String[]> urlString;
        boolean flag = false;
        TextView status;
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
                    pos = i;
                    String[] urls = urlString.get(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status = (TextView) ((LinearLayout) listView.getChildAt(pos)).getChildAt(5);
                            status.setText("In Progress");
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!flag)
                                status.setText("Failed");
                            else
                                status.setText("Completed");
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

}
