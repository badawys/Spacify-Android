package co.broccli.logic;

import android.content.Context;
import android.content.Intent;

import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth {

    private static Auth mInstance = null;
    private static SessionManager mSessionManager;
    private static Class<?> mLoginActivity;

    static Auth getInstance(SessionManager sessionManager, Class<?> loginActivity) {
        if(mInstance == null)
        {
            mInstance = new Auth(sessionManager, loginActivity);
        }
        return mInstance;
    }

    private Auth(SessionManager sessionManager, Class<?> loginActivity) {
        mSessionManager = sessionManager;
        mLoginActivity = loginActivity;
    }

    private static boolean isLoggedIn () {
        return mSessionManager.isLoggedIn();
    }

    public boolean needToBeLoggedIn (Context _context) {
        if (!isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, mLoginActivity);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return false;
        }
        return true;
    }

    public void logout (Context _context) {
        mSessionManager.logoutUser();

        // Logout user from the server (Revoke Access Token)
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<ResponseBody> call = apiService.logout();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do nothing
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Do nothing
            }
        });

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, mLoginActivity);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void login (final Context _context,
                       String email,
                       String password,
                       final co.broccli.logic.Callback<Boolean> callback) {

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);

        Call<OAuth2AccessToken> call =
                apiService.getAccessTokenData(new AccessTokenRequest(email, password));

        call.enqueue(new Callback<OAuth2AccessToken>() {
            @Override
            public void onResponse(Call<OAuth2AccessToken> call, Response<OAuth2AccessToken> response)  {
                if (response.isSuccessful()) {
                    if (!response.body().getAccessToken().isEmpty()) {
                        mSessionManager.createLoginSession(response.body().getAccessToken());
                        callback.onResult(Boolean.TRUE);
                    } else {
                        callback.onError("Login failed, Try again later");
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onError(error.getMessage());
                }
            }
            @Override
            public void onFailure(Call<OAuth2AccessToken> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
