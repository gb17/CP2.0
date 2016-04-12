package inc.gb.cp20.Util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import inc.gb.cp20.Models.ACKTAG;
import inc.gb.cp20.Models.CNTACKPOJO;
import inc.gb.cp20.Models.CVR;
import inc.gb.cp20.Models.ChangePassword;
import inc.gb.cp20.Models.ContainerPOJO;
import inc.gb.cp20.Models.IRCSFPOJO;
import inc.gb.cp20.Models.IRCSFResponsePOJO;
import inc.gb.cp20.Models.OutputPOJO;
import inc.gb.cp20.Models.ReqCVR;
import inc.gb.cp20.Models.SyncDetailingAckPOJO;
import inc.gb.cp20.Models.TAG;
import inc.gb.cp20.Models.TablesConfig;
import inc.gb.cp20.Models.UPW;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 *
 */
public class RestClient {


    private static GitApiInterface gitApiInterface;

    //     public static String baseUrl = "http://10.0.0.34:83";
      public static String baseUrl = "http://cirriusapi.cirrius.in";
 //   public static String baseUrl = "http://t1.cirrius.in";

    public static GitApiInterface getClient() {
        if (gitApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.setConnectTimeout(2, TimeUnit.MINUTES);
            okClient.setReadTimeout(2, TimeUnit.MINUTES);
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(chain.request());
                }
            });

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(GitApiInterface.class);
        }
        return gitApiInterface;
    }


    public interface GitApiInterface {
        @POST("/Config/UPW")
        Call<UPW> CallUPW(@Body UPW order);

        @POST("/ReqRes/CVR")
        Call<CVR> CallCVR(@Body ReqCVR reqCVR);

        @POST("/Config/AckTags")
        Call<ACKTAG> CallACK(@Body ACKTAG acktag);

        @POST("/Security/CHANGEPWD")
        Call<ChangePassword> CallChangePassword(@Body ChangePassword changePassword);

        @POST("/Config/ReadScript")
        Call<TablesConfig> CallTagDownload(@Body TAG tag);

        @POST("/Detailing/UDDET")
        Call<List<OutputPOJO>> uploadContainerData(@Body List<ContainerPOJO> req);

        @POST("/ReqRes/IRCSF")
        Call<List<IRCSFResponsePOJO>> downloadContentUrl(@Body IRCSFPOJO req);

        @POST("/Config/CONTENTACK")
        Call<String> contentAcknowledge(@Body CNTACKPOJO req);

        @POST("/Security/FORGOTPWD")
        Call<ChangePassword> CallForgotPassword(@Body ChangePassword changePassword);

        @POST("/Detailing/SYNCDETAILING")
        Call<List<SyncDetailingAckPOJO>> CallSyncDetailingAcknowledge(@Body List<SyncDetailingAckPOJO> req);


    }

    public interface GitApiInterface1 {
        @POST("/CommonAPI/Config/UPW")
        Call<UPW> CallUPW(@Body UPW order);

        @POST("/CommonAPI/ReqRes/CVR")
        Call<CVR> CallCVR(@Body ReqCVR reqCVR);

        @POST("/CommonAPI/Config/AckTags")
        Call<ACKTAG> CallACK(@Body ACKTAG acktag);

        @POST("/CommonAPI/Security/CHANGEPWD")
        Call<ChangePassword> CallChangePassword(@Body ChangePassword changePassword);

        @POST("/CommonAPI/Config/ReadScript")
        Call<TablesConfig> CallTagDownload(@Body TAG tag);

        @POST("/CommonAPI/Detailing/UDDET")
        Call<List<OutputPOJO>> uploadContainerData(@Body List<ContainerPOJO> req);

        @POST("/CommonAPI/ReqRes/IRCSF")
        Call<List<IRCSFResponsePOJO>> downloadContentUrl(@Body IRCSFPOJO req);

        @POST("/CommonAPI/Config/CONTENTACK")
        Call<String> contentAcknowledge(@Body CNTACKPOJO req);

        @POST("/CommonAPI/Security/FORGOTPWD")
        Call<ChangePassword> CallForgotPassword(@Body ChangePassword changePassword);

        @POST("/CommonAPI/Detailing/SYNCDETAILING")
        Call<List<SyncDetailingAckPOJO>> CallSyncDetailingAcknowledge(@Body List<SyncDetailingAckPOJO> req);


    }

    //http://t1.cirrius.in/CommonAPI/Config/UPW
//    http://t1.cirrius.in/CommonAPI/ReqRes/CVR
//    http://t1.cirrius.in/CommonAPI/Security/CHANGEPWD
//    http://t1.cirrius.in/CommonAPI/Security/FORGOTPWD
//    http://t1.cirrius.in/CommonAPI/Config/CONTENTACK
//    http://t1.cirrius.in/CommonAPI/Config/AckTags
//    http://t1.cirrius.in/CommonAPI/Config/ReadScript
//    http://t1.cirrius.in/CommonAPI/Detailing/UDDET
//    http://t1.cirrius.in/CommonAPI/Detailing/SYNCDETAILING
//    http://t1.cirrius.in/CommonAPI/ReqRes/IRCSF

//    http://cirriusapi.cirrius.in/Config/UPW
//    http://cirriusapi.cirrius.in/ReqRes/CVR
//    http://cirriusapi.cirrius.in/Security/CHANGEPWD
//    http://cirriusapi.cirrius.in/Security/FORGOTPWD
//    http://cirriusapi.cirrius.in/Config/CONTENTACK
//    http://cirriusapi.cirrius.in/Config/AckTags
//    http://cirriusapi.cirrius.in/Config/ReadScript
//    http://cirriusapi.cirrius.in/Detailing/UDDET
//    http://cirriusapi.cirrius.in/Detailing/SYNCDETAILING
//    http://cirriusapi.cirrius.in/ReqRes/IRCSF


}
