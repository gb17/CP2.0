package inc.gb.cp20.RecylerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ThumbnailAdapterForContentLibrary extends RecyclerView.Adapter<ThumbnailAdapterForContentLibrary.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;

    public static DBHandler dbHandler;

    public List<TBBRAND> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;
    static int index;
    public static int postionStack = -1;
    static int all_postion = 0;


    public ThumbnailAdapterForContentLibrary(List<TBBRAND> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener, int index) {
        this.brandList = brandList;
        ThumbnailAdapterForContentLibrary.mContext = mContext;
        this.fragmentManager = fragmentManager;
        ThumbnailAdapterForContentLibrary.itemListener = itemListener;
        ThumbnailAdapterForContentLibrary.index = index;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");

        dbHandler = DBHandler.getInstance(mContext);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, fb, pageCount, refCount, newTag, downloadTextView;

        ImageView imageView, pageFloatingActionButton, refFloatingActionButton, greendropdown;
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

            greendropdown = (ImageView) view.findViewById(R.id.greendropdown);


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

        public void bind(final TBBRAND brandList, final RecyclerViewClickListener listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postionStack = getLayoutPosition();
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
                .inflate(R.layout.tumbnail_item_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TBBRAND tbbrand = brandList.get(position);

        if (!tbbrand.getCOL10().equals("0"))
            holder.masklayRelativeLayout.setVisibility(View.VISIBLE);
        else {
            holder.masklayRelativeLayout.setVisibility(View.GONE);
        }

        holder.title.setText(tbbrand.getCOL2());
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


        //For New Tag
        if (tbbrand.getCOL11().equals("0")) {
            holder.newTag.setVisibility(View.INVISIBLE);
        } else {
            holder.newTag.setVisibility(View.VISIBLE);
        }
        try {
            String filePath = new File(mContext.getFilesDir() + "/", tbbrand.getCOL4() + ".png").getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if(bitmap!=null)
                holder.imageView.setImageBitmap(bitmap);
            else{
                if (tbbrand.getCOL0().equals("B"))
                    holder.imageView.setImageResource(R.drawable.brand);
                else if (tbbrand.getCOL0().equals("S"))
                    holder.imageView.setImageResource(R.drawable.speciality);
                else if (tbbrand.getCOL0().equals("T"))
                    holder.imageView.setImageResource(R.drawable.therapy);
                else if (tbbrand.getCOL0().equals("C"))
                    holder.imageView.setImageResource(R.drawable.corporate);
                else if (tbbrand.getCOL0().equals("SR"))
                    holder.imageView.setImageResource(R.drawable.services);
            }

        } catch (Exception e) {
            if (tbbrand.getCOL0().equals("B"))
                holder.imageView.setImageResource(R.drawable.brand);
            else if (tbbrand.getCOL0().equals("S"))
                holder.imageView.setImageResource(R.drawable.speciality);
            else if (tbbrand.getCOL0().equals("T"))
                holder.imageView.setImageResource(R.drawable.therapy);
            else if (tbbrand.getCOL0().equals("C"))
                holder.imageView.setImageResource(R.drawable.corporate);
            else if (tbbrand.getCOL0().equals("SR"))
                holder.imageView.setImageResource(R.drawable.services);
        }
        holder.bind(brandList.get(position), itemListener);
        if (postionStack == position) {
            holder.greendropdown.setVisibility(View.VISIBLE);
        } else {
            holder.greendropdown.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}
