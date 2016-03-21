package inc.gb.cp20.ContentLib;

import android.content.Context;
import android.database.Cursor;
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
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.Models.TBDPG;
import inc.gb.cp20.R;
import inc.gb.cp20.RecylerView.DividerItemDecoration;
import inc.gb.cp20.RecylerView.ThumbnailAdapter;
import inc.gb.cp20.RecylerView.ThumbnailAdpForSearch;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;


public class ContentLibrary extends AppCompatActivity implements RecyclerViewClickListener {


    private RecyclerView recyclerView, recyclerView_sub, recyclerView_ref, recyclerView_search;
    private ThumbnailAdapter mAdapter_sub, mAdapter_ref;

    private String Cat_Code;

    RelativeLayout sublistRelativeLayout, ref_sublistRelativeLayout;
    String WhereQuery = "";
    Typeface font;
    int a = 0;

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
        layoutManager1 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_sub.setLayoutManager(layoutManager1);
        recyclerView_ref.setLayoutManager(layoutManager2);


        sublistRelativeLayout = (RelativeLayout) libView.findViewById(R.id.sublist);
        ref_sublistRelativeLayout = (RelativeLayout) libView.findViewById(R.id.sublistfor_ref);


        TextView HeaderTextView = (TextView) libView.findViewById(R.id.header);
        HeaderTextView.setText(this.getResources().getString(R.string.threebar) + " " + Header);
        HeaderTextView.setTypeface(font);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ThumbnailAdapter mAdapter = new ThumbnailAdapter(thumbnailPOJOList, this, getSupportFragmentManager(), this);
        recyclerView_sub.addItemDecoration(new DividerItemDecoration(ContentLibrary.this, LinearLayoutManager.HORIZONTAL));

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData(DBHandler.TBBRAND, Code, WhereQuery, mAdapter, thumbnailPOJOList);
        return libView;
    }

    public void getsubPagelist(RecyclerView recyclerView, final RelativeLayout Ref_RelativeLayout) {

        final RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        final TextView pagecount = (TextView) gbRelativeLayout.getChildAt(0);
        pagecount.setTypeface(font);
        gbRelativeLayout.setVisibility(View.VISIBLE);
        List<TBBRAND> thumbnailPOJOList_sub = new ArrayList<>();
        ThumbnailAdapter mAdapter_sub = new ThumbnailAdapter(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter_sub);
        final LinearLayoutManager layoutManagerpages = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerpages);
        prepareData(DBHandler.TBBRAND, "B", WhereQuery, mAdapter_sub, thumbnailPOJOList_sub);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (a == 0) {
                    visibleItemCount = layoutManagerpages.getChildCount();
                    totalItemCount = layoutManagerpages.getItemCount();
                    pastVisiblesItems = layoutManagerpages.findFirstVisibleItemPosition();
                    firstVisiblePosition = layoutManagerpages.findFirstVisibleItemPosition();
                    findLastVisibleItemPosition = layoutManagerpages.findLastVisibleItemPosition();
                    pagecount.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + layoutManagerpages.getItemCount() + " Pages  " + ContentLibrary.this.getResources().getString(R.string.cross));
                    a = 1;
                } else {
                    a = 0;
                }
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

    public void getrefList(RecyclerView recyclerView) {

        RelativeLayout gbRelativeLayout = (RelativeLayout) recyclerView.getParent();
        gbRelativeLayout.setVisibility(View.VISIBLE);
        final TextView refcount = (TextView) gbRelativeLayout.getChildAt(0);
        refcount.setTypeface(font);
        recyclerView.setVisibility(View.VISIBLE);
        // ref_sublistRelativeLayout.setVisibility(View.VISIBLE);
        List<TBBRAND> thumbnailPOJOList_sub = new ArrayList<>();


        ThumbnailAdapter mAdapter_ref = new ThumbnailAdapter(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter_ref);
        final LinearLayoutManager layoutManagerRef = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerRef);
        prepareData(DBHandler.TBBRAND, "T", WhereQuery, mAdapter_ref, thumbnailPOJOList_sub);


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
        List<TBDPG> thumbnailPOJOList_sub = new ArrayList<>();
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

    private void prepareData(String TableName, String Code, String whereQuery, ThumbnailAdapter mAdapter, List<TBBRAND> thumbnailPOJOList) {

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

    private void prepareSerachData(String TableName, String whereQuery, ThumbnailAdpForSearch mAdapter, List<TBDPG> thumbnailPOJOList) {

        String Query = "";

        Query = " Select  a.*,b.COL2 from TBDPS a, TBDPG b where a.col5 = b.col0 and a.COL10 = 'IPL'  and b.COL7 ='0' and b.COL4 like '%" + whereQuery + "%'";

        TBDPG tbdpg;
        Cursor cursor = dbHandler.getCusrsor(Query);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            do {
                tbdpg = new TBDPG();
                tbdpg.setCOL0(cursor.getString(cursor.getColumnIndex("COL0")));
                tbdpg.setCOL1(cursor.getString(cursor.getColumnIndex("COL1")));
                tbdpg.setCOL2(cursor.getString(cursor.getColumnIndex("COL2")));
                tbdpg.setCOL3(cursor.getString(cursor.getColumnIndex("COL3")));
                tbdpg.setCOL4(cursor.getString(cursor.getColumnIndex("COL4")));
                tbdpg.setCOL5(cursor.getString(cursor.getColumnIndex("COL5")));
                tbdpg.setCOL6(cursor.getString(cursor.getColumnIndex("COL6")));
                tbdpg.setCOL7(cursor.getString(cursor.getColumnIndex("COL7")));
                tbdpg.setCOL8(cursor.getString(cursor.getColumnIndex("COL8")));
                tbdpg.setCOL9(cursor.getString(cursor.getColumnIndex("COL9")));
                tbdpg.setCOL10(cursor.getString(cursor.getColumnIndex("COL10")));
                thumbnailPOJOList.add(tbdpg);
            } while (cursor.moveToNext());
        }


        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(TBBRAND item, View v, int position) {

        try {
            RelativeLayout mainRelativeLayout = (RelativeLayout) v.getParent();

            RecyclerView recyclerView = (RecyclerView) mainRelativeLayout.getParent();
            RelativeLayout relativeLayout = (RelativeLayout) recyclerView.getParent();


            RelativeLayout RelativeLayoutsd_Page = (RelativeLayout) relativeLayout.getChildAt(2);
            RelativeLayout RelativeLayout_Ref = (RelativeLayout) relativeLayout.getChildAt(3);
            RecyclerView recyclerViewPage = (RecyclerView) RelativeLayoutsd_Page.getChildAt(1);
            recyclerViewPage.setVisibility(View.VISIBLE);
            getsubPagelist(recyclerViewPage, RelativeLayout_Ref);


            RecyclerView recyclerView_Ref = (RecyclerView) RelativeLayout_Ref.getChildAt(1);
            recyclerView_Ref.setVisibility(View.VISIBLE);
            getrefList(recyclerView_Ref);
        } catch (Exception e) {

        }

    }
}