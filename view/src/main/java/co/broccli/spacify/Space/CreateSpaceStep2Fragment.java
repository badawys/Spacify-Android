package co.broccli.spacify.Space;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.List;
import co.broccli.spacify.R;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class CreateSpaceStep2Fragment extends Fragment implements ISlidePolicy {

    private SimpleDraweeView _SpacePhoto;
    private EditText _SpaceName;
    private RadioGroup _SpaceType;
    private LinearLayout _SpacePhotoButton;

    protected String SpaceName = null;
    protected Uri SpacePhoto = null;
    protected int SpaceType = 0;

    public CreateSpaceStep2Fragment() {
        // Required empty public constructor
    }

    public static CreateSpaceStep2Fragment newInstance() {
        CreateSpaceStep2Fragment fragment = new CreateSpaceStep2Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_space_step2, container, false);

        _SpacePhoto = (SimpleDraweeView) view.findViewById(R.id.space_pic_view);
        _SpaceName = (EditText) view.findViewById(R.id.space_name_text_box);
        _SpaceType = (RadioGroup) view.findViewById(R.id.space_type_radio_group);
        _SpacePhotoButton = (LinearLayout) view.findViewById(R.id.add_space_photo_button);

        _SpacePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(CreateSpaceStep2Fragment.this,
                        getString(R.string.select_image), 0);
            }
        });
        _SpaceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.space_type_public)
                    SpaceType = 1;
                if (checkedId == R.id.space_type_private)
                    SpaceType = 2;
            }
        });
        _SpaceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (_SpaceName.length() < 1)
                    SpaceName = null;
                else
                    SpaceName = _SpaceName.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        return view;
    }

    private void startCrop (File imageFile) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        options.withMaxResultSize(200,200);
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
                .start(getActivity(), CreateSpaceStep2Fragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            final Uri resultUri = UCrop.getOutput(data);
            setSpacePhoto(resultUri.toString());
            SpacePhoto = resultUri;

        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext() , cropError.getMessage(), Toast.LENGTH_SHORT).show();

        } else {

            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(),
                    new DefaultCallback() {
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

    private void setSpacePhoto (String url) {

        Uri uri = Uri.parse(url);

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();

        PipelineDraweeController photoController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(_SpacePhoto.getController())
                        .build();

        _SpacePhoto.setController(photoController);
    }

    @Override
    public boolean isPolicyRespected() {
        return (SpaceName != null) && (SpaceType != 0);
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        String msg = getString(R.string.an_error_is_happened);

        if (SpaceName == null)
            msg = getString(R.string.create_space_name_is_required);
        else if (SpaceType == 0)
            msg = getString(R.string.create_space_type_is_required);

        if (getView() != null)
            Snackbar.make(getView(), msg,Snackbar.LENGTH_SHORT).show();
    }
}
