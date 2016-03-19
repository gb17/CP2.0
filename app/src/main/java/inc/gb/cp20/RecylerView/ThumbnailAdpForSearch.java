package inc.gb.cp20.RecylerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import inc.gb.cp20.Models.TBDPG;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

/**
 * Created by GB on 3/18/16.
 */
public class ThumbnailAdpForSearch extends RecyclerView.Adapter<ThumbnailAdpForSearch.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;
    public List<TBDPG> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;
    Integer Imagearr[] = {R.drawable.dempi, R.drawable.newace, R.drawable.newcfix1, R.drawable.newcfix3, R.drawable.newjade
            , R.drawable.newmezzo, R.drawable.newstillsep, R.drawable.solsuna, R.drawable.stelpep, R.drawable.zepine, R.drawable.stelpep, R.drawable.zepine,
            R.drawable.dempi, R.drawable.newace, R.drawable.newcfix1, R.drawable.newcfix3, R.drawable.newjade
            , R.drawable.newmezzo, R.drawable.newstillsep, R.drawable.solsuna, R.drawable.stelpep, R.drawable.zepine, R.drawable.stelpep, R.drawable.zepine};

    public ThumbnailAdpForSearch(List<TBDPG> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener) {
        this.brandList = brandList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, fb, pageCount, refCount, newTag;
        FloatingActionButton refFloatingActionButton, pageFloatingActionButton;
        ImageView imageView;
        RelativeLayout layout, pageCountLayout, refCountLayout;
        ScrollView childScrollView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);
            refFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.ref);
            layout = (RelativeLayout) view.findViewById(R.id.layoutsc);
            fb = (TextView) view.findViewById(R.id.close);
            childScrollView = (ScrollView) view.findViewById(R.id.childScroll);
            pageFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.page);
            pageCount = (TextView) view.findViewById(R.id.pagecount);
            refCount = (TextView) view.findViewById(R.id.refcount);
            newTag = (TextView) view.findViewById(R.id.newtag);

            pageCountLayout = (RelativeLayout) view.findViewById(R.id.pagelayout);
            pageCountLayout.setVisibility(View.GONE);
            refCountLayout = (RelativeLayout) view.findViewById(R.id.reflayout);
            refCountLayout.setVisibility(View.GONE);


            refFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            fb.setTypeface(font);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childScrollView.setVisibility(View.GONE);
                    fb.setVisibility(View.GONE);
                }
            });

            pageFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childScrollView.setVisibility(View.VISIBLE);
                    fb.setVisibility(View.VISIBLE);


                    int prevTextViewId = 0;
                    for (int i = 0; i < 15; i++) {
                        final TextView textView = new TextView(mContext);
                        textView.setText(i + "  Page Name");
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
                }
            });
            childScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of  child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tumbnail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TBDPG tbdpg = brandList.get(position);
        holder.title.setText(tbdpg.getCOL2());
        if (position >= Imagearr.length - 1)
            holder.imageView.setImageResource(R.drawable.dempi);
        else {
            holder.imageView.setImageResource(Imagearr[position]);
        }
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}

