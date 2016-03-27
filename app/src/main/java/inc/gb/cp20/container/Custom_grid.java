package inc.gb.cp20.container;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import inc.gb.cp20.R;

public class Custom_grid extends BaseAdapter {

    private Context mcontext;
    String[][] listData;
    private Typeface font;

    public Custom_grid(Context c, String[][] listData) {
        // TODO Auto-generated constructor stub
        mcontext = c;
        this.listData = listData;
        font = Typeface.createFromAsset(mcontext.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customlistnotification, null);
        }

        TextView seq = (TextView) convertView.findViewById(R.id.text);
        final TextView icon = (TextView) convertView.findViewById(R.id.email);

        String str = listData[position][2].toLowerCase();

        if (str.equals("pdf"))
            seq.setText(mcontext.getResources().getString(R.string.pdf_icon) + "   " + listData[position][0]);
        else if (str.equals("mp4"))
            seq.setText(mcontext.getResources().getString(R.string.play_icon) + "   " + listData[position][0]);

        icon.setText(mcontext.getResources().getString(R.string.message));
        icon.setTypeface(font);
        seq.setTypeface(font);

        icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                icon.setTextColor(Color.parseColor("#00FFFF"));
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                ((Container)mcontext).disMiss(position);
            }
        });
        return convertView;
    }

}
