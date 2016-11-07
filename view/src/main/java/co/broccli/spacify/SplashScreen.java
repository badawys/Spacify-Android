package co.broccli.spacify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import co.broccli.logic.SessionManager;
import co.broccli.logic.SpacifyApi;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(SpacifyApi.auth().needToBeLoggedIn(this)) {
            //There is a user logedin, so redirect to StartActivity
            startActivity(new Intent(SplashScreen.this, StartActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            finish();
        }
    }
}
