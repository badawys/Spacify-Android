package co.broccli.logic;

import android.content.Context;
import android.util.Log;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;

class Offline {

    /**
     *  Offline class name (to be used as Tag in logging)
     */
    private static final String TAG = Offline.class.getSimpleName();

    /**
     * The Offline class instance
     */
    private static Offline mInstance = null;

    /**
     * Initialize new instance of Offline class
     *
     * @param context the context of current state of the application
     */
    static void init(Context context) {
        if(mInstance == null)
        {
            mInstance = new Offline(context);
        }
    }

    /**
     * Offline class Constructor
     *
     * @param context he context of current state of the application
     */
    private Offline(Context context) {
        try {
            Reservoir.init(context, 51200); //in bytes
        } catch (Exception e) {
            Log.w(
                    TAG,
                    "Reservoir:" +  e.getMessage()
            );
        }
    }

    /**
     * Cache an object
     *
     * @param key the key of the data in the cache store
     * @param object object of the data to be cached
     * @param callback method callback
     */
    static void add (String key, Object object , final Callback<Boolean> callback) {
        Reservoir.putAsync(key, object, new ReservoirPutCallback() {
            @Override
            public void onSuccess() {
                callback.onResult(Boolean.TRUE);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    /**
     * Get an cached object
     *
     * @param key the key of the data in the cache store
     * @param targetClass the class of the retrievable object is instance of
     * @param callback method callback
     */
    static <T> void get (String key, final Class<T> targetClass, final Callback<Object> callback) {

        Reservoir.getAsync(key, targetClass, new ReservoirGetCallback<T>() {
            @Override
            public void onSuccess(Object o) {
                callback.onResult(o);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

}
