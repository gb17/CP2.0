package inc.gb.cp20.ContentLib;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.List_Utilities.ContentAdapter;
import inc.gb.cp20.Models.ContentPage;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.RefrenceContent;
import inc.gb.cp20.Models.SearchData;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.DividerItemDecoration;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForContentLibrary;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForPages;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForRefrence;
import inc.gb.cp20.RecylerView.ThumbnailAdpForSearch;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.interfaces.DownloadInterface;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;
import inc.gb.cp20.sync.Sync;


public class ContentLibrary extends AppCompatActivity implements RecyclerViewClickListener, DownloadInterface {


    private RecyclerView recyclerView, recyclerView_sub, recyclerView_ref, recyclerView_search;


    RelativeLayout sublistRelativeLayout, ref_sublistRelativeLayout;
    String WhereQuery = "";

    int a = 0;
    LinearLayoutManager layoutManager_main_list;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int firstVisiblePosition = -1;
    int findLastVisibleItemPosition = -1;
    LinearLayoutManager layoutManager3;

    LinearLayout content_view;

    int scrollforpage = 0;
    int scrollforref = 0;
    int scrollforpage_scr = 0;


    //Call IRCSF
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
    TextView search_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = DBHandler.getInstance(this);
        setContentView(R.layout.content_library_layout);
        content_view = (LinearLayout) findViewById(R.id.content_lib);
        recyclerView_search = (RecyclerView) findViewById(R.id.recycler_view_for_search);
        layoutManager3 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_search.setHasFixedSize(true);
        recyclerView_search.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView_search.setItemAnimator(new DefaultItemAnimator());
        recyclerView_search.setLayoutManager(layoutManager3);

        search_result = (TextView) findViewById(R.id.search_result);


