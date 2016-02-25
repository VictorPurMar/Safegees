package org.safegees.safegees.util;

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
    public static File getKMLFileStore(String fileName){
        try {
            File path = new File(Environment.getExternalStorageDirectory(), "kml");
            path.mkdir();
            File file = new File(path.getAbsolutePath(), fileName);
            return file;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public static File getFileStorePath(String fileName){
        return new File(Environment.getExternalStorageDirectory(), "kml"+File.separator+fileName);
    }

}
