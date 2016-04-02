package inc.gb.cp20.interfaces;

import android.view.View;

import inc.gb.cp20.Models.TBBRAND;

public interface RecyclerViewClickListener {

    void onItemClick(TBBRAND item, View v, int position);

    void onRetryClick(TBBRAND item, View v, int position);




}