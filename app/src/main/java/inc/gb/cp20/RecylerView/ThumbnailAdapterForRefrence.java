package inc.gb.cp20.RecylerView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.RefrenceContent;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

public class ThumbnailAdapterForRefrence extends RecyclerView.Adapter<ThumbnailAdapterForRefrence.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;

    public static DBHandler dbHandler;

    public List<RefrenceContent> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;

    public ThumbnailAdapterForRefrence(List<RefrenceContent> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener) {
        this.brandList = brandList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");

        dbHandler = DBHandler.getInstance(mContext);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);
        }

        public void bind(final RefrenceContent brandList, final RecyclerViewClickListener listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // listener.onItemClick(brandList, v, getLayoutPosition());
                }
            });

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_item_refrnce, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RefrenceContent tbbrand = brandList.get(position);
        holder.title.setText(tbbrand.getRefrenceName());

        holder.imageView.setImageResource(R.drawable.pdf);

        holder.bind(brandList.get(position), itemListener);


    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}
