package org.safegees.safegees.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by victor on 15/8/16.
 */
public class ImageController {
    public static Bitmap bitmap;

    public static Bitmap getUserImageBitmap(Context context) {
        bitmap = null; //Deallocate previous stored images
        try {
            String filename = getUserImageFileName(context);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(filename, options);

            if(bitmap!=null) {
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("IMAGE ERROR", e.getMessage());
        }
        return null;
    }

    public static Bitmap getContactImageBitmap(Context context,String email) {
        bitmap = null; //Deallocate previous stored images
        try {
            String filename = getUserImageFileNameByEmail(context,email);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(filename, options);

            if(bitmap!=null) {
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("IMAGE ERROR", e.getMessage());
        }
        //Return default i
        if (bitmap == null) bitmap = getBitmapFromAsset(context);

        return null;
    }

    public static Bitmap getBitmapFromAsset(Context context) {
        Drawable myDrawable = context.getResources().getDrawable(R.drawable.default_user_rounded);
        Bitmap bitmap = ((BitmapDrawable) myDrawable).getBitmap();
        return bitmap;
    }

    public static void storeUserImage(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName()
                + "/images");


        /*
                new File(Environment.getRootDirectory()
                + File.separator + getApplicationContext().getPackageName()
                + "/images");*/
        String filename = "USER_IMAGE_" + MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)).replace("@","_").replace(".","_") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;

        boolean success = true;
        if (!mediaStorageDir.exists()) {
            success = mediaStorageDir.mkdirs();
            //success = mediaStorageDir.mkdir();
        }
        if (success) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, out); // bmp is your Bitmap instance

                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Error", "File cant be created");
        }
    }

    public static void storeUserBitmapWithEmail(Context context, Bitmap bitmap, String email) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName()
                + "/images");


        /*
                new File(Environment.getRootDirectory()
                + File.separator + getApplicationContext().getPackageName()
                + "/images");*/
        String filename = "USER_IMAGE_" +email.replace("@","_").replace(".","_") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;

        boolean success = true;
        if (!mediaStorageDir.exists()) {
            success = mediaStorageDir.mkdirs();
            //success = mediaStorageDir.mkdir();
        }
        if (success) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, out); // bmp is your Bitmap instance

                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Error", "File cant be created");
        }
    }

    public static Bitmap buildBitmapFromData(Context context, Uri data){
        try {
            // We need to recyle unused bitmaps
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }

            InputStream stream = context.getContentResolver().openInputStream(
                    data);
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
                /*
                getSupportFragmentManager()
                imageView.setImageBitmap(bitmap);
                */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap = fixBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap fixBitmap(Bitmap bitmap) {

        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        //matrix.postRotate(90);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        if (bitmap.getWidth() >= bitmap.getHeight()){

            bitmap = Bitmap.createBitmap(
                    rotatedBitmap,
                    rotatedBitmap.getWidth()/2 - rotatedBitmap.getHeight()/2,
                    0,
                    rotatedBitmap.getHeight(),
                    rotatedBitmap.getHeight()
            );

        }else{

            bitmap = Bitmap.createBitmap(
                    rotatedBitmap,
                    0,
                    rotatedBitmap.getHeight()/2 - rotatedBitmap.getWidth()/2,
                    rotatedBitmap.getWidth(),
                    rotatedBitmap.getWidth()
            );
        }


        //Rounded the bitmap
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;


        paint.setAntiAlias(true);
        //canvas.drawARGB(0, 0, 0, 0);
        //paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //Scale squared
        output = Bitmap.createScaledBitmap(output, 300, 300, true);

        return output;

    }

    @NonNull
    public static String getUserImageFileName(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName()
                + "/images");
        String filename = "USER_IMAGE_" + MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)).replace("@","_").replace(".","_") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;
        return filename;
    }

    @NonNull
    public static String getUserImageFileNameByEmail(Context context, String email) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName()
                + "/images");
        String filename = "USER_IMAGE_" + email.replace("@","_").replace(".","_") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;
        return filename;
    }

    public static File getUserImageFile(Context context){
        return new File(ImageController.getUserImageFileName(context));
    }

    @NonNull
    private Bitmap getBitmap(Context context, int drawable) {
        Bitmap bitmap;
        int px = context.getResources().getDimensionPixelOffset(R.dimen.map_dot_marker_size);
        bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable shape = context.getResources().getDrawable(drawable);
        shape.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        shape.draw(canvas);
        return bitmap;
    }

    private Paint paintSurface() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(16);
        paint.setAlpha(100);
        return paint;
    }
}
