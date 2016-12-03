package co.broccli.logic.rest;

import java.util.Map;

import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.model.profile.User;
import co.broccli.logic.model.signup.Signup;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

public interface ApiInterface {

    /**
     *  Login user call
     *
     * @param accessTokenRequest request data
     * @return Access-Token data
     */
    @POST("oauth/token")
    Call<OAuth2AccessToken> getAccessTokenData(@Body AccessTokenRequest accessTokenRequest);

    /**
     *  Firebase JWT call
     *
     * @return Firebase JWT
     */
    @GET("firebase/auth")
    Call<ResponseBody> getFirebaseJWT ();

    /**
     *  Logout call
     *
     * @return void
     */
    @GET("logout")
    Call<ResponseBody> logout();

    /**
     *  Signup call
     *
     * @param name User full name
     * @param email User Email
     * @param password User Password
     * @return created user status
     */
    @FormUrlEncoded
    @POST("register")
    Call<Signup> signup(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Get loggedin User profile
     *
     * @return User
     */
    @GET("profile/name,photo,email")
    Call<User> getUser();

    /**
     * Edit loggedin User profile
     *
     * @return User
     */
    @Multipart
    @POST("profile")
    Call<User> editUser(
            @Header("X-HTTP-Method-Override") String _method,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part photo
    );
}