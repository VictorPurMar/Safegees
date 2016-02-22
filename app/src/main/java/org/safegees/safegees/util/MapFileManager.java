package org.safegees.safegees.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.safegees.safegees.gui.view.SplashActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by victor on 22/2/16.
 */
public class MapFileManager {

    public static boolean isSDCard(Context context){
        String strSDCardPath = System.getenv("SECONDARY_STORAGE");
        if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
            return false;
        }
        return true;
    }

    public static boolean mapExists(Context context) {


        String externalStorageDirectory =  getUserStorageriority();

        String destination = externalStorageDirectory + File.separator + "osmdroid" + File.separator + "tiles" + File.separator + "SafegeesMap";
        File desFile = new File(destination);
        // 16788000 is the default size of maps zip
        if (desFile.exists() && desFile.isDirectory() && desFile.length() > 1000) {
            return true;
        } else {
            String principalDestination = externalStorageDirectory + File.separator + "osmdroid";
            File principalFile = new File(principalDestination);
            principalFile.mkdirs();
            InputStream is = null;
            try {
                is = context.getAssets().open("SafegeesMap.zip");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                File f = new File(principalFile.getAbsolutePath().toString() + File.separator + "SafegeesMap.zip");

                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();


                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
                checkAndUnZipTilesFile();
                return true;
            } catch (IOException e) {
                //       e.printStackT
                //Map doesnt existrace();
            }

        }
        return false;
    }

    public static String getUserStorageriority() {
        boolean isSdCard = SplashActivity.DATA_STORAGE.getBoolean("SDCard");
        String strSDCardPath = "";
        if (isSdCard) {
            strSDCardPath = System.getenv("SECONDARY_STORAGE");

            if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
                strSDCardPath = System.getenv("EXTERNAL_SDCARD_STORAGE");
            }

            //If may get a full path that is not the right one, even if we don't have the SD Card there.
            //We just need the "/mnt/extSdCard/" i.e and check if it's writable
            if(strSDCardPath != null) {
                if (strSDCardPath.contains(":")) {
                    strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
                }
                File externalFilePath = new File(strSDCardPath);

                if (externalFilePath.exists() && externalFilePath.canWrite()){
                    return strSDCardPath;
                }
            }
        }else{
            strSDCardPath = Environment.getExternalStorageDirectory().toString();
        }

        return strSDCardPath;
    }



    public static boolean checkAndUnZipTilesFile(){
        String externalStorageDirectory = getUserStorageriority();
        boolean zipUnfilled = false;
        Log.i("Target" , externalStorageDirectory+File.separator+"osmdroid"+File.separator+"SafegeesMap.zip");
        Log.i("Destination" , externalStorageDirectory+File.separator+"osmdroid"+File.separator+"tiles"+File.separator);

        String target =  externalStorageDirectory+File.separator+"osmdroid"+File.separator+"SafegeesMap.zip";
        String destination = externalStorageDirectory+File.separator+"osmdroid"+File.separator+"tiles"+File.separator;

        File desFile = new File(destination);

        File zipFile = new File(target);

        if(zipFile.exists() && !zipFile.isDirectory()) {
            desFile.mkdirs();
            desFile.setReadable(true);
            String osmdroid =  externalStorageDirectory+File.separator+"osmdroid";
            String tiles =  externalStorageDirectory+File.separator+"osmdroid"+File.separator+"tiles";
            File osmDroidFile = new File(osmdroid);
            File tilesFile = new File(tiles);
            osmDroidFile.setWritable(true);
            tilesFile.setWritable(true);


            try {
               zipUnfilled =  MapFileManager.unzip(zipFile,desFile);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        //Delete unused zip
        zipFile.delete();
        return zipUnfilled;
    }

    public static boolean unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
            return true;
        }
    }
}
