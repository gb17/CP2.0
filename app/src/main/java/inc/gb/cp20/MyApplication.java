package inc.gb.cp20;


import android.app.Application;
import android.content.SharedPreferences;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import inc.gb.cp20.DB.DBHandler;

@ReportsCrashes(formKey = "iCLMCP20", // will not be used
        mailTo = "logs@cirrius.com",
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE, ReportField.LOGCAT})
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        DBHandler handler = DBHandler.getInstance(this);
        String data[][] = handler.genericSelect("Select VAL FROM TBUPW", 1);
        if (data != null) {
            String[] upwData = data[0][0].split("\\^");
            String rnumber = upwData[8];

            SharedPreferences preferences = getSharedPreferences("CP20", MODE_PRIVATE);

            String value = preferences.getString("unlisted_key",null);
            if (value == null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("unlisted_key", rnumber);
                editor.commit();
            }
        }
    }
}
