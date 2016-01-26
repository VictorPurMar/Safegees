package org.safegees.safegees.maps;

import android.content.res.AssetManager;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import org.safegees.safegees.gui.view.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CustomMapTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;
    private AssetManager mAssets;
    Tile previousTile;

    public CustomMapTileProvider(AssetManager assets) {
        mAssets = assets;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {

        //Stablish the MAX_ZOOM in Main activity depending the stored Map Tiles
        byte[] image = new byte[0];
            image = readTileImage(x, y, zoom);
        if (image != null){
            //If no image modify the MainActivity MAX_ZOOM
            MainActivity.MAX_ZOOM = zoom+0.9F;
            //Return empty tile
            this.previousTile =  NO_TILE;
        }
        return image == null ? this.previousTile : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }


    //This metod open the images from the local path
    private byte[] readTileImage(int x, int y, int zoom) {
        InputStream in = null;
        ByteArrayOutputStream buffer = null;

        try {
            in = mAssets.open(getTileFilename(x, y, zoom));

            //   previous_displayed_zoom--;
            //    in = mAssets.open(getTileFilename(previous_displayed_x,previous_displayed_y,previous_displayed_zoom));
            //}
            buffer = new ByteArrayOutputStream();


            int nRead;
            byte[] data = new byte[BUFFER_SIZE];

            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignored){
                //Ignored
                }

            }
            if (buffer != null) try {
                buffer.close();

            } catch (Exception ignored) {


            }
        }
    }

    private String getTileFilename(int x, int y, int zoom) {
        return "map/" + zoom + '/' + x + '/' + y + ".jpg";
    }
}
