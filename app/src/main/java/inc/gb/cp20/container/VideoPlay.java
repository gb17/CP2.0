package inc.gb.cp20.container;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import inc.gb.cp20.R;

public class VideoPlay extends Activity {
    String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fileName = bundle.getString("fileName");
        }
        setContentView(R.layout.videoplayer);
        VideoView vidView = (VideoView) findViewById(R.id.playvideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vidView);
        vidView.setVisibility(View.VISIBLE);
        String path = VideoPlay.this.getFilesDir().getAbsolutePath();
        File file = new File(path + "/" + fileName);
        Uri uri = null;
        if (!file.exists()) {
            String uriPath = "android.resource://" + getPackageName() + "/raw/" + fileName;
            uri = Uri.parse(uriPath);
        } else {
            file.setReadable(true, false);
            uri = Uri.fromFile(file);
        }
        vidView.setVideoURI(uri);
        vidView.setMediaController(mediaController);
        vidView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
