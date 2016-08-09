package org.safegees.safegees.util;

import java.util.ArrayList;

/**
 * Created by victor on 9/8/16.
 */
public class WebViewInfoWebDownloadController {
    public static final String CRISIS_HUB_DEFAULT_URL = "https://www.refugeeinfo.eu/";
    public static final String CRISIS_HUB_SECOND_URL = "https://www.refugeeinfo.eu/slavonski-brod/en/";
    public static final String CRISIS_HUB_THIRD_URL = "https://www.refugeeinfo.eu/germany/en/";



    public static ArrayList<String> getInfoUrlsArrayList(){
        ArrayList<String> infoUrlsArray = new ArrayList<String>();
        infoUrlsArray.add(CRISIS_HUB_DEFAULT_URL);
        infoUrlsArray.add(CRISIS_HUB_SECOND_URL);
        infoUrlsArray.add(CRISIS_HUB_THIRD_URL);

        return  infoUrlsArray;
    }
}
