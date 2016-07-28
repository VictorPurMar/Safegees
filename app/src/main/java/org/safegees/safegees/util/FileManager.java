package org.safegees.safegees.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by victor on 25/2/16.
 */
public class FileManager {

    //Change to store in Android/data/package
    /**
     * Get the default path for KML file on Android: on the external storage, in a "kml" directory.
     * Creates the directory if necessary.
     * @param fileName
     * @return full path, as a File, or null if error.
     */
    public static File getKMLFileStore(String fileName, Context context){
        try {
            File path = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName() +File.separator +"kml");
            path.mkdir();
            File file = new File(path.getAbsolutePath(), fileName);
            return file;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getFileExists(String fileName, Context context){
        boolean ret = false;
        try {
            File path = new File(Environment.getExternalStorageDirectory(),context.getApplicationContext().getPackageName() +File.separator + "kml"+File.separator+fileName);
            if (path.exists()) ret = true;
            else {
                File path2 = new File(Environment.getExternalStorageDirectory(), context.getApplicationContext().getPackageName() + File.separator + "kml" + File.separator);
                path2.mkdirs();
            }
        } catch (NullPointerException e){

        }
        return ret;
    }

    public static File getFileStorePath(String fileName, Context context){
        return new File(Environment.getExternalStorageDirectory(),context.getApplicationContext().getPackageName() +File.separator + "kml"+File.separator+fileName);
    }

}
