package co.broccli.spacify;


import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import co.broccli.logic.SpacifyApi;
import co.broccli.spacify.Auth.LoginActivity;

public class SpacifyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(
                    Thread paramThread,
                    Throwable paramThrowable
            ) {
                Log.e(this.getClass().getSimpleName(), "Thread and exception details:", paramThrowable);
                paramThrowable.printStackTrace();
                if (oldHandler != null)
                    oldHandler.uncaughtException(
                            paramThread,
                            paramThrowable
                    ); //Delegates to Android's error handling
                else
                    System.exit(2); //Prevents the service/app from freezing
            }
        });

        Fresco.initialize(this);

        SpacifyApi.initialize(this)
                .setLoginActivity(LoginActivity.class);
    }
}
