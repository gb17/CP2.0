package inc.gb.cp20.playlist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

import inc.gb.cp20.R;

/**
 * Created by Shubham on 3/5/16.
 */
public class GridAdapter extends BaseAdapter {
    ArrayList<String[]> gridData;
    ArrayList<String[]> gridDataForRecycler;
    Context mContext;

    public GridAdapter(Context mContext, ArrayList<String[]> gridData, ArrayList<String[]> gridDataForRecycler) {
        this.mContext = mContext;
        this.gridData = gridData;
        this.gridDataForRecycler = gridDataForRecycler;
    }


    @Override
    public int getCount() {
        return gridData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tumbnail_item_grid, viewGroup, false);
            holder = new ViewHolder();
            holder.PageName = (TextView) convertView.findViewById(R.id.title);
            holder.PageIcon = (ImageView) convertView.findViewById(R.id.bandimage);
            holder.pageCount = (TextView) convertView.findViewById(R.id.pagecount);
            holder.threedotMenu = (TextView) convertView.findViewById(R.id.threedot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String[] str = gridData.get(position);
        holder.PageName.setText(str[3]);
        String filePath = new File(mContext.getFilesDir() + "/done/", FilenameUtils.removeExtension(str[2]) + ".png").getAbsolutePath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.PageIcon.setImageBitmap(bitmap);
        holder.pageCount.setVisibility(View.GONE);

        convertView.setOnLongClickListener(new mylongclick());
        convertView.setOnDragListener(
                new MyDragListener(mContext, gridData, gridDataForRecycler));

        return convertView;
    }

    public final class mylongclick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            if (((RelativeLayout) view.getParent().getParent()).getChildAt(2).getVisibility() == View.GONE) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
            }
            return true;
        }
    }

    static class ViewHolder {
        public TextView PageName, pageCount, threedotMenu;
        ImageView PageIcon;
    }
}
