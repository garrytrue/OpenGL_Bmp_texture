package com.garrytrue.tryopengl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tiv on 04.03.2016.
 */
public class ImageUtils {
//    public static void loadBitmapFromResource(Target target/*, int width, int height*/) throws IOException {
//        Picasso.with(OpenGLApp.getContext()).load("file:///android_asset/floor_plan.png")./*transform(new ImageResizer(width, height)).*/into(target);
//    }

    public static Bitmap loadFromAsserts() {
        AssetManager assetManager = OpenGLApp.getContext().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("floor_plan.png");
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    private static class ImageResizer implements Transformation {
//        private int mWidth;
//        private int mHeight;
//
//        public ImageResizer(int mWidth, int mHeight) {
//            this.mWidth = mWidth;
//            this.mHeight = mHeight;
//        }
//
//        @Override
//        public Bitmap transform(Bitmap source) {
//            Matrix m = new Matrix();
//            m.setRectToRect(new RectF(0, 0, source.getWidth(), source.getHeight()), new RectF(0, 0, mWidth, mHeight), Matrix.ScaleToFit.CENTER);
//            Bitmap scaled = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
//            if (source != null) {
//                source.recycle();
//            }
//            return scaled;
//        }
//
//        @Override
//        public String key() {
//            return "ImageResizer. Expected dimension [" + mWidth + ", " + mHeight + "]";
//        }
//    }
}
