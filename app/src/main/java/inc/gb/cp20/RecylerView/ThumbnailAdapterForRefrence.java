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
import inc.gb.cp20.interfaces.RefrenceListner;

public class ThumbnailAdapterForRefrence extends RecyclerView.Adapter<ThumbnailAdapterForRefrence.MyViewHolder> {

    private static RefrenceListner itemListener;

    public static DBHandler dbHandler;

    public List<RefrenceContent> brandList;
    static Typeface font;
    static Context mContext;
    FragmentManager fragmentManager;

    static public long startTime = 0;
    static public long endTime = 0;
    static public long duration = 0;


    String patch;
    String randomNumber;


    public ThumbnailAdapterForRefrence(List<RefrenceContent> brandList, Context mContext, FragmentManager fragmentManager, RefrenceListner itemListener, String patch, String randomNumber) {
        this.brandList = brandList;
        ThumbnailAdapterForRefrence.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.itemListener = itemListener;
        font = Typeface.createFromAsset(mContext.getAssets(),
                "fontawesome-webfont.ttf");

        dbHandler = DBHandler.getInstance(mContext);

        this.patch = patch;
        this.randomNumber = randomNumber;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, page_name;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.bandimage);
            page_name = (TextView) view.findViewById(R.id.page_name);
        }

        public void bind(final RefrenceContent brandList, final RefrenceListner listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onRefrenceClick(brandList);
//                    startTime = System.currentTimeMillis();
//                    if (brandList.getRefrenceName().toLowerCase().contains("mp4")) {
//                        Intent intent = new Intent(mContext,
//                                VideoPlay.class);
//                        intent.putExtra("fileName", brandList.getRefrenceName());
//
//                        Activity origin = (Activity) mContext;
//                        origin.startActivityForResult(new Intent(mContext, ContentLibrary.class), 1111);
//
//                    } else if (brandList.getRefrenceName().contains("pdf")) {
//                        String path1 = mContext.getFilesDir().getAbsolutePath() + "/"
//                                + brandList.getRefrenceName();
//                        File file1 = new File(path1);
//                        if (file1.exists()) {
//                            file1.setReadable(true, false);
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
//                            try {
//                                ((Activity) mContext).startActivityForResult(intent, 1001);
//                            } catch (Exception e) {
//                                System.out.println("PDF Exception = = = >" + e.toString());
//                            }
//                        } else {
//                            Toast.makeText(mContext,
//                                    "Please wait PDF being downloaded.....", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }

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
        holder.page_name.setText(tbbrand.getPagename_name());
        if (tbbrand.getRefrenceName().toLowerCase().contains("mp4")) {
            holder.imageView.setImageResource(R.drawable.vdoicon);
        } else if (tbbrand.getRefrenceName().toLowerCase().contains("pdf")) {
            holder.imageView.setImageResource(R.drawable.pdficon);
        }


        holder.bind(brandList.get(position), itemListener);


    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }


}
