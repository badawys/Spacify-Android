package co.broccli.logic;

import android.content.Context;

import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.profile.User;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import retrofit2.*;

public class Profile {
    /**
     * The Auth class instance
     */
    private static Profile mInstance = null;

    /**
     * The Session Manager instance
     */
    private static SessionManager mSessionManager;

    /**
     * This method is used to create new instance of the
     * Auth class so it can be used as singleton class
     *
     * @param sessionManager SessionManager instance
     * @return new instance of Auth class
     */
    static Profile getInstance(SessionManager sessionManager) {
        if(mInstance == null)
        {
            mInstance = new Profile(sessionManager);
        }
        return mInstance;
    }

    /**
     *  Auth class Constructor
     *
     * @param sessionManager SessionManager instance
     */
    private Profile(SessionManager sessionManager) {
        mSessionManager = sessionManager;
    }

    /**
     *  Get the profile data of the loggedin user
     *
     * @param _context the context of current state of the application
     * @param callback  method callback
     */
    public void getUserProfile (final Context _context,
                                    final Callback<User> callback) {
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<User> call = apiService.getUser();
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    callback.onError(error.getMessage());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
