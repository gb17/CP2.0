package inc.gb.cp20.RecylerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Models.SearchData;
import inc.gb.cp20.R;
import inc.gb.cp20.container.VideoPlay;
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
    static DBHandler dbHandler;
    Bitmap bitmap;

    public ThumbnailAdpForSearch(List<SearchData> brandList, Context mContext, FragmentManager fragmentManager, RecyclerViewClickListener itemListener) {
        this.brandList = brandList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");
        dbHandler = DBHandler.getInstance(mContext);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, brandname, pagename, newTag;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);
            newTag = (TextView) view.findViewById(R.id.newtag);
            brandname = (TextView) view.findViewById(R.id.brand_name);
            pagename = (TextView) view.findViewById(R.id.page_name);


        }

        public void bind(final SearchData brandList, final int Position_) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (brandList.getPageNamee().toLowerCase().contains("mp4")) {
                        Intent intent = new Intent(mContext,
                                VideoPlay.class);
                        intent.putExtra("fileName", brandList.getPageNamee());
                        ((Activity) mContext).startActivityForResult(intent, 1001);
                    } else if (brandList.getPageNamee().contains("pdf")) {
                        String path1 = mContext.getFilesDir().getAbsolutePath() + "/"
                                + brandList.getPageNamee();
                        File file1 = new File(path1);
                        if (file1.exists()) {
                            file1.setReadable(true, false);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                            try {
                                ((Activity) mContext).startActivityForResult(intent, 1001);
                            } catch (Exception e) {
                                System.out.println("PDF Exception = = = >" + e.toString());
                            }
                        } else {
                            Toast.makeText(mContext,
                                    "Please wait PDF being downloaded.....", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {

                        String pos = "0";
                        String[][] playstData = dbHandler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                                "        where a.col5 = b.col0\n" +
                                "        and a.COL3 = '" + brandList.getBrand_code() + "' and a.COL9 = '" + brandList.getCat_Type() + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
                        if (playstData != null)
                            for (int i = 0; i < playstData.length; ++i) {
                                for (int j = 0; j < playstData[i].length; ++j) {
                                    if (brandList.getPageCode().equals(playstData[i][0])) {
                                        pos = i + "";
                                        break;
                                    }
                                }
                            }
                        Intent intent = new Intent();
                        intent.putExtra("category_code", brandList.getBrand_code());
                        intent.putExtra("category_name", brandList.getCat_Type());
                        intent.putExtra("page_number", pos);
                        intent.putExtra("index_lib", "4");
                        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                        ((Activity) mContext).finish();
                    }
                }
            });

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tumbnail_item_search, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchData tbdpg = brandList.get(position);
        holder.title.setText(tbdpg.getPageNamee());
        holder.pagename.setText(tbdpg.getSubpageName());
        holder.brandname.setText(tbdpg.getCat_Name());
        String filePath = new File(mContext.getFilesDir() + "/" + FilenameUtils.removeExtension(tbdpg.getImagePath()) + "/", FilenameUtils.removeExtension(tbdpg.getImagePath()) + ".png").getAbsolutePath();
        if (tbdpg.getPageNamee().toLowerCase().contains("mp4")) {
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
            holder.imageView.setImageResource(R.drawable.vdoicon);
        } else if (tbdpg.getPageNamee().toLowerCase().contains("pdf")) {
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
            holder.imageView.setImageResource(R.drawable.pdficon);
        } else {
            bitmap = BitmapFactory.decodeFile(filePath);
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.bind(brandList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}

