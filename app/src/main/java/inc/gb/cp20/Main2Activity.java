package inc.gb.cp20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trncic.library.DottedProgressBar;

public class Main2Activity extends AppCompatActivity {
    DottedProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        progressBar = (DottedProgressBar) findViewById(R.id.progress);
        progressBar.startProgress();

    }
}
