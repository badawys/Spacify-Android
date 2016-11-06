package co.broccli.logic;

import android.content.Context;
import android.util.Log;

public class SpacifyApi {

    private static SpacifyApi mInstance  = null;
    private  SessionManager sessionManager;
    private static final String TAG = SpacifyApi.class.getSimpleName();
    private static Class<?> mloginActivity;

    public static SpacifyApi initialize (Context context) {
        //Check if SpacifyApi as already been initialized
        if(mInstance == null) {
            mInstance = new SpacifyApi();
            mInstance.initializeSessionManager(context);
        } else {
            Log.w(
                    TAG,
                    "SpacifyApi has already been initialized! `SpacifyApi.initialize(...)` " +
                            "should only be called 1 single time to avoid memory leaks!"
            );
        }
        return mInstance;
    }

    private void initializeSessionManager (Context context) {
        sessionManager = new SessionManager(context);
    }

    public void setLoginActivity (Class<?> loginActivity) {
        mloginActivity = loginActivity;
    }

    public static Auth auth () {
        return Auth.getInstance(mInstance.sessionManager, mloginActivity);
    }
}
