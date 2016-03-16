package inc.gb.cp20.playlist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import inc.gb.cp20.R;

/**
 * Created by Shubham on 3/9/16.
 */
public class BrandlistAdapter extends RecyclerView.Adapter<BrandlistAdapter.NewViewHolder> {

    ArrayList<String[]> gridData;
    ArrayList<String[]> gridDataForRecycler;
    Context mContext;

    public class NewViewHolder extends RecyclerView.ViewHolder {
        public TextView PageName, pageCount, threedotMenu;
        ImageView PageIcon;

        public NewViewHolder(View view) {
            super(view);
            PageName = (TextView) view.findViewById(R.id.title);
            PageIcon = (ImageView) view.findViewById(R.id.bandimage);
            pageCount = (TextView) view.findViewById(R.id.pagecount);
            threedotMenu = (TextView) view.findViewById(R.id.threedot);
        }
    }

    public BrandlistAdapter(Context mContext, ArrayList<String[]> gridData, ArrayList<String[]> gridDataForRecycler) {
        this.mContext = mContext;
        this.gridData = gridData;
        this.gridDataForRecycler = gridDataForRecycler;
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tumbnail_item, parent, false);
        itemView.setOnLongClickListener(new MyOnLongClickListener());
        itemView.setOnDragListener(
                new MyDragListener(mContext, gridData, gridDataForRecycler));
        return new NewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewViewHolder holder, int position) {
        String[] str = gridData.get(position);
        holder.PageName.setText(str[3]);
        String filePath = new File(mContext.getFilesDir() + "/done/", FilenameUtils.removeExtension(str[2]) + ".png").getAbsolutePath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.PageIcon.setImageBitmap(bitmap);
        holder.pageCount.setVisibility(View.GONE);
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
