//package inc.gb.cp20.ConatentLib;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.support.v7.widget.SearchView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import inc.gb.cp20.DB.DatabaseAdapter;
//import inc.gb.cp20.R;
//import inc.gb.cp20.RecylerView.DividerItemDecoration;
//import inc.gb.cp20.RecylerView.ThumbnailAdapter;
//import inc.gb.cp20.RecylerView.Thumbnail_POJO;
//import inc.gb.cp20.interfaces.RecyclerViewClickListener;
//
//
//public class ContentLibrary extends AppCompatActivity implements RecyclerViewClickListener {
//
//
//    private RecyclerView recyclerView, recyclerView_sub, recyclerView_ref, recyclerView_search;
//    private ThumbnailAdapter mAdapter, mAdapter_sub, mAdapter_ref, mAdapter_search;
//    private String Header = "ACA";
//    private String Cat_Code;
//    DatabaseAdapter databaseAdapter;
//    RelativeLayout sublistRelativeLayout, ref_sublistRelativeLayout;
//    String WhereQuery = "";
//    Typeface font;
//    TextView closepage, closepage_ref;
//    int pastVisiblesItems, visibleItemCount, totalItemCount;
//
//    int firstVisiblePosition = -1;
//    int findLastVisibleItemPosition = -1;
//    LinearLayoutManager layoutManager1, layoutManager2, layoutManager3;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.container_horizontal_recylerview);
//        font = Typeface.createFromAsset(this.getAssets(),
//                "fontawesome-webfont.ttf");
//        List<Thumbnail_POJO> thumbnailPOJOList = new ArrayList<>();
//
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_for_hr);
//        recyclerView_sub = (RecyclerView) findViewById(R.id.recycler_view_for_hr_sub);
//        recyclerView_ref = (RecyclerView) findViewById(R.id.recycler_view_for_hr_sub_ref);
//
//        recyclerView_search = (RecyclerView) findViewById(R.id.recycler_view_for_search);
//
//
//        layoutManager1 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager2 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager3 = new LinearLayoutManager(ContentLibrary.this, LinearLayoutManager.HORIZONTAL, false);
//
//        recyclerView_sub.setLayoutManager(layoutManager1);
//        recyclerView_ref.setLayoutManager(layoutManager2);
//        recyclerView_search.setLayoutManager(layoutManager3);
//
//        sublistRelativeLayout = (RelativeLayout) findViewById(R.id.sublist);
//        ref_sublistRelativeLayout = (RelativeLayout) findViewById(R.id.sublistfor_ref);
//
//        databaseAdapter = new DatabaseAdapter(this);
//        databaseAdapter.open();
//
//
//        TextView HeaderTextView = (TextView) findViewById(R.id.header);
//        HeaderTextView.setText(this.getResources().getString(R.string.threebar) + " " + Header);
//        HeaderTextView.setTypeface(font);
//
//
//        closepage = (TextView) findViewById(R.id.closepage);
//        closepage.setTypeface(font);
//        closepage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sublistRelativeLayout.setVisibility(View.GONE);
//                ref_sublistRelativeLayout.setVisibility(View.GONE);
//            }
//        });
//        closepage_ref = (TextView) findViewById(R.id.closepage_ref);
//        closepage_ref.setTypeface(font);
//        closepage_ref.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//
//        mAdapter = new ThumbnailAdapter(thumbnailPOJOList, this, getSupportFragmentManager(), this);
//        recyclerView_sub.addItemDecoration(new DividerItemDecoration(ContentLibrary.this, LinearLayoutManager.HORIZONTAL));
//
//        recyclerView.setHasFixedSize(true);
//
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//        prepareMovieData("TBBRND", "B", WhereQuery, mAdapter, thumbnailPOJOList);
//
//    }
//
//    public void getsublist() {
//
//
//        recyclerView_sub.setVisibility(View.VISIBLE);
//        sublistRelativeLayout.setVisibility(View.VISIBLE);
//        List<Thumbnail_POJO> thumbnailPOJOList_sub = new ArrayList<>();
//
//
//        mAdapter_sub = new ThumbnailAdapter(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
//        recyclerView_sub.setHasFixedSize(true);
//        recyclerView_sub.setItemAnimator(new DefaultItemAnimator());
//        recyclerView_sub.setAdapter(mAdapter_sub);
//        prepareMovieData("TBBRND", "K", WhereQuery, mAdapter_sub, thumbnailPOJOList_sub);
//
//
//        recyclerView_sub.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                visibleItemCount = layoutManager1.getChildCount();
//                totalItemCount = layoutManager1.getItemCount();
//                pastVisiblesItems = layoutManager1.findFirstVisibleItemPosition();
//                firstVisiblePosition = layoutManager1.findFirstVisibleItemPosition();
//                findLastVisibleItemPosition = layoutManager1.findLastVisibleItemPosition();
//                closepage.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " Pages  " + ContentLibrary.this.getResources().getString(R.string.cross));
//
//            }
//        });
//
//
//    }
//
////    public void getrefList() {
////
////
////        recyclerView_ref.setVisibility(View.VISIBLE);
////        ref_sublistRelativeLayout.setVisibility(View.VISIBLE);
////        List<Thumbnail_POJO> thumbnailPOJOList_sub = new ArrayList<>();
////
////
////        mAdapter_ref = new ThumbnailAdapter(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
////        recyclerView_ref.setHasFixedSize(true);
////        recyclerView_ref.setItemAnimator(new DefaultItemAnimator());
////        recyclerView_ref.setAdapter(mAdapter_ref);
////        prepareMovieData("TBBRND", "T", WhereQuery, mAdapter_ref, thumbnailPOJOList_sub);
////
////
////        recyclerView_ref.addOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////
////                visibleItemCount = layoutManager2.getChildCount();
////                totalItemCount = layoutManager2.getItemCount();
////                pastVisiblesItems = layoutManager2.findFirstVisibleItemPosition();
////                firstVisiblePosition = layoutManager2.findFirstVisibleItemPosition();
////                findLastVisibleItemPosition = layoutManager2.findLastVisibleItemPosition();
////                closepage_ref.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  ");
////            }
////        });
////
////
////    }
////
//
////    public void getSearchList(String whereQuery) {
////
////
////        recyclerView_search.setVisibility(View.VISIBLE);
////        List<Thumbnail_POJO> thumbnailPOJOList_sub = new ArrayList<>();
////
////
////        mAdapter_search = new ThumbnailAdapter(thumbnailPOJOList_sub, ContentLibrary.this, getSupportFragmentManager(), this);
////        recyclerView_search.setHasFixedSize(true);
////        recyclerView_search.setItemAnimator(new DefaultItemAnimator());
////        recyclerView_search.setAdapter(mAdapter_search);
////        prepareMovieData("TBBRND", "B", whereQuery, mAdapter_search, thumbnailPOJOList_sub);
////
////
////        recyclerView_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////
////                visibleItemCount = layoutManager3.getChildCount();
////                totalItemCount = layoutManager3.getItemCount();
////                pastVisiblesItems = layoutManager3.findFirstVisibleItemPosition();
////                firstVisiblePosition = layoutManager3.findFirstVisibleItemPosition();
////                findLastVisibleItemPosition = layoutManager3.findLastVisibleItemPosition();
////                //closepage_ref.setText((firstVisiblePosition + 1) + "-" + (findLastVisibleItemPosition + 1) + " of " + totalItemCount + " References  " + ContentLibrary.this.getResources().getString(R.string.cross));
////            }
////        });
////
////
////    }
//
//    //
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
//                if (!s.equals("")) {
//                    getSearchList(s.toString());
//                } else {
//                    recyclerView_search.setVisibility(View.GONE);
//                }
//
//                return true;
//            }
//        });
//        return true;
//
//
//    }
//
//    private void prepareMovieData(String TableName, String Code, String whereQuery, ThumbnailAdapter mAdapter, List<Thumbnail_POJO> thumbnailPOJOList) {
//
//        String Query = "";
//        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
//        databaseAdapter.open();
//
//        if (whereQuery.length() == 0)
//            Query = "SELECT * FROM " + TableName + " where  col_1 = '" + Code + "'";
//        else {
//            Query = "SELECT * FROM  " + TableName + " where col_1='" + Code + "' AND col_3 like '%" + whereQuery + "%'";
//        }
//
//        String[][] pdatat = databaseAdapter.genericSelect(Query, 10);
//
//        databaseAdapter.close();
//        Thumbnail_POJO thumbnailPOJO = null;
//
//        if (pdatat != null) {
//            for (int i = 0; i < pdatat.length; i++) {
//                thumbnailPOJO = new Thumbnail_POJO(pdatat[i][1], pdatat[i][2], pdatat[i][3], pdatat[i][4],
//                        pdatat[i][5], pdatat[i][6], pdatat[i][7], pdatat[i][8], pdatat[i][9]);
//                thumbnailPOJOList.add(thumbnailPOJO);
//
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//
//    }
//
//
//    @Override
//    public void recyclerViewListClicked(View v, int position) {
//
//        getsublist();
//        getrefList();
//    }
//}
