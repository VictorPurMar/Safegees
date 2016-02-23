package org.safegees.safegees.util;

import android.net.Uri;
import android.util.Log;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.safegees.safegees.R;

import java.io.File;

/**
 * Created by victor on 23/2/16.
 */
public class SafegeesXTTileSource extends OnlineTileSourceBase {

    public SafegeesXTTileSource(final String aName, final int aZoomMinLevel,
                        final int aZoomMaxLevel, final int aTileSizePixels, final String aImageFilenameEnding,
                        final String[] aBaseUrl) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
                aImageFilenameEnding, aBaseUrl);
    }

    @Override
    public String getTileURLString(final MapTile aTile) {
        String externalStorageDirectory =  MapFileManager.getUserStorageriority();
        String destination = externalStorageDirectory + File.separator + "osmdroid" + File.separator + "tiles" + File.separator + "Mapnik";
        String file = destination + aTile.getZoomLevel() + "/" + aTile.getX() + "/" + aTile.getY()
                + mImageFilenameEnding;
        File f = new File(file);
        if(f.exists() && !f.isDirectory()) {
            return getBaseUrl() + aTile.getZoomLevel() + "/" + aTile.getX() + "/" + aTile.getY()
                    + mImageFilenameEnding;
        }
        Uri path = Uri.parse("android.resource://org.safegees.safegees/" + R.drawable.no_tile);
        Log.e("EnTrY", "LO QUE SEA");
        return path.toString();
    }
}
