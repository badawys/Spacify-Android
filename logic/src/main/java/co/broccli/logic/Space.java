package co.broccli.logic;


import android.content.Context;
import android.net.Uri;
import java.util.HashMap;
import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.space.CreateSpace;
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
}
