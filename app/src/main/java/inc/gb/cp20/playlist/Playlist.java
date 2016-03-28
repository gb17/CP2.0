package inc.gb.cp20.playlist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Landing.LandingPage;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.DividerItemDecoration;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.container.LightContainer;
import inc.gb.cp20.sync.Sync;

/**
 * Created by Shubham on 3/4/16.
 */
public class Playlist extends Activity {
    DBHandler handler;
    RecyclerView recyclerView;
    private String customer_id = "";
    private String category_code = "";
    private String category_name = "";
    private String thumbnail_category = "";
    private String index = "3";

    private String playListData[][];
    private String brandListData[][];
    private EditText searchView;
    private String searchText = "";

    TextView edit, preview, done, reset, cancel, library, name, page_count, searchClick;
    Typeface font;
    String nameStr = "";

    RecyclerView grid;

    ArrayList<String[]> recyclerData, gridData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        font = Typeface.createFromAsset(this.getAssets(),
                "fontawesome-webfont.ttf");

        Bundle extras = getIntent().getExtras();
        gridData = new ArrayList<>();

        customer_id = extras.getString("customer_id");
        category_code = extras.getString("category_code");
        category_name = extras.getString("category_name");
        thumbnail_category = extras.getString("thumbnail_category");

        handler = DBHandler.getInstance(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        grid = (RecyclerView) findViewById(R.id.grid);
        grid.setTag("2");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setTag("1");

        String[][] countData = handler.genericSelect("Select count(1) from TBDPS2 where col10 = '" + customer_id + "'", 1);
        if (countData[0][0].equals("0")) {
            //select and insert
            db = handler.getWritableDatabase();
            db.execSQL(" insert into TBDPS2 select A.COL0, A.COL1, A.COL2, A.COL3, A.COL4, A.COL5, A.COL6, A.COL7, A.COL8, A.COL9, '" + customer_id + "', '0', A.COL11 from TBDPS A WHERE COL3 = '" + category_code + "' and COL9 = '" + category_name + "'");
        }
        //initialize playlist
        playListData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS2 a , TBDPG b\n" +
                "        where a.col5 = b.col0\n" +
                "        and a.COL10 = '" + customer_id + "' order by  CAST (a.col2 AS INTEGER) ASC ", 8);

        if (playListData != null)
            recyclerData = twoDArrayToList(playListData);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        grid.setLayoutManager(layoutManager2);
        grid.setHasFixedSize(true);
        grid.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        grid.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new PlaylistAdapter(this, gridData, recyclerData, 0));

        String custData[][] = handler.genericSelect("Select COL1, COL10, COL11 From TBPARTY where col0 = '" + customer_id + "'", 3);

        TextView data1 = (TextView) findViewById(R.id.data1);
        TextView data2 = (TextView) findViewById(R.id.data2);
        TextView data3 = (TextView) findViewById(R.id.data3);
        name = (TextView) findViewById(R.id.name);
        page_count = (TextView) findViewById(R.id.page_count);
        if (recyclerData != null)
            page_count.setText("(" + recyclerData.size() + " Pages)");

        if (custData != null) {
            nameStr = custData[0][0];
            name.setText(custData[0][1]);
            data1.setText(custData[0][0]);
            data2.setText(custData[0][1]);
            data3.setText(custData[0][2]);
        }
        if (recyclerData != null)
            for (int i = 0; i < recyclerData.size(); i++) {
                String[] str = recyclerData.get(i);
                if (str[7].equals("1"))
                    name.setText("Custom Playlist");
            }

        library = (TextView) findViewById(R.id.library);
        searchView = (EditText) findViewById(R.id.search);
        searchClick = (TextView) findViewById(R.id.searchClick);
        searchClick.setText(getResources().getString(R.string.search) + " Search");
        searchClick.setTypeface(font);
        searchClick.setOnClickListener(searchListener);

        edit = (TextView) findViewById(R.id.edit);
        edit.setText(getResources().getString(R.string.icon_pen) + " Edit");
        edit.setTypeface(font);
        edit.setOnClickListener(editListener);

        preview = (TextView) findViewById(R.id.preview);
        preview.setText(getResources().getString(R.string.play) + " Preview");
        preview.setTypeface(font);
        preview.setOnClickListener(previewListener);
        if (playListData == null)
            preview.setVisibility(View.INVISIBLE);

        done = (TextView) findViewById(R.id.done);
        done.setText(getResources().getString(R.string.icon_tick) + " Done");
        done.setTypeface(font);
        done.setOnClickListener(doneListener);
        done.setVisibility(View.GONE);

        reset = (TextView) findViewById(R.id.reset);
        reset.setText(getResources().getString(R.string.refresh) + " Reset");
        reset.setTypeface(font);
        reset.setVisibility(View.GONE);
        reset.setOnClickListener(resetListener);

        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setText(getResources().getString(R.string.close) + " Cancel");
        cancel.setTypeface(font);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(cancelListener);

