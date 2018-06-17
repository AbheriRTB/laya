package com.abheri.laya.models;

import java.io.Serializable;

/**
 * Created by prasanna.ramaswamy on 24/03/18.
 */

public class Subscription implements Serializable {
    private long subscription_id;
    private String subscription_sku;
    private String is_active;

    public long getId() {
        return subscription_id;
    }

    public void setId(long id) {
        this.subscription_id = id;
    }

    public String getSubscription_sku() {
        return subscription_sku;
    }

    public void setSubscription_sku(String sku) {
        this.subscription_sku = sku;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String active) {
        this.is_active = active;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return subscription_sku;
    }
    //Test

}
