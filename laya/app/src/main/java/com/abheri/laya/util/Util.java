package com.abheri.laya.util;

/**
 * Created by prasanna.ramaswamy on 04/02/18.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.abheri.laya.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * use 10.0.3.2 for IP address if using localhost for servers and
 * client is Genymotion
 */
public class Util {

    //Constants
    public static final int HOW_OLD = 3;

    public static final String HOME_SCREEN = "Home";


    public static final long AUTO_REFRESH_INTERVAL = 1 * 24 * 60 * 60 * 1000;//7*24*60*60*1000;


    /*
    public static ArrayList<String> productSubscriptions = new ArrayList<String>(
            Arrays.asList("com.abheri.laya.monthlysubscription",
                    "monthly_discounted", "com.abheri.laya.yearlysubscription")); */

    //-----------------


    /**
     * @param oper
     * @return
     */
    public static String getServiceUrl(LayamOperations oper) {

        String url = "";

        switch (oper) {

            case LOGIN:
                url = "https://sunaad-services-njs.herokuapp.com/loginUser/";
                if(BuildConfig.DEBUG){
                    //url += "?DEBUG";
                }
                break;
            case SIGNUP:
                url = "https://sunaad-services-njs.herokuapp.com/signupLayamUser/";
                if(BuildConfig.DEBUG){
                    //url += "?DEBUG";
                }
                break;

            case GETSUBSCRIPTIONS:
                url = "https://sunaad-services-njs.herokuapp.com/getSubscriptionTypes/";
                if(BuildConfig.DEBUG){
                    //url += "?DEBUG";
                }
                break;
            default:
                break;
        }

        return url;
    }


    public static String getImageUrl() {

        String url = "";

        url = "http://abheri.pythonanywhere.com/static/images/";

        return url;
    }

    public void myToastMessage(android.content.Context context, String message) {

        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!networkAvailable)
            myToastMessage(context, "Network not available. Using stored data");

        return networkAvailable;

    }



    public static boolean isYes(String inStr) {
        if (inStr.equalsIgnoreCase("Y") ||
                inStr.equalsIgnoreCase("YES")) {
            return true;
        }

        return false;
    }

    public static boolean isNo(String inStr) {
        if (inStr.equalsIgnoreCase("N") ||
                inStr.equalsIgnoreCase("NO")) {
            return true;
        }

        return false;
    }


    public static String getFormattedDate(Date dt){

        DateFormat tf = new SimpleDateFormat("dd-MMM-yyyy");
        String returnStr = tf.format(dt.getTime()).toString();

        return returnStr;

    }

    public static String getFormattedDateTime(Date dt){

        DateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tf.setTimeZone(TimeZone.getTimeZone("GMT"));

        String returnStr = tf.format(dt.getTime()).toString();

        return returnStr;

    }



    /*public static void logToGA(String what) {
        Tracker mTracker;

        //Log to Google Analytics only when the build type = Release
        if (!BuildConfig.DEBUG) {
            // Obtain the shared Tracker instance.
            AnalyticsApplication application = (AnalyticsApplication) new AnalyticsApplication();
            mTracker = application.getDefaultTracker();
            Log.i("Sunaad", "Setting screen name: " + what);
            mTracker.setScreenName("Image~" + what);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Share")
                    .build());
        }
    }*/


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

        /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

    public static String encodeToBase64(String instr) {
        String base64="";
        try{
            byte[] data = instr.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);

        }catch(Exception e){
            e.printStackTrace();
        }

        return base64;
    }

    public static String decodeFromBase64(String instr){

        String text="";
        try{
            byte[] data = Base64.decode(instr, Base64.DEFAULT);
            text = new String(data, "UTF-8");

        }catch (Exception e){
            e.printStackTrace();
        }

        return text;
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static String getPostDataJSON(HashMap<String, String> params) throws UnsupportedEncodingException {
        JSONObject postData = new JSONObject();

        try{
        for(Map.Entry<String, String> entry : params.entrySet()) {
                postData.put(entry.getKey(), entry.getValue());
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return postData.toString();
    }
}

