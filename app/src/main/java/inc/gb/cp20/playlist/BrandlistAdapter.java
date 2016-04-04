package inc.gb.cp20.playlist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.R;

/**
 * Created by Shubham on 3/9/16.
 */
public class BrandlistAdapter extends RecyclerView.Adapter<BrandlistAdapter.NewViewHolder> {

    ArrayList<String[]> gridData;
    ArrayList<String[]> gridDataForRecycler;
    Context mContext;
    DBHandler dbHandler;
    Typeface font;

    public class NewViewHolder extends RecyclerView.ViewHolder {
        public TextView PageName, fb, pageCount, refCount, threedotMenu, newtag;
        ImageView PageIcon, pagesCountIcon, custom, refCountIcon;
        RelativeLayout layout;
        ScrollView childScrollView;


        public NewViewHolder(View view) {
            super(view);
            PageName = (TextView) view.findViewById(R.id.title);
            PageIcon = (ImageView) view.findViewById(R.id.bandimage);
            refCount = (TextView) view.findViewById(R.id.refcount);
            threedotMenu = (TextView) view.findViewById(R.id.threedot);
            refCountIcon = (ImageView) itemView.findViewById(R.id.ref);
            layout = (RelativeLayout) view.findViewById(R.id.layoutsc);
            childScrollView = (ScrollView) view.findViewById(R.id.childScroll);
            newtag = (TextView) view.findViewById(R.id.newtag);
            fb = (TextView) view.findViewById(R.id.close);
        }
    }

    public BrandlistAdapter(Context mContext, ArrayList<String[]> gridData, ArrayList<String[]> gridDataForRecycler) {
        this.mContext = mContext;
        this.gridData = gridData;
        this.gridDataForRecycler = gridDataForRecycler;
        dbHandler = DBHandler.getInstance(mContext);
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbail_item_library, parent, false);
        itemView.setOnLongClickListener(new MyOnLongClickListener());
        itemView.setOnDragListener(
                new MyDragListener(mContext, gridData, gridDataForRecycler));
        return new NewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewViewHolder holder, int position) {
        final String[] str = gridData.get(position);
        holder.PageName.setText(str[3]);
        String filePath = new File(mContext.getFilesDir() + "/" + FilenameUtils.removeExtension(str[2]) + "/", FilenameUtils.removeExtension(str[2]) + ".png").getAbsolutePath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.PageIcon.setImageBitmap(bitmap);
        holder.fb.setTypeface(font);


        holder.refCountIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.childScrollView.setVisibility(View.VISIBLE);
                holder.layout.removeAllViews();
                holder.fb.setVisibility(View.VISIBLE);
                String Query = " Select * from TBDRG where col0='" + str[0] + "'";

                String[][] pagename = dbHandler.genericSelect(Query, 3);
                if (pagename != null) {
                    int prevTextViewId = 0;
                    for (int i = 0; i < pagename.length; i++) {
                        final TextView textView = new TextView(mContext);
                        String temp = "";
                        if (i < 10) {
                            if (i == 9)
                                temp = "0" + (i);
                            else
                                temp = "0" + (i + 1);
                        } else {
                            temp = "" + (i + 1);
                        }


                        textView.setText(temp + " " + pagename[i][2]);
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
                        holder.layout.addView(textView, params);
                    }
                }
            }
        });
        holder.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.childScrollView.setVisibility(View.GONE);
                holder.fb.setVisibility(View.GONE);
            }
        });

        if (Integer.parseInt(str[8]) > 0) {
            holder.refCountIcon.setVisibility(View.VISIBLE);
            holder.refCount.setVisibility(View.VISIBLE);
            holder.refCount.setText(str[8]);
        } else {
            holder.refCountIcon.setVisibility(View.INVISIBLE);
            holder.refCount.setVisibility(View.INVISIBLE);
        }

        try {
            if (!str[9].equals("1"))
                holder.newtag.setVisibility(View.VISIBLE);
            else
                holder.newtag.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return gridData.size();
    }

    public void remove(int position) {
        gridData.remove(position);
        notifyItemRemoved(position);
    }

    public void swap(int firstPosition, int secondPosition) {
        Collections.swap(gridData, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }

    public class MyOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
//            int itemPosition = recyclerView.getChildPosition(v);
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        }
    }
}
