package inc.gb.cp20.RecylerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

/**
 * Created by GB on 2/23/16.
 */
public class HorizontalRecylerView {

    Context mContext;
    private List<TBBRAND> thumbnailPOJOList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ThumbnailAdapter mAdapter;
    private String Header;
    private String Cat_Code;
    DBHandler dbHandler;

    String WhereQuery;
    Typeface font;
    View second;
    RecyclerViewClickListener recyclerViewClickListener;

    public HorizontalRecylerView(Context mContext, String Header, String Cat_Code, String WhereQuery, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = mContext;
        this.Header = Header;
        this.Cat_Code = Cat_Code;
        dbHandler = DBHandler.getInstance(mContext);
        this.WhereQuery = WhereQuery;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
        this.recyclerViewClickListener = recyclerViewClickListener;

    }

    public View getHorizontalRecylerView(android.support.v4.app.FragmentManager manager) {
        View mRecylerView = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        mRecylerView = inflater.inflate(R.layout.horizontalscrollrecylerview, null);
        recyclerView = (RecyclerView) mRecylerView.findViewById(R.id.recycler_view_for_hr);


        second = mRecylerView.findViewById(R.id.secondrec);

        TextView HeaderTextView = (TextView) mRecylerView.findViewById(R.id.header);
        HeaderTextView.setText(mContext.getResources().getString(R.string.threebar) + " " + Header );
        HeaderTextView.setTypeface(font);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ThumbnailAdapter(thumbnailPOJOList, mContext, manager, recyclerViewClickListener, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData(Cat_Code, WhereQuery);

        return mRecylerView;
    }

    private void prepareMovieData(String Code, String whereQuery) {

        String Query = "";


        if (whereQuery.length() == 0)
            Query = "SELECT * FROM TBBRAND where  COL0 = '" + Code + "'";
        else {
            Query = "SELECT * FROM TBBRAND where COL0 ='" + Code + "' AND COL2 like '%" + whereQuery + "%'";
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
                tbbrand.setCOL11(cursor.getString(cursor.getColumnIndex("COL11")));
                tbbrand.setCOL12(cursor.getString(cursor.getColumnIndex("COL12")));
                tbbrand.setCOL13(cursor.getString(cursor.getColumnIndex("COL13")));
                tbbrand.setCOL14(cursor.getString(cursor.getColumnIndex("COL14")));

                thumbnailPOJOList.add(tbbrand);
            } while (cursor.moveToNext());
        }


        mAdapter.notifyDataSetChanged();
    }

}



