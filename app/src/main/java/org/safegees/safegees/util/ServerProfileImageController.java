package org.safegees.safegees.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by victor on 11/8/16.
 */


public class ServerProfileImageController {

    public static void uploadAnImage(Bitmap bitmap){




    }

    @NonNull
    private static Bitmap bitmapToString(Bitmap bitmap) {
        // get the base 64 string
        String imgString = Base64.encodeToString(ServerProfileImageController.getBytesFromBitmap(bitmap),
                Base64.NO_WRAP);
        //Log.i("IMAGE", imgString);
        //Log.i("IMAGE_SIZE", ""+ imgString.length());

        int maxLogSize = 1000;
        for(int i = 0; i <= imgString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > imgString.length() ? imgString.length() : end;
            Log.v("IMAGE", imgString.substring(start, end));
        }

        //ServerProfileImageController.writeToFile(imgString);

        Bitmap newBitmap = ServerProfileImageController.stringToBitMap(imgString);

        if(bitmap.equals(newBitmap)){
            Log.i("IMAGE","Bitmap equals");
        }
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
