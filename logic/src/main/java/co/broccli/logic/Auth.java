package co.broccli.logic;

import android.content.Context;
import android.content.Intent;
import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.model.signup.Signup;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth {

    /**
     * The Auth class instance
     */
    private static Auth mInstance = null;

    /**
     * The Session Manager instance
     */
    private static SessionManager mSessionManager;

    /**
     * The reference of the login activity class
     */
    private static Class<?> mLoginActivity;

    /**
     * This method is used to create new instance of the
     * Auth class so it can be used as singleton class
     *
     * @param sessionManager SessionManager instance
     * @param loginActivity Class of the login activity
     * @return new instance of Auth class
     */
    static Auth getInstance(SessionManager sessionManager, Class<?> loginActivity) {
        if(mInstance == null)
        {
            mInstance = new Auth(sessionManager, loginActivity);
        }
        return mInstance;
    }

    /**
     *  Auth class Constructor
     *
     * @param sessionManager SessionManager instance
     * @param loginActivity Class of the login activity
     */
    private Auth(SessionManager sessionManager, Class<?> loginActivity) {
        mSessionManager = sessionManager;
        mLoginActivity = loginActivity;
    }

    /**
     *  This method is to check if there is a loggedin user
     *
     * @return True if an user is loggedin and False if not
     */
    private static boolean isLoggedIn () {
        return mSessionManager.isLoggedIn();
    }

    /**
     *  This method can be used to restrict access to only loggedin users
     *
     * @param _context the context of current state of the application
     * @return False and Navigate the app the login activity if the user need to be loggedin
     */
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

    /**
     * Logout user from current active session
     *
     * @param _context the context of current state of the application
     */
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

    /**
     * Login user using user's email and password
     *
     * @param _context the context of current state of the application
     * @param email user login email
     * @param password user login password
     * @param callback method callback
     */
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

    /**
     *  Signup new user
     *
     * @param _context the context of current state of the application
     * @param name new user name
     * @param email new user email
     * @param password new user password
     * @param callback method callback
     */
    public void signup (final Context _context,
                        String name,
                        String email,
                        String password,
                        final co.broccli.logic.Callback<Boolean> callback) {

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<Signup> call =
                apiService.signup(name, email, password);
        call.enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response)  {
                if (response.isSuccessful()) {
                        callback.onResult(Boolean.TRUE);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onError(error.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Signup> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