//        done = (TextView) findViewById(R.id.done);
//        done.setOnClickListener(doneListener);


//        // Setup ItemTouchHelper
//        ItemTouchHelper.Callback callback = new RecyclerTouchHelper(mAdapter);
//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(recyclerView);

    }


    public ArrayList<String[]> twoDArrayToList(String[][] twoDArray) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        for (int i = 0; i < twoDArray.length; i++)
            list.add(new String[]{twoDArray[i][0], twoDArray[i][1], twoDArray[i][2], twoDArray[i][3], twoDArray[i][4], twoDArray[i][5], twoDArray[i][6], twoDArray[i][7]});
        return list;
    }

    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //grid.setAdapter(new GridAdapter(Playlist.this, gridData, recyclerData));
            searchText = searchView.getText().toString();
            if (searchText.equals("")) {
                if (gridData != null) {
                    gridData.clear();
                    grid.setAdapter(new BrandlistAdapter(Playlist.this, gridData, recyclerData));
                }
            } else {
                brandListData = handler.genericSelect("select COL0, 1, COL1, COL2, COL3, COL5, COL16, '0' from TBDPG b where COL4 like '%" + searchText + "%' and not exists (Select 1 from TBDPS2 a where COL10 = '" + customer_id + "' and a.COL5 = b.COL0) and COL8 = 'P'", 8);
                if (brandListData != null) {
                    gridData = twoDArrayToList(brandListData);
                    grid.setAdapter(new BrandlistAdapter(Playlist.this, gridData, recyclerData));
                }
            }
            PlaylistAdapter adapter = (PlaylistAdapter) recyclerView.getAdapter();
            recyclerView.setAdapter(new PlaylistAdapter(Playlist.this, gridData, adapter.recyclerData, 1));
            recyclerView.setOnDragListener(
                    new MyDragListener(Playlist.this, gridData, adapter.recyclerData));
        }
    };

    View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            edit.setVisibility(View.GONE);
            preview.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            library.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            searchClick.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);
            //initialize playlist
            recyclerView.setAdapter(new PlaylistAdapter(Playlist.this, gridData, recyclerData, 1));
        }
    };

    public void setDeleteVisible(int index) {
        LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());

        for (int i = 0; i < manager.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) manager.getChildAt(i);
            if (index == 1)
                relativeLayout.getChildAt(4).setVisibility(View.VISIBLE);
            else if (index == 0)
                relativeLayout.getChildAt(4).setVisibility(View.INVISIBLE);
        }
    }

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playListData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS2 a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL10 = '" + customer_id + "' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
            if (playListData != null)
                recyclerData = twoDArrayToList(playListData);
            if (gridData != null) {
                gridData.clear();
                grid.setAdapter(new BrandlistAdapter(Playlist.this, gridData, recyclerData));
            }
            recyclerView.setAdapter(new PlaylistAdapter(Playlist.this, gridData, recyclerData, 0));
            if (playListData != null)
                page_count.setText("(" + recyclerData.size() + " Pages)");

            edit.setVisibility(View.VISIBLE);
            if (playListData != null)
                preview.setVisibility(View.VISIBLE);
            done.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            reset.setVisibility(View.GONE);
            library.setVisibility(View.INVISIBLE);
            searchView.setVisibility(View.INVISIBLE);
            searchClick.setVisibility(View.INVISIBLE);
            grid.setVisibility(View.INVISIBLE);
            setDeleteVisible(0);
        }
    };

    View.OnClickListener doneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ArrayList<String[]> recyclerData = ((PlaylistAdapter) recyclerView.getAdapter()).recyclerData;
            if (recyclerData.size() > 0) {
                SQLiteDatabase db = handler.getWritableDatabase();
                db.execSQL("delete  from TBDPS2 where COL10 = '" + customer_id + "'");

                for (int i = 0; i < recyclerData.size(); i++) {
                    String[] strData = recyclerData.get(i);
                    int sequence = i + 1;
                    String query = "Insert into TBDPS2 select ' ', ' ', '" + sequence + "', ' ', ' ', COL6, '0', ' ', ' ', ' ', '" + customer_id + "', '0', ' '  from TBDPG where COL0 = '" + strData[0] + "'";
                    db.execSQL(query);
                }
                db.execSQL("update  TBDPS2  set COL11 = '1' where COL10 = '" + customer_id + "' and not exists (Select 1 from TBDPS a where a.COL5 = TBDPS2.COL5 and a.COL3 = '" + category_code + "' )");
                db.execSQL("update  TBPARTY  set COL15 = '1' where COL0 = '" + customer_id + "'");
                String[][] tbdps2Data = handler.genericSelect("Select * from TBDPS2 where COL10 = '" + customer_id + "'", 12);


                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String txndate = formatter.format(date);

                String uniqueNumber = Utility.getUniqueNo();

                long time = System.currentTimeMillis();
                db = handler.getWritableDatabase();
                for (int i = 0; i < tbdps2Data.length; i++) {
                    ContentValues cv = new ContentValues();
                    cv.put("COL0", tbdps2Data[i][0]);
                    cv.put("COL1", tbdps2Data[i][1]);
                    cv.put("COL2", tbdps2Data[i][2]);
                    cv.put("COL3", tbdps2Data[i][3]);
                    cv.put("COL4", tbdps2Data[i][4]);
                    cv.put("COL5", tbdps2Data[i][5]);
                    cv.put("COL6", tbdps2Data[i][6]);
                    cv.put("COL7", tbdps2Data[i][7]);
                    cv.put("COL8", tbdps2Data[i][8]);
                    cv.put("COL9", tbdps2Data[i][9]);
                    cv.put("COL10", tbdps2Data[i][10]);
                    cv.put("COL11", tbdps2Data[i][11]);
                    cv.put("COL12", "0");
                    cv.put("COL13", txndate);
                    cv.put("COL14", uniqueNumber + i);
                    cv.put("COL15", time);

                    db.insert("TBDPS3", null, cv);
                }
                if (playListData != null)
                    page_count.setText("(" + recyclerData.size() + " Pages)");
                SyncData();
            } else {
                Utility.showSweetAlert(Playlist.this, "Please select at least one pages", CmsInter.WARNING_TYPE);
            }
        }
    };

    private void SyncData() {
        Sync sync = new Sync(Playlist.this);
        sync.prepareRequest(2);
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Playlist.this, CmsInter.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Data Saved Successfully")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(Playlist.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();
    }

    View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SweetAlertDialog dialog = new SweetAlertDialog(Playlist.this, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("Your Playlist will Reset?")
                    .setCancelText("No, cancel it!")
                    .setConfirmText("Yes, reset it!")
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
                            makePlaylistAsDefault();
                            sDialog
                                    .setTitleText("Reset Done!")
                                    .setContentText("Your Playlist is reset!")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener previewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Playlist.this, LightContainer.class);
            Bundle bundle = new Bundle();
            bundle.putString("customer_id", customer_id);
            bundle.putString("customer_name", nameStr);
            bundle.putString("category_code", category_code);
            bundle.putString("category_name", category_name);
            bundle.putString("thumbnail_category", thumbnail_category);
            bundle.putString("index", index);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void makePlaylistAsDefault() {
        //initialize playlist
        playListData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, '0' from TBDPS a , TBDPG b\n" +
                "        where a.col5 = b.col0\n" +
                "        and a.COL3 = '" + category_code + "' and a.COL9 = '" + category_name + "' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
        if (playListData == null) {
            Utility.showSweetAlert(Playlist.this, "No default playlist available", CmsInter.NORMAL_TYPE);
        } else {
            recyclerData = twoDArrayToList(playListData);

            searchText = searchView.getText().toString();
            if (searchText.equals("")) {
                if (gridData != null) {
                    gridData.clear();
                    grid.setAdapter(new BrandlistAdapter(Playlist.this, gridData, recyclerData));
                }
            } else {
                brandListData = handler.genericSelect("select COL0, 1, COL1, COL2, COL3, COL5, COL16, '0' from TBDPG b where COL4 like '%" + searchText + "%' and not exists (Select 1 from TBDPS2 a where COL10 = '" + customer_id + "' and a.COL5 = b.COL0)", 8);
                if (brandListData != null) {
                    gridData = twoDArrayToList(brandListData);
                    grid.setAdapter(new BrandlistAdapter(Playlist.this, gridData, recyclerData));
                }
            }

            recyclerView.setAdapter(new PlaylistAdapter(Playlist.this, gridData, recyclerData, 0));
            if (playListData != null)
                page_count.setText("(" + recyclerData.size() + " Pages)");
        }
    }
}
