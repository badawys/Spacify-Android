package co.broccli.logic;


import android.content.Context;
import android.net.Uri;
import java.util.HashMap;
import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.space.CreateSpace;
import co.broccli.logic.model.space.GetSpace;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import static co.broccli.logic.util.Util.createPartFromDouble;
import static co.broccli.logic.util.Util.createPartFromInt;
import static co.broccli.logic.util.Util.createPartFromString;
import static co.broccli.logic.util.Util.prepareFilePart;

public class Space {

    /**
     * The Space class instance
     */
    private static Space mInstance = null;

    /**
     *  Profile class name
     */
    private static final String TAG = Space.class.getSimpleName();

    /**
     * This method is used to create new instance of the
     * Auth class so it can be used as singleton class
     *
     * @return new instance of Auth class
     */
    static Space getInstance() {
        if(mInstance == null)
        {
            mInstance = new Space();
        }
        return mInstance;
    }

    /**
     *  Create new space
     *
     * @param _context the context of current state of the application
     * @param callback  method callback
     */
    public void createNewSpace (final Context _context,
                                 String name,
                                 int type,
                                 double lng,
                                 double lat,
                                 Uri photo,
                                 final Callback<CreateSpace> callback) {

        MultipartBody.Part body = null;

        if (photo != null) {
            body = prepareFilePart("photo", photo);
        }

        RequestBody nName = createPartFromString(name);
        RequestBody nType = createPartFromInt(type);
        RequestBody nLng = createPartFromDouble(lng);
        RequestBody nLat = createPartFromDouble(lat);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", nName);
        map.put("type", nType);
        map.put("lng", nLng);
        map.put("lat", nLat);

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<CreateSpace> call = apiService.createSpace(map, body);
        call.enqueue(new retrofit2.Callback<CreateSpace>() {
            @Override
            public void onResponse(Call<CreateSpace> call, Response<CreateSpace> response) {
                // if there is no error from the server, return the data to the
                // client and cache the new data for the offline use
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                } else {
                    // if there is error from the server, parse the error and
                    // return it to the client
                    APIError error = ErrorUtils.parseError(response);
                    callback.onError(error.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CreateSpace> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    /**
     *  Get the space data
     *
     * @param _context the context of current state of the application
     * @param callback  method callback
     */
    public void getSpace (final Context _context,
                                final int id,
                                final Callback<GetSpace> callback) {

        // First: get the offline data (if existed) asynchronously
        Offline.get(TAG+id, GetSpace.class, new Callback<Object>() {
            @Override
            public void onResult(Object o) {
                // if there is a cached copy of User object return it to the client
                if (o instanceof GetSpace)
                    callback.onResult((GetSpace) o);
            }

            @Override
            public void onError(String errorMessage) {
                // Error handling TODO: Logging
            }
        });

        // Second: get the online data
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<GetSpace> call = apiService.getSpace(id);
        call.enqueue(new retrofit2.Callback<GetSpace>() {
            @Override
            public void onResponse(Call<GetSpace> call, Response<GetSpace> response) {
                // if there is no error from the server, return the data to the
                // client and cache the new data for the offline use
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                    Offline.add(TAG+id, response.body(), new Callback<Boolean>() {
                        @Override
                        public void onResult(Boolean aBoolean) {
                            //Data had been cached successfully
                            // TODO: Logging
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Error handling TODO: Logging
                        }
                    });
                } else {
                    // if there is error from the server, parse the error and
                    // return it to the client
                    APIError error = ErrorUtils.parseError(response);
                    callback.onError(error.getMessage());
                }
            }
            @Override
            public void onFailure(Call<GetSpace> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
