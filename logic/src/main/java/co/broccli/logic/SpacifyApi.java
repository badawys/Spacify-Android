package co.broccli.logic;

import android.content.Context;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;

public class SpacifyApi {

    /**
     * The SpacifyApi class instance
     */
    private static SpacifyApi mInstance  = null;

    /**
     * The Session Manager instance
     */
    private  SessionManager sessionManager;

    /**
     *  SpacifyApi class name (to be used as Tag in logging)
     */
    private static final String TAG = SpacifyApi.class.getSimpleName();

    /**
     * The reference of the login activity class
     */
    private static Class<?> mloginActivity;

    /**
     *  Initialize new instance of SpacifyApi class
     *
     * @param context the context of current state of the application
     * @return the singleton instance of the SpacifyApi class
     */
    public static SpacifyApi initialize (Context context) {
        //Check if SpacifyApi as already been initialized
        if(mInstance == null) {
            mInstance = new SpacifyApi();
            mInstance.initializeSessionManager(context);
            Offline.init(context);
        } else {
            Log.w(
                    TAG,
                    "SpacifyApi has already been initialized! `SpacifyApi.initialize(...)` " +
                            "should only be called 1 single time to avoid memory leaks!"
            );
        }
        return mInstance;
    }

    /**
     * Initialize new instance of SessionManager class
     *
     * @param context the context of current state of the application
     */
    private void initializeSessionManager (Context context) {
        sessionManager = new SessionManager(context);
    }

    /**
     *  Set the Login Activity reference
     *
     * @param loginActivity set reference of the login activity class
     */
    public void setLoginActivity (Class<?> loginActivity) {
        mloginActivity = loginActivity;
    }

    /**
     * All the authentication and authorization operations
     *
     * @return instance if Auth class
     */
    public static Auth auth () {
        return Auth.getInstance(mInstance.sessionManager, mloginActivity);
    }

    /**
     * All the user profile related operations
     *
     * @return instance if Auth class
     */
    public static Profile profile () {
        return Profile.getInstance();
    }
}