        defaultLayout();

    }

    public void defaultLayout() {
        String[][] pdatat = dbHandler.genericSelect("SELECT * FROM TBBRAND group by COL1", 9);
        if (content_view != null)
            content_view.removeAllViews();

        for (int i = 0; i < pdatat.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 5);
            content_view.addView(getContenLibraryView(pdatat[i][1], pdatat[i][2]),
                    params);
        }
    }

    public View getContenLibraryView(String Code, String Header) {
        View libView = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(this.getAssets(),
                "fontawesome-webfont.ttf");

        libView = inflater.inflate(R.layout.container_horizontal_recylerview, null);
        List<TBBRAND> thumbnailPOJOList = new ArrayList<>();
        recyclerView = (RecyclerView) libView.findViewById(R.id.recycler_view_for_hr);
        recyclerView_sub = (RecyclerView) libView.findViewById(R.id.recycler_view_for_hr_sub);
        recyclerView_ref = (RecyclerView) libView.findViewById(R.id.recycler_view_for_hr_sub_ref);


        sublistRelativeLayout = (RelativeLayout) libView.findViewById(R.id.sublist);
        ref_sublistRelativeLayout = (RelativeLayout) libView.findViewById(R.id.sublistfor_ref);


        TextView HeaderTextView = (TextView) libView.findViewById(R.id.header);
        HeaderTextView.setText(this.getResources().getString(R.string.threebar) + " " + Header);
        HeaderTextView.setTypeface(font);


        layoutManager_main_list
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager_main_list);

        ThumbnailAdapterForContentLibrary mAdapter = new ThumbnailAdapterForContentLibrary(thumbnailPOJOList, this, getSupportFragmentManager(), this, 99);
        recyclerView_sub.addItemDecoration(new DividerItemDecoration(ContentLibrary.this, LinearLayoutManager.HORIZONTAL));

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData(DBHandler.TBBRAND, Code, WhereQuery, mAdapter, thumbnailPOJOList);
        return libView;
    }

    public void getsubPagelist(RecyclerView recyclerView, final RelativeLayout Ref_RelativeLayout, TBBRAND item, final ImageView selectedtextTextView) {

        final RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        final TextView pagecount = (TextView) gbRelativeLayout.getChildAt(1);
        final TextView page = (TextView) gbRelativeLayout.getChildAt(0);

        scrollforpage = 0;
        page.setText(ContentLibrary.this.getResources().getString(R.string.icon_page) + " Pages");
        page.setTypeface(font);
        pagecount.setTypeface(font);
        gbRelativeLayout.setVisibility(View.VISIBLE);
        List<ContentPage> thumbnailPOJOList_sub = new ArrayList<>();
        ThumbnailAdapterForPages mAdapter_sub = new ThumbnailAdapterForPages(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter_sub);
        final LinearLayoutManager layoutManagerpages = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerpages);
        preparePageData((RelativeLayout) recyclerView.getParent(), item, WhereQuery, mAdapter_sub, thumbnailPOJOList_sub);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManagerpages.getItemCount();
        pastVisiblesItems = layoutManagerpages.findFirstVisibleItemPosition();
        firstVisiblePosition = layoutManagerpages.findFirstVisibleItemPosition();
        findLastVisibleItemPosition = layoutManagerpages.findLastVisibleItemPosition();
        scrollforpage_scr = 0;
        if (totalItemCount == 1)
            pagecount.setText("1-1 of 1 Pages" + ContentLibrary.this.getResources().getString(R.string.cross));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = layoutManagerpages.getChildCount();
                totalItemCount = layoutManagerpages.getItemCount();
                pastVisiblesItems = layoutManagerpages.findFirstVisibleItemPosition();
                firstVisiblePosition = layoutManagerpages.findFirstVisibleItemPosition();
                findLastVisibleItemPosition = layoutManagerpages.findLastVisibleItemPosition();
                if (visibleItemCount != 0)
                    pagecount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + layoutManagerpages.getItemCount() + " Pages  " + ContentLibrary.this.getResources().getString(R.string.cross));
            }
        });


        pagecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedtextTextView.setVisibility(View.GONE);
                gbRelativeLayout.setVisibility(View.GONE);
                Ref_RelativeLayout.setVisibility(View.GONE);

            }
        });


    }

    public void getrefList(RecyclerView recyclerView, TBBRAND item) {

        RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        gbRelativeLayout.setVisibility(View.VISIBLE);
        scrollforref = 0;
        final TextView refHeader = (TextView) gbRelativeLayout.getChildAt(0);
        refHeader.setText(ContentLibrary.this.getResources().getString(R.string.icon_page) + " References");
        refHeader.setTypeface(font);
        final TextView refcount = (TextView) gbRelativeLayout.getChildAt(1);
        refcount.setTypeface(font);
        recyclerView.setVisibility(View.VISIBLE);
        // ref_sublistRelativeLayout.setVisibility(View.VISIBLE);
        List<RefrenceContent> thumbnailPOJOList_sub = new ArrayList<>();


        ThumbnailAdapterForRefrence mAdapter_ref = new ThumbnailAdapterForRefrence(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter_ref);
        final LinearLayoutManager layoutManagerRef = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerRef);
        prepareRefrneceData((RelativeLayout) recyclerView.getParent(), item, WhereQuery, mAdapter_ref, thumbnailPOJOList_sub);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (scrollforref == 0) {
                    visibleItemCount = layoutManagerRef.getChildCount();
                    totalItemCount = layoutManagerRef.getItemCount();
                    pastVisiblesItems = layoutManagerRef.findFirstVisibleItemPosition();
                    firstVisiblePosition = layoutManagerRef.findFirstVisibleItemPosition();
                    findLastVisibleItemPosition = layoutManagerRef.findLastVisibleItemPosition();
                    refcount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  ");
                    scrollforref = 1;
                }

                if (dx != 0) {
                    visibleItemCount = layoutManagerRef.getChildCount();
                    totalItemCount = layoutManagerRef.getItemCount();
                    pastVisiblesItems = layoutManagerRef.findFirstVisibleItemPosition();
                    firstVisiblePosition = layoutManagerRef.findFirstVisibleItemPosition();
                    findLastVisibleItemPosition = layoutManagerRef.findLastVisibleItemPosition();
                    if (visibleItemCount != 0)
                        refcount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  ");

                }
            }
        });


    }


    public void getSearchList(String whereQuery) {

        search_result.setVisibility(View.VISIBLE);
        recyclerView_search.setVisibility(View.VISIBLE);
        List<SearchData> thumbnailPOJOList_sub = new ArrayList<>();
        ThumbnailAdpForSearch mAdapter_search = new ThumbnailAdpForSearch(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
        recyclerView_search.setAdapter(mAdapter_search);


        prepareSerachData(DBHandler.TBDPG, whereQuery, mAdapter_search, thumbnailPOJOList_sub);


        recyclerView_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = layoutManager3.getChildCount();
                totalItemCount = layoutManager3.getItemCount();
                pastVisiblesItems = layoutManager3.findFirstVisibleItemPosition();
                firstVisiblePosition = layoutManager3.findFirstVisibleItemPosition();
                findLastVisibleItemPosition = layoutManager3.findLastVisibleItemPosition();
                //closepage_ref.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  " + ContentLibrary.this.getResources().getString(R.string.cross));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    getSearchList(s.toString());
                } else {
                    recyclerView_search.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                }

                return true;
            }
        });
        return true;


    }

    private void prepareRefrneceData(RelativeLayout recyclerView, TBBRAND itme, String whereQuery, ThumbnailAdapterForRefrence mAdapter, List<RefrenceContent> thumbnailPOJOList) {

        String Query = " select DISTINCT l.COL1,l.COL2,l.COL15  ,(\n" +
                "select k.col2  from TBDPG k\n" +
                "where k.col0 = l.col0\n" +
                ") pagename from TBDRG l\n" +
                "        where exists(\n" +
                "        select 1\n" +
                "        from TBDPS a\n" +
                "        where a.col9='" + itme.getCOL0() + "'\n" +
                "        and a.col3='" + itme.getCOL3() + "'" +
                "        and a.col10='IPL'\n" +
                "        and a.col5=l.col0)";

        RefrenceContent tbbrand;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                tbbrand = new RefrenceContent();

                tbbrand.setRefrenceName(cursor.getString(cursor.getColumnIndex("COL1")));
                tbbrand.setRefrenceCode(cursor.getString(cursor.getColumnIndex("COL2")));
                tbbrand.setCategory_code(cursor.getString(cursor.getColumnIndex("COL15")));
                tbbrand.setPagename_name(cursor.getString(cursor.getColumnIndex("pagename")));
                thumbnailPOJOList.add(tbbrand);
            } while (cursor.moveToNext());
        } else {
            recyclerView.setVisibility(View.GONE);
        }


        mAdapter.notifyDataSetChanged();
    }


    private void preparePageData(RelativeLayout relativeLayout, TBBRAND itme, String whereQuery, ThumbnailAdapterForPages mAdapter, List<ContentPage> thumbnailPOJOList) {

        String Query = "select  b.col1 imagepath, a.COL2, b.COL2 ,b.COl0,a.COL9 , a.COL1\n" +
                "        from  TBDPS a, TBDPG b\n" +
                "        where a.col5 = b.col0\n" +
                "        and a.col9 = '" + itme.getCOL0() + "'\n" +
                "        and a.col3 = '" + itme.getCOL3() + "'\n" +
                "        and a.col10 = 'IPL'\n" +
                "        and b.col8 = 'P'\n" +
                "        and b.col7 = '1'\n" +
                "         order by  CAST (a.col2 AS INTEGER) ASC \n";
        ContentPage contentPage;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                contentPage = new ContentPage();
                contentPage.setPageCode(cursor.getString(cursor.getColumnIndex("COL0")));
                contentPage.setPageName(cursor.getString(cursor.getColumnIndex("COL2")));
                contentPage.setCategory_code(cursor.getString(cursor.getColumnIndex("COL1")));
                contentPage.setCategory_name(cursor.getString(cursor.getColumnIndex("COL9")));
                contentPage.setImagepath(cursor.getString(cursor.getColumnIndex("imagepath")));
                thumbnailPOJOList.add(contentPage);
            } while (cursor.moveToNext());
        } else {
            relativeLayout.setVisibility(View.GONE);
        }


        mAdapter.notifyDataSetChanged();


    }

    private void prepareData(String TableName, String Code, String whereQuery, ThumbnailAdapterForContentLibrary mAdapter, List<TBBRAND> thumbnailPOJOList) {

        String Query = "";
        if (whereQuery.length() == 0)
            Query = "SELECT * FROM " + TableName + " where  COL0 = '" + Code + "'";
        else {
            Query = "SELECT * FROM " + TableName + " where COL0 ='" + Code + "' AND COL2 like '%" + whereQuery + "%'";
        }
        TBBRAND tbbrand;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                tbbrand = new TBBRAND();
                tbbrand.setCOL0(cursor.getString(cursor.getColumnIndex("COL0")));
                tbbrand.setCOL1(cursor.getString(cursor.getColumnIndex("COL1")));
                tbbrand.setCOL2(cursor.getString(cursor.getColumnIndex("COL2")));
                tbbrand.setCOL3(cursor.getString(cursor.getColumnIndex("COL3")));
                tbbrand.setCOL4(cursor.getString(cursor.getColumnIndex("COL4")));
                tbbrand.setCOL5(cursor.getString(cursor.getColumnIndex("COL5")));
                tbbrand.setCOL6(cursor.getString(cursor.getColumnIndex("COL6")));
                tbbrand.setCOL7(cursor.getString(cursor.getColumnIndex("COL7")));
                tbbrand.setCOL8(cursor.getString(cursor.getColumnIndex("COL8")));
                tbbrand.setCOL9(cursor.getString(cursor.getColumnIndex("COL9")));
                tbbrand.setCOL10(cursor.getString(cursor.getColumnIndex("COL10")));
                thumbnailPOJOList.add(tbbrand);
            } while (cursor.moveToNext());
        }


        mAdapter.notifyDataSetChanged();
    }

    private void prepareSerachData(String TableName, String whereQuery, ThumbnailAdpForSearch mAdapter, List<SearchData> thumbnailPOJOList) {


//        String Query = "select  b.col1 imagepath , b.COL2 ,b.COl0,a.COL9 , a.COL1  from  TBDPS a, TBDPG b\n" +
//                "    where a.col5 = b.col0\n" +
//                "    and a.col10 = 'IPL'\n" +
//                "     and b.col8 = 'P'\n" +
//                "   and b.col7='1'\n" +
//                "                and b.COL4 like '%" + whereQuery + "%'\n" +
//                "                     order by  CAST (a.col2 AS INTEGER) ASC ";

        String Query = "select  b.col1 IMAGEPATH , b.COL2 TITLE ,b.COl0 PAGECODE,a.COL9 CAT_TYPE, a.COL1 BRAND_CODE , b.col2  pagename , a.col4 cat_name , b.col8 " +
                "from  TBDPS a, TBDPG b where a.col5 = b.col0 and a.col10 = 'IPL' " +
                "and b.col8 = 'P' and b.col7='1' and b.col4 like '%" + whereQuery + "%' " +
                "union select  a.col1 imagepath  , " +
                " substr( a.col1, 1)   , a.COL2 ,  a.COL9 , a.COL1 , " +
                "(select b.col2 from  TBDPG b where b.col0= n.col5) " +
                "page_name,n.col4 cat_name , 'R' from TBDRG a, TBDPS n " +
                "where a.col0 = n.col5 and a.col6 = n.col3 and exists (select 1 from TBDPG g where g.col0 = a.col2 and  g.col8 = 'R' and g.col7='1' and g.col4 like '%" + whereQuery + "%')  order by 2 ASC";

        SearchData searchData;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                searchData = new SearchData();
                searchData.setImagePath(cursor.getString(cursor.getColumnIndex("IMAGEPATH")));
                searchData.setPageCode(cursor.getString(cursor.getColumnIndex("PAGECODE")));
                searchData.setPageNamee(cursor.getString(cursor.getColumnIndex("TITLE")));
                searchData.setCat_Type(cursor.getString(cursor.getColumnIndex("CAT_TYPE")));
                searchData.setBrand_code(cursor.getString(cursor.getColumnIndex("BRAND_CODE")));
                searchData.setSubpageName(cursor.getString(cursor.getColumnIndex("pagename")));
                searchData.setCat_Name(cursor.getString(cursor.getColumnIndex("cat_name")));


                thumbnailPOJOList.add(searchData);
            } while (cursor.moveToNext());
        } else {

        }

        if (cursor.getCount() != 0) {
            mAdapter.notifyDataSetChanged();
            String text = "Your search for <font color='#0a937f'>  " + whereQuery + " </font>  returned " + cursor.getCount() + " result(s)";
            search_result.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        } else {
            String text = "Your search for <font color='#0a937f'>  " + whereQuery + " </font>  returned " + cursor.getCount() + " result";
            search_result.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }


    @Override
    public void onItemClick(TBBRAND item, View v, int position) {

        try {

            RelativeLayout mainRelativeLayout = (RelativeLayout) v.getParent();

            RecyclerView recyclerView = (RecyclerView) mainRelativeLayout.getParent();
            RelativeLayout relativeLayout = (RelativeLayout) recyclerView.getParent();

            //Top level RecylerView
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();


            ImageView imageViewu = (ImageView) mainRelativeLayout.getChildAt(11);
            for (int i = 0; i < visibleItemCount; i++) {
                RelativeLayout parent = (RelativeLayout) recyclerView.getChildAt(i);
                TextView child = (TextView) parent.getChildAt(1);
                ImageView imageView = (ImageView) parent.getChildAt(11);
                imageView.setVisibility(View.GONE);
            }


            imageViewu.setVisibility(View.VISIBLE);


            RelativeLayout RelativeLayoutsd_Page = (RelativeLayout) relativeLayout.getChildAt(2);
            RelativeLayoutsd_Page.setVisibility(View.VISIBLE);
            RelativeLayout RelativeLayout_Ref = (RelativeLayout) relativeLayout.getChildAt(3);

            RecyclerView recyclerViewPage = (RecyclerView) RelativeLayoutsd_Page.getChildAt(2);
            recyclerViewPage.setVisibility(View.VISIBLE);

            getsubPagelist(recyclerViewPage, RelativeLayout_Ref, item, imageViewu);


            RecyclerView recyclerView_Ref = (RecyclerView) RelativeLayout_Ref.getChildAt(2);
            recyclerView_Ref.setVisibility(View.VISIBLE);
            getrefList(recyclerView_Ref, item);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRetryClick(TBBRAND item, View v, int position) {
        CallDownloadContainer(2, item.getCOL0(), item.getCOL3());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void CallDownloadContainer(int mode, String CATEGORYTYPE, String CATEGORYCODE) {
        sync = new Sync(ContentLibrary.this);
        sync.downloadContentUrl(ContentLibrary.this, mode, CATEGORYTYPE, CATEGORYCODE);
        dialog = new SweetAlertDialog(ContentLibrary.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading....");
        dialog.setContentText("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();
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

    void showContentDownloadDialog(List<IRCSFResponsePOJO> listP) {
        list = listP;
        content_dialog = new Dialog(ContentLibrary.this);
        content_dialog.getWindow();
        content_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        content_dialog.setContentView(R.layout.list);
        content_dialog.setCancelable(false);
        content_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.WHITE));
        Display display = ((WindowManager) ContentLibrary.this
                .getSystemService(ContentLibrary.this.WINDOW_SERVICE))
                .getDefaultDisplay();
        content_dialog.setCancelable(false);
        int width = display.getWidth();
        int height = display.getHeight();
        content_dialog.getWindow().setLayout((10 * width) / 12, (5 * height) / 8);
        content_dialog.setContentView(R.layout.list);
        TextView header = (TextView) content_dialog.findViewById(R.id.header);
        header.setText(getResources().getString(R.string.refresh) + " Content update available (" + list.size() + " update)");
        header.setTypeface(font);
        listView = (ListView) content_dialog.findViewById(R.id.list);
        listView.setAdapter(new ContentAdapter(ContentLibrary.this, list));

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
                    ContentLibrary.this.runOnUiThread(new Runnable() {
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
                            String directory = ContentLibrary.this.getFilesDir().getAbsolutePath()
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

                    if (!flag)
                        pojo.setSTATUS("Failed");
                    else
                        pojo.setSTATUS("Completed");
                    ContentLibrary.this.runOnUiThread(new Runnable() {
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
            if (content_dialog != null) {
                content_dialog.dismiss();
                dbHandler.UpdateTBRAND();
                defaultLayout();

            }

        }
    }

}
