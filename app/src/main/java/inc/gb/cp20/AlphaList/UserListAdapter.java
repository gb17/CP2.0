package inc.gb.cp20.AlphaList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

import inc.gb.cp20.R;

public class UserListAdapter extends BaseAdapter {

    private static final String TAG = UserListAdapter.class.getName();

    private Vector<DrList_POJO> items;

    boolean headerview;
    boolean play_icon;
    boolean tick_icon;
    private ItemFilter mFilter = new ItemFilter();


    Typeface font;


    Context context;

    public UserListAdapter(Vector<DrList_POJO> items,
                           Context context, boolean headerview, boolean play_icon, boolean tick_icon) {
        Log.i(TAG, TAG);

        this.items = items;
        this.context = context;
        this.headerview = headerview;
        this.play_icon = play_icon;
        this.tick_icon = tick_icon;
        font = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            //	LayoutInflater inflater = context.getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_alphabates, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.drnametextview);
            holder.CLassTV = (TextView) convertView.findViewById(R.id.drclasstextview);
            holder.spcTV = (TextView) convertView.findViewById(R.id.drspcltextview);
            holder.menudot = (TextView) convertView.findViewById(R.id.threedot_menu);
            holder.drimageview = (de.hdodenhof.circleimageview.CircleImageView) convertView
                    .findViewById(R.id.dr_image);
            holder.headingLL = (LinearLayout) convertView
                    .findViewById(R.id.headingLL);
            holder.headingTV = (TextView) convertView
                    .findViewById(R.id.headingTV);
            holder.nameLL = (RelativeLayout) convertView
                    .findViewById(R.id.detail_area);
            holder.menudot.setTypeface(font);


            holder.tick = (TextView) convertView
                    .findViewById(R.id.tick1);

            holder.tick.setTypeface(font);


            holder.play = (TextView) convertView
                    .findViewById(R.id.play);

            holder.play.setTypeface(font);

            if (play_icon)
                holder.play.setVisibility(View.VISIBLE);
            if (tick_icon)
                holder.tick.setVisibility(View.VISIBLE);

            holder.menudot.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((AlphaListActivity) context).onItemListMenuClick(items.get(position),v);


                }
            });


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < items.size()) {

            final DrList_POJO drListPOJO = items.get(position);
            if (drListPOJO != null && (drListPOJO.getCOL14().length() == 1)) {
                holder.nameLL.setVisibility(View.GONE);
                if (headerview)
                    holder.headingLL.setVisibility(View.VISIBLE);
                holder.headingTV.setText(drListPOJO.getCOL14());
                holder.headingTV.setTextColor(Color.parseColor("#000000"));
                holder.headingLL.setBackgroundColor(Color.parseColor("#d9d7d4"));
            } else {
                holder.nameLL.setVisibility(View.VISIBLE);
                holder.headingLL.setVisibility(View.GONE);
                holder.name.setText(drListPOJO.getCOL1());
                holder.CLassTV.setText(drListPOJO.getCOL11());
                holder.spcTV.setText(drListPOJO.getCOL10());

                //  holder.drimageview.setImageResource(R.drawable.phy);

                View ll = (RelativeLayout) holder.name.getParent();
                ll.setFocusable(true);
                ll.setSelected(true);

            }
        }

        holder.nameLL.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AlphaListActivity) context).onItemListClick(items.get(position), v);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView name, headingTV, spcTV, CLassTV, menudot, tick, play;
        de.hdodenhof.circleimageview.CircleImageView drimageview;
        LinearLayout headingLL;
        RelativeLayout nameLL;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            notifyDataSetChanged();
        }

    }



}

