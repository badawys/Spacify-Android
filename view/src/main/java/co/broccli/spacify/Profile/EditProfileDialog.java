package co.broccli.spacify.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.victor.loading.rotate.RotateLoading;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.List;
import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.profile.User;
import co.broccli.spacify.R;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import mehdi.sakout.fancybuttons.FancyButton;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class EditProfileDialog extends SupportBlurDialogFragment {

    private LinearLayout dialogContent;
    private RotateLoading rotateLoading;
    private SimpleDraweeView profilePhoto;
    private EditText userName;
    private EditText email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_profile, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialogContent = (LinearLayout) v.findViewById(R.id.dialog_content);
        LinearLayout changeProfilePic = (LinearLayout) v.findViewById(R.id.change_profile_pic);
        rotateLoading = (RotateLoading) v.findViewById(R.id.rotateloading);
        profilePhoto = (SimpleDraweeView) v.findViewById(R.id.user_pic_view);
        userName = (EditText) v.findViewById(R.id.input_name);
        email = (EditText) v.findViewById(R.id.input_email);
        FancyButton cancelButton = (FancyButton) v.findViewById(R.id.cancelButton);
        FancyButton okButton = (FancyButton) v.findViewById(R.id.okButton);

        EasyImage.configuration(getContext()).setImagesFolderName("profile-pic").saveInAppExternalFilesDir();

        dialogContent.setVisibility(View.INVISIBLE);
        rotateLoading.start();

        SpacifyApi.profile().getUserProfile(getContext(), new Callback<User>() {
            @Override
            public void onResult(User user) {
                setProfilePhoto ("http://spacify.s3.amazonaws.com/" + user.getPhoto());
                userName.setText(user.getName());
                email.setText(user.getEmail());
                rotateLoading.stop();
                dialogContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(EditProfileDialog.this, "Select Image", 0);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }



    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 5;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 7;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return true;
    }
    /**
     * ToDo: Add Description
     */
    private void setProfilePhoto (String url) {

        Uri uri = Uri.parse(url);

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();

        PipelineDraweeController photoController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(profilePhoto.getController())
                        .build();

        profilePhoto.setController(photoController);
    }

    private void startCrop (File imageFile) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setCircleDimmedLayer(true);
        options.setShowCropGrid(false);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.primary));
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary_dark));
        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.primary));
        options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.white));

        //Handle the images
        UCrop.of(
                Uri.fromFile(imageFile),
                Uri.fromFile(new File(
                        getActivity().getExternalCacheDir(),
                        "/EasyImage/" + imageFile.getName())))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(getActivity(), EditProfileDialog.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            setProfilePhoto(resultUri.toString());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext() , cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    Toast.makeText(getContext() , e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                    startCrop(imagesFiles.get(0));
                }
            });
        }
    }
}
