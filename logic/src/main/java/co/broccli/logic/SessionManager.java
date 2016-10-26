package co.broccli.logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // SharedPreference file name
    private static final String PREF_NAME = "ssp";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Access Token (make variable public to access from outside)
    public static final String ACCESS_TOKEN = "AccessToken";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email, String accessToken) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing login value as TRUE
        editor.putString(ACCESS_TOKEN, accessToken);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin(Class<?> LoginActivty ) {

        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
               Intent i = new Intent(_context, LoginActivty);
               // Closing all the Activities
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

               // Add new Flag to start new Activity
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               // Staring Login Activity
               _context.startActivity(i);

            return true;
        }

        //TODO: for more security check if the access token is valid
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser(Class<?> LoginActivty) {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivty);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}