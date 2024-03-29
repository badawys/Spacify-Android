package co.broccli.logic;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.HashMap;

import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.profile.User;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.*;

public class Profile {
    /**
     * The Auth class instance
     */
    private static Profile mInstance = null;

    /**
     *  Profile class name
     */
    private static final String TAG = Profile.class.getSimpleName();

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * This method is used to create new instance of the
     * Auth class so it can be used as singleton class
     *
     * @return new instance of Auth class
     */
    static Profile getInstance() {
        if(mInstance == null)
        {
            mInstance = new Profile();
        }
        return mInstance;
    }


    /**
     *  Get the profile data of the loggedin user
     *
     * @param _context the context of current state of the application
     * @param callback  method callback
     */
    public void getUserProfile (final Context _context,
                                    final Callback<User> callback) {

        // First: get the offline data (if existed) asynchronously
        Offline.get(TAG, User.class, new Callback<Object>() {
            @Override
            public void onResult(Object o) {
                // if there is a cached copy of User object return it to the client
                if (o instanceof User)
                    callback.onResult((User) o);
            }

            @Override
            public void onError(String errorMessage) {
                // Error handling TODO: Logging
            }
        });

        // Second: get the online data
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<User> call = apiService.getUser();
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // if there is no error from the server, return the data to the
                // client and cache the new data for the offline use
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                    Offline.add(TAG, response.body(), new Callback<Boolean>() {
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
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    /**
     *  Edit the profile data of the loggedin user
     *
     * @param _context the context of current state of the application
     * @param callback  method callback
     */
    public void editUserProfile (final Context _context,
                                 String name,
                                 String email,
                                 Uri photo,
                                 final Callback<User> callback) {

        MultipartBody.Part body = null;

        if (photo != null) {
            body = prepareFilePart("photo", photo);
        }

        RequestBody nName = createPartFromString(name);
        RequestBody nEmail = createPartFromString(email);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", nName);
        map.put("email", nEmail);

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, _context);
        Call<User> call = apiService.editUser("PATCH", map, body);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // if there is no error from the server, return the data to the
                // client and cache the new data for the offline use
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                    Offline.add(TAG, response.body(), new Callback<Boolean>() {
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
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
