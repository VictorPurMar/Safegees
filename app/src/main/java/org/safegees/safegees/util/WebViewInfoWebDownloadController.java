package org.safegees.safegees.util;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by victor on 9/8/16.
 */
public class WebViewInfoWebDownloadController {

    public static final String CRISIS_HUB_DEFAULT_URL = "https://www.refugeeinfo.eu/";
    public static final String CRISIS_HUB_DEFAULT_URL_EN = CRISIS_HUB_DEFAULT_URL + "?language=en";
    public static final String CRISIS_HUB_DEFAULT_URL_AR = CRISIS_HUB_DEFAULT_URL + "?language=ar";
    public static final String CRISIS_HUB_DEFAULT_URL_FA = CRISIS_HUB_DEFAULT_URL + "?language=fa";


    //SLOVENIA
    public static final String CH_SLOVENIA_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "slovenia/";
    public static final String CH_SLOVENIA_DOBOVA_URL = CRISIS_HUB_DEFAULT_URL + "dobova/";

    //CROATIA
    public static final String CH_CROATIA_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "slovenia/";

    public static final String CH_CROATIA_SLAVONSKI_URL = CRISIS_HUB_DEFAULT_URL + "slavonski-brod/";
    //ITALY
    public static final String CH_ITALY_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "italy/";
    public static final String CH_ITALY_DOBOVA_URL = CRISIS_HUB_DEFAULT_URL + "dobova/";
    public static final String CH_ITALY_POZALLO_URL = CRISIS_HUB_DEFAULT_URL + "pozzallo/";
    public static final String CH_ITALY_LAMPEDUSA_URL = CRISIS_HUB_DEFAULT_URL + "lampedusa";
    //SERBIA
    public static final String CH_SERBIA_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "serbia/";
    public static final String CH_SERBIA_SID_URL = CRISIS_HUB_DEFAULT_URL + "sid/";
    public static final String CH_SERBIA_BELGRADE_URL = CRISIS_HUB_DEFAULT_URL + "belgrade/";
    public static final String CH_SERBIA_PRESEVO_URL = CRISIS_HUB_DEFAULT_URL + "presevo/";
    public static final String CH_SERBIA_DIMITROVGRAD_URL = CRISIS_HUB_DEFAULT_URL + "dimitrovgrad/";
    //GERMANY
    public static final String CH_GERMANY_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "germany/";
    //MACEDONIA
    public static final String CH_MACEDONIA_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "fyrm/";
    public static final String CH_MACEDONIA_TABANOVCE_URL = CRISIS_HUB_DEFAULT_URL + "tabanovce/";
    public static final String CH_MACEDONIA_GEVGELIJA_URL = CRISIS_HUB_DEFAULT_URL + "gevgelija/";
    //GREECE
    public static final String CH_GREECE_GENERAL_URL = CRISIS_HUB_DEFAULT_URL + "greece/";
    public static final String CH_GREECE_CHERSO_URL = CRISIS_HUB_DEFAULT_URL + "cherso/";
    public static final String CH_GREECE_NEA_KAVALA_URL = CRISIS_HUB_DEFAULT_URL + "nea-kavala/";
    public static final String CH_GREECE_IDOMENI_URL = CRISIS_HUB_DEFAULT_URL + "idomeni/";
    public static final String CH_GREECE_DIAVATA_URL = CRISIS_HUB_DEFAULT_URL + "diavata/";
    public static final String CH_GREECE_ALEXANDREIA_URL = CRISIS_HUB_DEFAULT_URL + "alexandreia/";
    public static final String CH_GREECE_RITSONA_URL = CRISIS_HUB_DEFAULT_URL + "ritsona/";
    public static final String CH_GREECE_ATHENS_URL = CRISIS_HUB_DEFAULT_URL + "athens/";
    public static final String CH_GREECE_LESVOS_URL = CRISIS_HUB_DEFAULT_URL + "lesvos/";
    public static final String CH_GREECE_CHIOS_URL = CRISIS_HUB_DEFAULT_URL + "chios/";
    public static final String CH_GREECE_SAMOS_URL = CRISIS_HUB_DEFAULT_URL + "samos/";
    public static final String CH_GREECE_KOS_URL = CRISIS_HUB_DEFAULT_URL + "chios/";


    //GENERAL
    public static final String CH_STAYING_SAFE = CRISIS_HUB_DEFAULT_URL + "common-messaging-staying-safe";
    public static final String CH_FAMILIES = CRISIS_HUB_DEFAULT_URL + "families";
    public static final String CH_MINORS = CRISIS_HUB_DEFAULT_URL + "minors";


    public static final String CH_LANGUAGE_EN = "en/";
    public static final String CH_LANGUAGE_AR = "ar/";
    public static final String CH_LANGUAGE_FA = "fa/";


    public static String language;


    public static ArrayList<String> getInfoUrlsArrayList(){

        //Language
        //Get device language
        language = Locale.getDefault().getDisplayLanguage();

        ArrayList<String> infoUrlsArray = new ArrayList<String>();
        infoUrlsArray.add(CRISIS_HUB_DEFAULT_URL);

        if (language.equals("fa")){
            infoUrlsArray.add(CRISIS_HUB_DEFAULT_URL_FA);
        }else if (language.equals("ar")){
            infoUrlsArray.add(CRISIS_HUB_DEFAULT_URL_AR);
        }else{
            infoUrlsArray.add(CRISIS_HUB_DEFAULT_URL_EN);
        }


        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SLOVENIA_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SLOVENIA_DOBOVA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_CROATIA_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_CROATIA_SLAVONSKI_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_ITALY_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_ITALY_DOBOVA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_ITALY_POZALLO_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_ITALY_LAMPEDUSA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SERBIA_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SERBIA_SID_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SERBIA_BELGRADE_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SERBIA_PRESEVO_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_SERBIA_DIMITROVGRAD_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GERMANY_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_MACEDONIA_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_MACEDONIA_TABANOVCE_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_MACEDONIA_GEVGELIJA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_GENERAL_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_CHERSO_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_NEA_KAVALA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_IDOMENI_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_DIAVATA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_ALEXANDREIA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_RITSONA_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_ATHENS_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_LESVOS_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_CHIOS_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_SAMOS_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_GREECE_KOS_URL);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_STAYING_SAFE);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_FAMILIES);
        infoUrlsArray = addThreeLenguagesUrl(infoUrlsArray, CH_MINORS);
        infoUrlsArray.add(CH_STAYING_SAFE);
        infoUrlsArray.add(CH_FAMILIES);
        infoUrlsArray.add(CH_MINORS);
        return  infoUrlsArray;
    }

    public static ArrayList<String> addThreeLenguagesUrl(ArrayList<String> infoUrlsArray, String url){

        //Download info only in device language
        if (language.equals("fa")){
            infoUrlsArray.add(url + CH_LANGUAGE_FA);
        }else if (language.equals("ar")){
            infoUrlsArray.add(url + CH_LANGUAGE_AR);
        }else{
            infoUrlsArray.add(url + CH_LANGUAGE_EN);
        }




        return infoUrlsArray;
    }
}
