package inc.gb.cp20.container;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import inc.gb.cp20.R;

/**
 * Created by Shubham on 2/8/16.
 */
public class LightContainer extends Activity {
    public String customer_id = "";
    public String customer_name = "";
    public String category_code = "";
    public String category_name = "";
    public String thumbnail_category = "";
    public String patch = "";
    public String index = "0";

    public String url = "";

    TextView name;
    ImageView next;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightcontainer);

        name = (TextView) findViewById(R.id.name);
        next = (ImageView) findViewById(R.id.next);
        webView = (WebView) findViewById(R.id.webView);

        Bundle extras = getIntent().getExtras();
        customer_id = extras.getString("customer_id");
        customer_name = extras.getString("customer_name");
        category_code = extras.getString("category_code");
        category_name = extras.getString("category_name");
        thumbnail_category = extras.getString("thumbnail_category");
        index = extras.getString("index");
        patch = extras.getString("patch");
        if (index == null)
            index = "0";
        if (patch == null)
            patch = "";

        if (customer_name != null)
            name.setText(customer_name);
        else{
            Intent intent = new Intent(LightContainer.this, Container.class);
            Bundle bundle = new Bundle();
            bundle.putString("category_code", category_code);
            bundle.putString("category_name", category_name);
            bundle.putString("thumbnail_category", thumbnail_category);
            bundle.putString("index", index);
            bundle.putString("patch", patch);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }


        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        url = "file://" + getFilesDir().getAbsolutePath() + "/welcome/welcome.htm";

        // url = "file:///android_asset/WELCOME/welcome.htm";
        webView = (WebView) findViewById(R.id.webView);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });

        next.setOnClickListener(nextListener);
    }

    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name.setVisibility(View.GONE);
            next.setVisibility(View.GONE);

            Intent intent = new Intent(LightContainer.this, Container.class);
            Bundle bundle = new Bundle();
            if (customer_id != null && customer_name != null) {
                bundle.putString("customer_id", customer_id);
                bundle.putString("customer_name", customer_name);
            }
            bundle.putString("category_code", category_code);
            bundle.putString("category_name", category_name);
            bundle.putString("thumbnail_category", thumbnail_category);
            bundle.putString("index", index);
            bundle.putString("patch", patch);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };
}
