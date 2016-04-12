package inc.gb.cp20.RecylerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.ContentPage;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

public class ThumbnailAdapterForPages extends RecyclerView.Adapter<ThumbnailAdapterForPages.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;


    Bitmap bitmap;
    public static DBHandler dbHandler;

    public List<ContentPage> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;

    public ThumbnailAdapterForPages(List<ContentPage> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener) {
        this.brandList = brandList;
        ThumbnailAdapterForPages.mContext = mContext;
        this.fragmentManager = fragmentManager;
        ThumbnailAdapterForPages.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");

        dbHandler = DBHandler.getInstance(mContext);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, newtag;

        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            newtag = (TextView) view.findViewById(R.id.newtag);
            imageView = (ImageView) view.findViewById(R.id.bandimage);
        }

        public void bind(final ContentPage brandList, final int Position_) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent();
                    intent.putExtra("category_code", brandList.getCategory_code());
                    intent.putExtra("category_name", brandList.getCategory_name());
                    intent.putExtra("page_number", Position_ + "");
                    intent.putExtra("index_lib", "4");
                    ThumbnailAdapterForContentLibrary.postionStack = -1;
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
            });

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_item_pages, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContentPage tbbrand = brandList.get(position);
        holder.title.setText(tbbrand.getPageName());
        if (!tbbrand.getNewtag().equals("0")) {
            holder.newtag.setVisibility(View.INVISIBLE);
        } else {
            holder.newtag.setVisibility(View.VISIBLE);
        }

        String filePath = new File(mContext.getFilesDir() + "/" + FilenameUtils.removeExtension(tbbrand.getImagepath()) + "/", FilenameUtils.removeExtension(tbbrand.getImagepath()) + ".png").getAbsolutePath();
        bitmap = BitmapFactory.decodeFile(filePath);
        holder.imageView.setImageBitmap(bitmap);
        if (bitmap == null)
            holder.imageView.setImageResource(R.drawable.page);
        holder.bind(brandList.get(position), position);


    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}
