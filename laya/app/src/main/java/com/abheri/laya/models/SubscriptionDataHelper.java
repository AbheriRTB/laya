package com.abheri.laya.models;

/**
 * Created by prasanna.ramaswamy on 24/03/18.
 */

import com.abheri.laya.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by prasanna.ramaswamy on 25/10/15.
 */
public class SubscriptionDataHelper {

    JSONArray ja;

    public List<Subscription> parseVenueListFromJsonResponse(String jsonstring) {


        List<Subscription> subscriptions = new ArrayList<Subscription>();
        List<Subscription> cachedVenues = new ArrayList<Subscription>();

        Subscription tmpSubscription;

        //String dummy_json =  "[{\"artiste_id\":1,\"artiste_name\":\"Ananthram B K\",\"artiste_phone\":\"123456789\",\"artiste_website\":\"null\",\"art_type\":\"Carnatic\",\"audio_clip\":\"aud    io_bka.wav\",\"artiste_instrument\":\"null\",\"artiste_address1\":\"9th Main\",\"artiste_address2\":\"Subramanyanagar\",\"artiste_city\":\"Bangalore\",\"artiste_state\":\"Karnataka\",\"artiste_country\":\"India\",\"artiste_pincode\":\"560079\",\"artiste_coords\":\"undefined\",\"artiste_image\":\"b_k_ananthram.jpeg\",\"artiste_is_published\":\"null\"}]";

        try {
            ja = new JSONArray(jsonstring);
            if (ja != null) {
                for (int i = 0; i < ja.length(); ++i) {
                    tmpSubscription = new Subscription();
                    JSONObject jo = ja.getJSONObject(i);

                    tmpSubscription.setId(jo.getLong("subscriptoion_id"));
                    tmpSubscription.setSubscription_sku(jo.getString("subscription_sku"));
                    tmpSubscription.setIs_active(jo.getString("is_active"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return subscriptions;
    }
}
