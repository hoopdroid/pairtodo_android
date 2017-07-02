package com.pairtodopremium.network.api;

import com.google.gson.GsonBuilder;
import com.pairtodopremium.data.response.EmptyResultResponse;
import com.pairtodopremium.data.response.chat.ChatDataResponse;
import com.pairtodopremium.data.response.gifts.GiftsResponse;
import com.pairtodopremium.data.response.invite.InviteResponse;
import com.pairtodopremium.data.response.pulse.PulseResponse;
import com.pairtodopremium.data.response.searchCouple.SearchCoupleResponse;
import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.data.response.stats.StatsData;
import com.pairtodopremium.data.response.tasks.ChangeTaskResponse;
import com.pairtodopremium.data.response.tasks.GetTasksResponse;
import com.pairtodopremium.data.response.tasks.UploadImageData;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.response.userPhoto.UploadUserPhotoResponse;
import com.pairtodopremium.utils.AutoValueGsonFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * A manager that allows get data from network using Retrofit2
 */

public class ApiManager {

  public static final String APP_TOKEN = "977c3deeecf18d5d8a0abbd77667eebf86081";
  private static final String URL = "http://v3.api.pairtodo.com";
  public static OkHttpClient okHttpClient;
  private static ApiService apiService;

  public ApiManager() {
    initRetrofit();
  }

  public static ApiService getApiService() {
    synchronized (ApiManager.class) {
      if (apiService == null) {
        new ApiManager();
      } else {
        return apiService;
      }
    }

    return apiService;
  }

  private static void initRetrofit() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build();

    GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
        new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create())
            .serializeNulls()
            .create());
    Retrofit retrofit = new Retrofit.Builder().baseUrl(URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build();

    apiService = retrofit.create(ApiService.class);
  }

  public static void cancelAllRequests() {
    if (okHttpClient != null) okHttpClient.dispatcher().cancelAll();
  }

  public interface ApiService {

    @GET("/user/reg") Call<BasicResponse> signUpUser(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("email") String email, @Query("name") String name,
        @Query("password") String passwordSha1, @Query("ts") long timeStamp);

    @GET("/user/auth") Call<BasicResponse> signInUser(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("email") String email, @Query("password") String passwordSha1,
        @Query("ts") long timeStamp);

    @GET("/user/socauth") Call<BasicResponse> signInUserVk(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("soc") String socialTitle, @Query("soc_id") String socialIdSha1,
        @Query("soc_name") String name, @Query("soc_photo") String photo,
        @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/job/add") Call<BasicResponse> addTask(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Field("title") String title, @Field("term_date") String termDate,
        @Field("description") String description, @Field("list") String list,
        @Field("image") String image, @Field("executor") String executor,
        @Field("important") String important, @Query("token") String token,
        @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/job/set") Call<ChangeTaskResponse> editTask(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("job_id") String id, @Field("job_id") String idJob, @Field("title") String title,
        @Field("term_date") String termDate, @Field("description") String description,
        @Field("list") String list, @Field("image") String image,
        @Field("executor") String executor, @Field("important") String important,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/job/get") Call<GetTasksResponse> getMyTasks(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/job/remove") Call<ChangeTaskResponse> removeTask(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("job_id") String id, @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/job/mark") Call<ChangeTaskResponse> markTask(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("job_id") String id, @Query("token") String token, @Query("ts") long timeStamp,
        @Query("finish") String finish);

    @GET("/user/getme") Call<UserDataResponse> getMyInfo(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/pair/sendcode") Call<InviteResponse> sendCode(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Field("code1") String code1, @Field("code2") String code2, @Query("token") String token,
        @Query("ts") long timeStamp);

    @Multipart @POST("/job/addimage") Call<UploadImageData> addImageToTask(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Part MultipartBody.Part file, @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/statistics/get") Call<StatsData> getStats(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/mail/get") Call<GiftsResponse> getGifts(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/mail/send") Call<EmptyResultResponse> sendGift(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp, @Field("text") String text,
        @Field("template") String template);

    @GET("/message/getarchive") Call<ChatDataResponse> getMessagesArchive(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/message/getnew") Call<ChatDataResponse> getNewMessages(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/message/send") Call<BasicResponse> sendMessage(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Field("message") String important, @Query("token") String token,
        @Query("ts") long timeStamp);

    @GET("/user/pulse") Call<PulseResponse> getPulse(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("message/getjobarchive") Call<ChatDataResponse> getJobArchiveMessages(
        @Query("_app") int appId, @Query("_sig") String sig, @Query("_lng") String lng,
        @Query("_os") String os, @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/message/getjobnew") Call<ChatDataResponse> getJobNewMessages(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/message/sendjob") Call<BasicResponse> sendJobMessage(
        @Query("_app") int appId, @Query("_sig") String sig, @Query("_lng") String lng,
        @Query("_os") String os, @Field("job_id") String job_id, @Field("message") String message,
        @Query("token") String token, @Query("ts") long timeStamp);

    @Multipart @POST("/user/setavatar") Call<UploadUserPhotoResponse> setUserImage(
        @Query("_app") int appId, @Query("_sig") String sig, @Query("_lng") String lng,
        @Query("_os") String os, @Part MultipartBody.Part file, @Query("token") String token,
        @Query("ts") long timeStamp);

    @GET("/pair/search") Call<SearchCoupleResponse> searchPairByNameOrEmail(
        @Query("_app") int appId, @Query("_sig") String sig, @Query("_lng") String lng,
        @Query("_os") String os, @Query("type") String type, @Query("query") String query,
        @Query("token") String token, @Query("ts") long timeStamp);

    @FormUrlEncoded @POST("/user/setme") Call<EmptyResultResponse> changeValues(
        @Query("_app") int appId, @Query("_sig") String sig, @Query("_lng") String lng,
        @Query("_os") String os, @FieldMap Map<String, String> fields, @Query("token") String token,
        @Query("ts") long timeStamp);

    @GET("/user/logout") Call<EmptyResultResponse> logout(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);

    @GET("/pair/remove") Call<EmptyResultResponse> removeCouple(@Query("_app") int appId,
        @Query("_sig") String sig, @Query("_lng") String lng, @Query("_os") String os,
        @Query("token") String token, @Query("ts") long timeStamp);
  }
}
