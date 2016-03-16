package inc.gb.cp20.container;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import inc.gb.cp20.R;

public class Custom_grid extends BaseAdapter {

    private Context mcontext;
    String[][] listData;

    public Custom_grid(Context c, String[][] listData) {
        // TODO Auto-generated constructor stub
        mcontext = c;
        this.listData = listData;

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

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_icon);
        String str = listData[position][2].toLowerCase();
        if (str.equals("pdf"))
            imageView.setImageResource(R.drawable.pdf);
        else if (str.equals("mp4"))
            imageView.setImageResource(R.drawable.video);

        TextView seq = (TextView) convertView.findViewById(R.id.text);
        seq.setText(listData[position][0]);

        final ImageView icon = (ImageView) convertView.findViewById(R.id.email);

        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                icon.setImageResource(R.drawable.emailiconglow);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Container.disMiss(position);

            }
        });
        return convertView;
    }

}
