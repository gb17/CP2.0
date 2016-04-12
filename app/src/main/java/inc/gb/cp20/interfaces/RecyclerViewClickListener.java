package inc.gb.cp20.interfaces;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import inc.gb.cp20.Models.TBBRAND;

public interface RecyclerViewClickListener {

    void onItemClick(TBBRAND item, View v, int position);

    void onRetryClick(TBBRAND item, View v, int position);

    void onPageClick(TBBRAND item, View v, int position, RelativeLayout layout, int total, ScrollView scrollView, TextView textView);

    void onRefClick(TBBRAND item, View v, int position, RelativeLayout layout, int total, ScrollView scrollView, TextView textView);


}