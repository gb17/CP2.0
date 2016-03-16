package inc.gb.cp20.List_Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.R;

/**
 * Created by Shubham on 3/12/16.
 */
public class ContentAdapter extends BaseAdapter {
    List<IRCSFResponsePOJO> listData;
    public boolean[] mCheckedState;
    Context context;
    private Typeface font;

    public ContentAdapter(Context context, List<IRCSFResponsePOJO> listData) {
        this.context = context;
        this.listData = listData;
        mCheckedState = new boolean[listData.size()];
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public int getCount() {
        return listData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_list, null);

            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.check);
            holder.category = (TextView) convertView.findViewById(R.id.cnt_category);
            holder.type = (TextView) convertView.findViewById(R.id.cnt_type);
            holder.size = (TextView) convertView.findViewById(R.id.file_size);
            holder.date = (TextView) convertView.findViewById(R.id.effect_date);
            holder.status = (TextView) convertView.findViewById(R.id.status);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IRCSFResponsePOJO pojo = listData.get(position);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                mCheckedState[position] = compoundButton.isChecked();
                if (mCheckedState[position]) {
                    compoundButton.setChecked(true);
                } else {
                    compoundButton.setChecked(false);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox child = ((CheckBox) ((LinearLayout) view).getChildAt(0));
                if (child.isChecked())
                    child.setChecked(false);
                else
                    child.setChecked(true);

            }
        });

        holder.checkBox.setChecked(mCheckedState[position]);
        holder.category.setText(pojo.getCNT_CATEGORY());
        if (pojo.getCNT_TYPE().equalsIgnoreCase("html"))
            holder.type.setText(context.getResources().getString(R.string.html_icon));
        else if (pojo.getCNT_TYPE().equalsIgnoreCase("pdf"))
            holder.type.setText(context.getResources().getString(R.string.pdf_icon));
        else if (pojo.getCNT_TYPE().equalsIgnoreCase("mp4"))
            holder.type.setText(context.getResources().getString(R.string.play_icon));
        holder.date.setText(pojo.getEFFECT_DATE());
        holder.size.setText(pojo.getFILE_SIZE());
        holder.type.setTypeface(font);

        return convertView;
    }

    static class ViewHolder {
        public CheckBox checkBox;
        public TextView category;
        public TextView type;
        public TextView status;
        public TextView date;
        public TextView size;

    }

}
