package co.broccli.logic.rest;

import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.model.protectedPath.protectedPath;
import co.broccli.logic.model.signup.Signup;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    /**
     * This call for protected API that require
     * access token to be provided to work
     * FOR TEST PROPOSES ONLY
     *
     * @return User
     */
    @GET("protected")
    Call<protectedPath> getUser();

    /**
     *  Login user call
     *
     * @param accessTokenRequest request data
     * @return Access-Token data
     */
    @POST("oauth/token")
    Call<OAuth2AccessToken> getAccessTokenData(@Body AccessTokenRequest accessTokenRequest);

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

}