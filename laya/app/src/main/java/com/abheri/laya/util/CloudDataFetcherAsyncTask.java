package com.abheri.laya.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.abheri.laya.views.HandleServiceResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by prasanna.ramaswamy on 04/02/18.
 */

public class CloudDataFetcherAsyncTask extends AsyncTask<String, String, Object> {

    ProgressDialog progressDialog;
    HandleServiceResponse serviceResponseInterface;
    LayamOperations currentOper;
    Context context;
    HashMap postDataParams;

    public CloudDataFetcherAsyncTask(HandleServiceResponse handleServiceResponse,
                                     LayamOperations curoper, Context ctx)
    {
        serviceResponseInterface = handleServiceResponse;
        currentOper = curoper;
        context = ctx;
    }

    public CloudDataFetcherAsyncTask(HandleServiceResponse handleServiceResponse,
                                     LayamOperations curoper, Context ctx, HashMap pDataParam)
    {
        serviceResponseInterface = handleServiceResponse;
        currentOper = curoper;
        context = ctx;
        postDataParams = pDataParam;
    }

    @Override
    protected Object doInBackground(String... uri) {
        HttpURLConnection urlConnection;
        BufferedInputStream inputStream;
        String responseString = null;

        Object returnObj = null;

        int result=0;
        try {
             /* forming th java.net.URL object */
            URL url = new URL(uri[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
            if(currentOper == LayamOperations.SIGNUP){
                urlConnection.setRequestMethod("POST");
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                String json = Util.getPostDataJSON(postDataParams);
                writer.write(json);

                writer.flush();
                writer.close();
                os.close();
            }
            else {
                urlConnection.setRequestMethod("GET");
            }
            int statusCode = urlConnection.getResponseCode();
            System.out.println(statusCode);
                            /* 200 represents HTTP OK */
            if (statusCode ==  200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                responseString = convertInputStreamToString(inputStream);

                switch(currentOper){

                    case LOGIN:
                        returnObj = responseString;
                        break;
                    case SIGNUP:
                        returnObj = responseString;
                        break;
                    case GETSUBSCRIPTIONS:
                        returnObj = responseString;
                        break;

                    default:
                        break;
                }


                //parseResult(responseString);
                result = 1; // Successful


            }else{
                result = 0; //"Failed to fetch data!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnObj = e;
        }
        return returnObj;
    }

    @Override
    protected void onPostExecute(Object result) {

        if(null != result && !(result instanceof Exception))
            serviceResponseInterface.onSuccess(result);
        else
            serviceResponseInterface.onError(result);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate();
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

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

}