package inc.gb.cp20.ContentLib;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.ContentPage;
import inc.gb.cp20.Models.RefrenceContent;
import inc.gb.cp20.Models.SearchData;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.DividerItemDecoration;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForContentLibrary;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForPages;
import inc.gb.cp20.RecylerView.ThumbnailAdapterForRefrence;
import inc.gb.cp20.RecylerView.ThumbnailAdpForSearch;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;


public class ContentLibrary extends AppCompatActivity implements RecyclerViewClickListener {


    private RecyclerView recyclerView, recyclerView_sub, recyclerView_ref, recyclerView_search;


    RelativeLayout sublistRelativeLayout, ref_sublistRelativeLayout;
    String WhereQuery = "";
    Typeface font;
    int a = 0;
    LinearLayoutManager layoutManager_main_list;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int firstVisiblePosition = -1;
    int findLastVisibleItemPosition = -1;
    LinearLayoutManager layoutManager1, layoutManager2, layoutManager3;
    DBHandler dbHandler;
    LinearLayout content_view;


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
                    new LinearLayout.LayoutParams(params));
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

    public void getsubPagelist(RecyclerView recyclerView, final RelativeLayout Ref_RelativeLayout, TBBRAND item) {

        final RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        final TextView pagecount = (TextView) gbRelativeLayout.getChildAt(1);
        final TextView page = (TextView) gbRelativeLayout.getChildAt(0);
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

        visibleItemCount = layoutManagerpages.getChildCount();
        totalItemCount = layoutManagerpages.getItemCount();
        pastVisiblesItems = layoutManagerpages.findFirstVisibleItemPosition();
        firstVisiblePosition = layoutManagerpages.findFirstVisibleItemPosition();
        findLastVisibleItemPosition = layoutManagerpages.findLastVisibleItemPosition();
        pagecount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + layoutManagerpages.getItemCount() + " Pages  " + ContentLibrary.this.getResources().getString(R.string.cross));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = layoutManagerpages.getChildCount();
                totalItemCount = layoutManagerpages.getItemCount();
                pastVisiblesItems = layoutManagerpages.findFirstVisibleItemPosition();
                firstVisiblePosition = layoutManagerpages.findFirstVisibleItemPosition();
                findLastVisibleItemPosition = layoutManagerpages.findLastVisibleItemPosition();
                pagecount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + layoutManagerpages.getItemCount() + " Pages  " + ContentLibrary.this.getResources().getString(R.string.cross));

            }
        });


        pagecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gbRelativeLayout.setVisibility(View.GONE);
                Ref_RelativeLayout.setVisibility(View.GONE);

            }
        });


    }

    public void getrefList(RecyclerView recyclerView, TBBRAND item) {

        RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        gbRelativeLayout.setVisibility(View.VISIBLE);

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

                visibleItemCount = layoutManagerRef.getChildCount();
                totalItemCount = layoutManagerRef.getItemCount();
                pastVisiblesItems = layoutManagerRef.findFirstVisibleItemPosition();
                firstVisiblePosition = layoutManagerRef.findFirstVisibleItemPosition();
                findLastVisibleItemPosition = layoutManagerRef.findLastVisibleItemPosition();
                refcount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  ");
            }
        });


    }


    public void getSearchList(String whereQuery) {


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
                }

                return true;
            }
        });
        return true;


    }

    private void prepareRefrneceData(RelativeLayout recyclerView, TBBRAND itme, String whereQuery, ThumbnailAdapterForRefrence mAdapter, List<RefrenceContent> thumbnailPOJOList) {

        String Query = " select l.COL1,l.COL2,l.COL15  from TBDRG l\n" +
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
                thumbnailPOJOList.add(tbbrand);
            } while (cursor.moveToNext());
        } else {
            recyclerView.setVisibility(View.GONE);
        }


        mAdapter.notifyDataSetChanged();
    }


    private void preparePageData(RelativeLayout relativeLayout, TBBRAND itme, String whereQuery, ThumbnailAdapterForPages mAdapter, List<ContentPage> thumbnailPOJOList) {

        String Query = "";

        Query = "select  b.col1 imagepath, a.COL2, b.COL2 ,b.COl0,a.COL9 , a.COL1\n" +
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


        String Query = "select  b.col1 imagepath , b.COL2 ,b.COl0,a.COL9 , a.COL1  from  TBDPS a, TBDPG b\n" +
                "    where a.col5 = b.col0\n" +
                "    and a.col10 = 'IPL'\n" +
                "     and b.col8 = 'P'\n" +
                "   and b.col7='1'\n" +
                "                and b.COL4 like '%" + whereQuery + "%'\n" +
                "                     order by  CAST (a.col2 AS INTEGER) ASC ";

        SearchData searchData;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                searchData = new SearchData();
                searchData.setPageCode(cursor.getString(cursor.getColumnIndex("COL0")));
                searchData.setPageNamee(cursor.getString(cursor.getColumnIndex("COL2")));
                searchData.setCat_Type(cursor.getString(cursor.getColumnIndex("COL9")));
                searchData.setBrand_code(cursor.getString(cursor.getColumnIndex("COL1")));
                searchData.setImagePath(cursor.getString(cursor.getColumnIndex("imagepath")));
                thumbnailPOJOList.add(searchData);
            } while (cursor.moveToNext());
        }

        if (cursor.getCount() != 0)
            mAdapter.notifyDataSetChanged();
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

            TextView title = (TextView) mainRelativeLayout.getChildAt(1);
            for (int i = 0; i < visibleItemCount; i++) {
                RelativeLayout parent = (RelativeLayout) recyclerView.getChildAt(i);
                TextView child = (TextView) parent.getChildAt(1);
                child.setTextColor(Color.parseColor("#424242"));
            }

            title.setTextColor(Color.parseColor("#FF0000"));


            RelativeLayout RelativeLayoutsd_Page = (RelativeLayout) relativeLayout.getChildAt(2);
            RelativeLayoutsd_Page.setVisibility(View.VISIBLE);
            RelativeLayout RelativeLayout_Ref = (RelativeLayout) relativeLayout.getChildAt(3);

            RecyclerView recyclerViewPage = (RecyclerView) RelativeLayoutsd_Page.getChildAt(2);
            recyclerViewPage.setVisibility(View.VISIBLE);

            getsubPagelist(recyclerViewPage, RelativeLayout_Ref, item);


            RecyclerView recyclerView_Ref = (RecyclerView) RelativeLayout_Ref.getChildAt(2);
            recyclerView_Ref.setVisibility(View.VISIBLE);
            getrefList(recyclerView_Ref, item);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRetryClick(TBBRAND item, View v, int position) {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
