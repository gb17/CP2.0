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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

import inc.gb.cp20.Models.SearchData;
import inc.gb.cp20.R;
import inc.gb.cp20.interfaces.RecyclerViewClickListener;

/**
 * Created by GB on 3/18/16.
 */
public class ThumbnailAdpForSearch extends RecyclerView.Adapter<ThumbnailAdpForSearch.MyViewHolder> {

    private static RecyclerViewClickListener itemListener;
    public List<SearchData> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;

    Bitmap bitmap;
    Integer Imagearr[] = {R.drawable.dempi, R.drawable.newace, R.drawable.newcfix1, R.drawable.newcfix3, R.drawable.newjade
            , R.drawable.newmezzo, R.drawable.newstillsep, R.drawable.solsuna, R.drawable.stelpep, R.drawable.zepine, R.drawable.stelpep, R.drawable.zepine,
            R.drawable.dempi, R.drawable.newace, R.drawable.newcfix1, R.drawable.newcfix3, R.drawable.newjade
            , R.drawable.newmezzo, R.drawable.newstillsep, R.drawable.solsuna, R.drawable.stelpep, R.drawable.zepine, R.drawable.stelpep, R.drawable.zepine};

    public ThumbnailAdpForSearch(List<SearchData> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener) {
        this.brandList = brandList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, fb, pageCount, refCount, newTag;

        ImageView imageView, pageFloatingActionButton, refFloatingActionButton;
        RelativeLayout layout, pageCountLayout, refCountLayout;
        ScrollView childScrollView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);

            layout = (RelativeLayout) view.findViewById(R.id.layoutsc);
            fb = (TextView) view.findViewById(R.id.close);
            childScrollView = (ScrollView) view.findViewById(R.id.childScroll);

            pageFloatingActionButton = (ImageView) view.findViewById(R.id.page);
            pageFloatingActionButton.setVisibility(View.GONE);
            refFloatingActionButton = (ImageView) view.findViewById(R.id.ref);
            refFloatingActionButton.setVisibility(View.GONE);
            pageCount = (TextView) view.findViewById(R.id.pagecount);
            pageCount.setVisibility(View.GONE);
            refCount = (TextView) view.findViewById(R.id.refcount);
            refCount.setVisibility(View.GONE);

            newTag = (TextView) view.findViewById(R.id.newtag);

            pageCountLayout = (RelativeLayout) view.findViewById(R.id.pagelayout);
            pageCountLayout.setVisibility(View.GONE);
            refCountLayout = (RelativeLayout) view.findViewById(R.id.reflayout);
            refCountLayout.setVisibility(View.GONE);
        }

        public void bind(final SearchData brandList, final int Position_) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("category_code", brandList.getBrand_code());
                    intent.putExtra("category_name", brandList.getCat_Type());
                    intent.putExtra("page_number", Position_ + "");
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
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
        SearchData tbdpg = brandList.get(position);
        holder.title.setText(tbdpg.getPageNamee());
        String filePath = new File(mContext.getFilesDir() + "/" + FilenameUtils.removeExtension(tbdpg.getImagePath()) + "/", FilenameUtils.removeExtension(tbdpg.getImagePath()) + ".png").getAbsolutePath();
        bitmap = BitmapFactory.decodeFile(filePath);
        holder.imageView.setImageBitmap(bitmap);
        holder.bind(brandList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}

