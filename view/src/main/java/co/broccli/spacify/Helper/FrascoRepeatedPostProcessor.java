package co.broccli.spacify.Helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.BaseRepeatedPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class FrascoRepeatedPostProcessor extends BaseRepeatedPostProcessor {

    List<BasePostprocessor> postprocessors = new ArrayList<>();

    public void apply(BasePostprocessor postprocessor) {
        postprocessors.add(postprocessor);
        update();
    }

    @Override
    public void process(Bitmap dest, Bitmap source) {
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        canvas.drawBitmap(source, 0, 0, paint);

        for (BasePostprocessor postprocessor : postprocessors) {
            postprocessor.process(dest, dest);
        }
    }
}