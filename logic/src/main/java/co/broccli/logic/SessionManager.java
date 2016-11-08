package co.broccli.logic;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // SharedPreference file name
    private static final String PREF_NAME = "ssp";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Access Token (make variable public to access from outside)
    private static final String ACCESS_TOKEN = "AccessToken";

    // Constructor
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String accessToken) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing login value as TRUE
        editor.putString(ACCESS_TOKEN, accessToken);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored Access Token
     */
    public String getLoggedInAccessToken() {

        return pref.getString(ACCESS_TOKEN, null);
    }

    /**
     * Clear session details
     */
    void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     */
    boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}