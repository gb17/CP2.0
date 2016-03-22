package inc.gb.cp20.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("NewApi")
class MyDragListener implements OnDragListener {
    Context context;
    ArrayList<String[]> griddata;
    ArrayList<String[]> recyclerdata;

    public MyDragListener(Context context, ArrayList<String[]> arrList, ArrayList<String[]> recyclerdata) {
        this.context = context;
        this.griddata = arrList;
        this.recyclerdata = recyclerdata;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:

                break;
            case DragEvent.ACTION_DRAG_EXITED:

                // v.setBackgroundDrawable(normalShape);

                break;
            case DragEvent.ACTION_DROP:
                //Jispe drop hua is a parent  //chance is jaha drop kiya //chance2 is jaha se uthaya
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent instanceof RecyclerView) {
                    RecyclerView recycle = (RecyclerView) parent;
                    if (parent.getTag().equals("1")) {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) recycle.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int chance = firstVisiblePosition + parent.indexOfChild(v);
                        View view = (View) event.getLocalState();
                        int chance2 = parent.indexOfChild(view);
                        if (chance2 == -1) {
                            RecyclerView grid = (RecyclerView) view.getParent();
                            LinearLayoutManager layoutManager2 = ((LinearLayoutManager) grid.getLayoutManager());
                            int firstVisiblePosition2 = layoutManager2.findFirstVisibleItemPosition();
                            chance2 = firstVisiblePosition2 + grid.indexOfChild(view);
                            String[] str = griddata.get(chance2);
                            griddata.remove(chance2);
                            recyclerdata.add(chance, str);
                            PlaylistAdapter adapter = new PlaylistAdapter(context, griddata,
                                    recyclerdata, 1);
                            recycle.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            BrandlistAdapter adapter2 = new BrandlistAdapter(context, griddata,
                                    recyclerdata);
                            grid.setAdapter(adapter2);
                            adapter.notifyDataSetChanged();
                        } else {
                            chance2 = firstVisiblePosition + parent.indexOfChild(view);
                            String[] str = recyclerdata.get(chance2);
                            recyclerdata.remove(chance2);
                            recyclerdata.add(chance, str);
                            PlaylistAdapter adapter = new PlaylistAdapter(context, griddata,
                                    recyclerdata, 1);
                            recycle.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        ((TextView) ((RelativeLayout) recycle.getParent()).getChildAt(1)).setText("(" + recyclerdata.size() + " Pages)");
                    } else if (parent.getTag().equals("2")) {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) recycle.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int chance = firstVisiblePosition + parent.indexOfChild(v);
                        View view = (View) event.getLocalState();
                        int chance2 = parent.indexOfChild(view);
                        if (chance2 == -1) {
                            RecyclerView grid = (RecyclerView) view.getParent();
                            LinearLayoutManager layoutManager2 = ((LinearLayoutManager) grid.getLayoutManager());
                            int firstVisiblePosition2 = layoutManager2.findFirstVisibleItemPosition();
                            chance2 = firstVisiblePosition2 + grid.indexOfChild(view);
                            String[] str = recyclerdata.get(chance2);
                            recyclerdata.remove(chance2);
                            griddata.add(chance, str);
                            PlaylistAdapter adapter = new PlaylistAdapter(context, griddata,
                                    recyclerdata, 1);
                            grid.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            BrandlistAdapter adapter2 = new BrandlistAdapter(context, griddata,
                                    recyclerdata);
                            recycle.setAdapter(adapter2);
                            adapter.notifyDataSetChanged();
                        } else {
                            chance2 = firstVisiblePosition + parent.indexOfChild(view);
                            String[] str = griddata.get(chance2);
                            griddata.remove(chance2);
                            griddata.add(chance, str);
                            BrandlistAdapter adapter = new BrandlistAdapter(context, griddata,
                                    recyclerdata);
                            recycle.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }

        return true;
    }
}
