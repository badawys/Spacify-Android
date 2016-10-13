package co.broccli.spacify;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        int SPLASH_DISPLAY_LENGTH = 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                SplashScreen.this.overridePendingTransition(0, 0);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
