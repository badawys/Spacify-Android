package co.broccli.spacify;


import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class SpacifyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
