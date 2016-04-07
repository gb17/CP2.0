package inc.gb.cp20.Landing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;

import inc.gb.cp20.R;

public class LoginPageActivity extends Activity {
    WebView loginWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayou);
        loginWebView = (WebView) findViewById(R.id.webView1);
        loginWebView.getSettings().setPluginState(PluginState.ON_DEMAND);
        loginWebView.getSettings().setJavaScriptEnabled(true);
        loginWebView.getSettings().setAllowFileAccess(true);
        loginWebView.addJavascriptInterface(new JavaScriptInterface(this),
                "cpjs");
        try {
            final String url1 = "file:///android_asset/" + "Hiora8/Hiora8.htm";
            loginWebView.loadUrl(url1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @android.webkit.JavascriptInterface
        public void sendToAndroid(String str) {


            Toast.makeText(LoginPageActivity.this,
                    "Please Enter Mandatory Details" + str, Toast.LENGTH_SHORT)
                    .show();


        }

    }
}
//        loginWebView.addJavascriptInterface(new JavaScriptInterface(this),
//                "MyAndroid");
//        try {
//            loginWebView.loadUrl("file:///android_asset/login/index.html");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public class JavaScriptInterface {
//        Context mContext;
//
//        JavaScriptInterface(Context c) {
//            mContext = c;
//        }
//
//        @android.webkit.JavascriptInterface
//        public void receiveValueFromJs(String str, String pwd) {
//            Toast.makeText(LoginPageActivity.this,
//                    "Please Enter Mandatory Details"+str, Toast.LENGTH_SHORT)
//                    .show();
//            if (str.equals("") || str.equals("")) {
//                Toast.makeText(LoginPageActivity.this,
//                        "Please Enter Mandatory Details", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//
//                Intent intent = new Intent(LoginPageActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
//
//            }
//
//        }
//
//    }
//}
