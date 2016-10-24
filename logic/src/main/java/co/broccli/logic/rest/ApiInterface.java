package co.broccli.logic.rest;

import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.model.protectedPath.protectedPath;
import retrofit2.Call;
import retrofit2.http.Body;
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
     *
     * @return
     */
    @POST("oauth/token")
    Call<OAuth2AccessToken> getAccessTokenData(@Body AccessTokenRequest accessTokenRequest);
}