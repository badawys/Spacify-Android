package co.broccli.logic.util;

import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Util {

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody createPartFromString(String partString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), partString);
    }

    public static RequestBody createPartFromInt(int partInt) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), Integer.toString(partInt));
    }

    public static RequestBody createPartFromDouble(Double partDouble) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), Double.toString(partDouble));
    }

    public static MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
