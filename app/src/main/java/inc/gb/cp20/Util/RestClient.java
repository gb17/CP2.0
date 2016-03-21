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
 * Created by GB on 3/11/16.
 */
public class RestClient {


    private static GitApiInterface gitApiInterface;

    public static String baseUrl = "http://10.0.0.34:83";

    public static GitApiInterface getClient() {
        if (gitApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.setConnectTimeout(5, TimeUnit.MINUTES);
            okClient.setReadTimeout(5, TimeUnit.MINUTES);
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response;
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
        @POST("/CirriusConfigAPI/UPW")
        Call<UPW> CallUPW(@Body UPW order);

        @POST("/CirriusReqResAPI/CVR")
        Call<CVR> CallCVR(@Body ReqCVR reqCVR);

        @POST("/CirriusConfigAPI/AckTags")
        Call<ACKTAG> CallACK(@Body ACKTAG acktag);

        @POST("/CirriusSecurityAPI/CHANGEPWD")
        Call<ChangePassword> CallChangePassword(@Body ChangePassword changePassword);

        @POST("/CirriusConfigAPI/ReadScript")
        Call<TablesConfig> CallTagDownload(@Body TAG tag);

        @POST("/CirriusDetailingAPI/UDDET")
        Call<List<OutputPOJO>> uploadContainerData(@Body List<ContainerPOJO> req);

        @POST("/CirriusReqResAPI/IRCSF")
        Call<List<IRCSFResponsePOJO>> downloadContentUrl(@Body IRCSFPOJO req);

        @POST("/CirriusConfigAPI/CONTENTACK")
        Call<String> contentAcknowledge(@Body CNTACKPOJO req);

        @POST("/CirriusSecurityAPI/FORGOTPWD")
        Call<ChangePassword> CallForgotPassword(@Body ChangePassword changePassword);

        @POST("/CirriusDetailingAPI/SYNCDETAILING")
        Call<List<SyncDetailingAckPOJO>> CallSyncDetailingAcknowledge(@Body List<SyncDetailingAckPOJO> req);


    }


}
