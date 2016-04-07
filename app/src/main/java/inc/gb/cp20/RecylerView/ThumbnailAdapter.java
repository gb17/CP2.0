package inc.gb.cp20.RecylerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;

    public static DBHandler dbHandler;

    public List<TBBRAND> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;

    static int index;

    static int pageflag = -1;

    public ThumbnailAdapter(List<TBBRAND> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener, int index) {
        this.brandList = brandList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        this.index = index;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
        dbHandler = DBHandler.getInstance(mContext);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, fb, pageCount, refCount, newTag, downloadTextView;

        ImageView imageView, pageFloatingActionButton, refFloatingActionButton;
        RelativeLayout layout, pageCountLayout, refCountLayout;
        ScrollView childScrollView;
        RelativeLayout masklayRelativeLayout;
        LinearLayout retryLinearLayout;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);

            layout = (RelativeLayout) view.findViewById(R.id.layoutsc);
            fb = (TextView) view.findViewById(R.id.close);
            childScrollView = (ScrollView) view.findViewById(R.id.childScroll);

            pageFloatingActionButton = (ImageView) view.findViewById(R.id.ref);
            refFloatingActionButton = (ImageView) view.findViewById(R.id.page);

            //Chnages made to swtich page to refrence
            pageCount = (TextView) view.findViewById(R.id.refcount);
            refCount = (TextView) view.findViewById(R.id.pagecount);

            newTag = (TextView) view.findViewById(R.id.newtag);

            pageCountLayout = (RelativeLayout) view.findViewById(R.id.pagelayout);
            refCountLayout = (RelativeLayout) view.findViewById(R.id.reflayout);


            //For Download Content

            downloadTextView = (TextView) view.findViewById(R.id.download);
            masklayRelativeLayout = (RelativeLayout) view.findViewById(R.id.masklay);
            retryLinearLayout = (LinearLayout) view.findViewById(R.id.retry);


            fb.setTypeface(font);
            downloadTextView.setTypeface(font);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childScrollView.setVisibility(View.GONE);
                    fb.setVisibility(View.GONE);
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

        public void bind(final TBBRAND brandList, final int total) {
            if (index == 1) {
                pageFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        pageflag = getLayoutPosition();


                        childScrollView.setVisibility(View.VISIBLE);
                        fb.setVisibility(View.VISIBLE);
                        layout.removeAllViews();
                        String[][] pagename = dbHandler.genericSelect("select b.COL2 from TBDPS a , TBDPG b where a.col5 = b.col0 and a.col3 =  '" + brandList.getCOL3() + "' and a.COL9 = '" + brandList.getCOL0() + "'   and a.COL10 = 'IPL'", 1);
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


                                textView.setText(temp + " " + pagename[i][0]);
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
                                layout.addView(textView, params);
                            }
                        }

                    }
                });


                refFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        childScrollView.setVisibility(View.VISIBLE);
                        layout.removeAllViews();
                        fb.setVisibility(View.VISIBLE);
                        String Query = " select DISTINCT l.COL1,l.COL2,l.COL15  from TBDRG l\n" +
                                "        where exists(\n" +
                                "        select 1\n" +
                                "        from TBDPS a\n" +
                                "        where a.col9='" + brandList.getCOL0() + "'\n" +
                                "        and a.col3='" + brandList.getCOL3() + "'" +
                                "        and a.col10='IPL'\n" +
                                "        and a.col5=l.col0)";

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


                                textView.setText(temp + " " + pagename[i][0]);
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
                                layout.addView(textView, params);
                            }
                        }
                    }
                });

            }
        }

        public void bind(final TBBRAND brandList, final RecyclerViewClickListener listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(brandList, v, getLayoutPosition());
                }
            });

            retryLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRetryClick(brandList, v, getLayoutPosition());
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
        TBBRAND tbbrand = brandList.get(position);
        holder.title.setText(tbbrand.getCOL2());
//Retry
        if (!tbbrand.getCOL10().equals("0"))
            holder.masklayRelativeLayout.setVisibility(View.VISIBLE);
        else {
            holder.masklayRelativeLayout.setVisibility(View.GONE);
        }


        if (tbbrand.getCOL5().equals("0") || tbbrand.getCOL5().equals("")) {
            holder.pageCount.setText("");
            holder.pageFloatingActionButton.setVisibility(View.INVISIBLE);
        } else {
            holder.pageCount.setText(tbbrand.getCOL5());
            holder.pageFloatingActionButton.setVisibility(View.VISIBLE);
        }
        //For REFRENCE
        if (tbbrand.getCOL9().equals("0") || tbbrand.getCOL9().equals("")) {
            holder.refCount.setText("");
            holder.refFloatingActionButton.setVisibility(View.INVISIBLE);
        } else {
            holder.refCount.setText(tbbrand.getCOL9());
            holder.refFloatingActionButton.setVisibility(View.VISIBLE);
        }
        try {
            String filePath = new File(mContext.getFilesDir() + "/", tbbrand.getCOL4() + ".png").getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            holder.imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            //holder.imageView.setI
        }

        //For New Tag
        if (tbbrand.getCOL11().equals("0")) {
            holder.newTag.setVisibility(View.INVISIBLE);
        } else {
            holder.newTag.setVisibility(View.VISIBLE);
        }

        //For PAGE Layout
//        if (pageflag == position) {
//            holder.childScrollView.setVisibility(View.VISIBLE);
//            holder.fb.setVisibility(View.VISIBLE);
//        } else {
//            holder.childScrollView.setVisibility(View.GONE);
//            holder.fb.setVisibility(View.GONE);
//        }
        holder.bind(brandList.get(position), itemListener);
        holder.bind(brandList.get(position), getItemCount());


    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}
