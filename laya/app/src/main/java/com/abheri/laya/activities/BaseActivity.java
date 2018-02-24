package com.abheri.laya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abheri.laya.subscriptionUtils.IabException;
import com.abheri.laya.subscriptionUtils.IabHelper;
import com.abheri.laya.subscriptionUtils.IabResult;
import com.abheri.laya.subscriptionUtils.Inventory;
import com.abheri.laya.subscriptionUtils.Purchase;
import com.abheri.laya.subscriptionUtils.SkuDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sahana on 8/7/17.
 */
public class BaseActivity extends AppCompatActivity{


    private static final int REQUEST_CODE = 1001;
    //    private String base64EncodedPublicKey = "PixnMSYGLjg7Ah0xDwYILlVZUy0sIiBoMi4jLDcoXTcNLiQjKgtlIC48NiRcHxwKHEcYEyZrPyMWXFRpV10VES9ENzg1Hj06HTV1MCAHJlpgEDcmOxFDEkA8OiQRKjEQDxhRWVVEMBYmNl1AJghcKUAYVT15KSQgBQABMgwqKSlqF1gZBA4fAw5rMyxKIw9LJFc7AhxZGjoPATgRUiUjKSsOWyRKDi4nIA9lKgAGOhMLDF06CwoKGFR6Wj0hGwReS10NXzQTIREhKlkuMz4XDTwUQjRCJUAVjQVPUIoPicOLQJCLxs8RjZnJxY1OQNLKgQCPj83AyBEFSAJEk5UClYjGxVLNBU3FS4DCztENQMuOk5rFVclKz88AAApPgADGFxEEV5eQAF7QBhdQEEBzc5MygCAwlEFzclKRB7FB0uFgwPKgAvLCk2OyFiKxkgIy8BBQYjFy4/E1ktJikrEVlKJVYIHh16NDwtDCU0Vg8JNzoQBwQWOwk1GzZ4FT8fWicwITcRJi8=";
//    private String  = "PixnMSYGLjg7Ah0xDwYILlVZUy0sIiBoMi4jLDcoXTcNLiQjKgtlIC48NiRcHxwKHEcYEyZrPyMWXFRpV10VES9ENzg1Hj06HTV1MCAHJlpgEDcmOxFDEkA8OiQRKjEQDxhRWVVEMBYmNl1AJghcKUAYVT15KSQgBQABMgwqKSlqF1gZBA4fAw5rMyxKIw9LJFc7AhxZGjoPATgRUiUjKSsOWyRKDi4nIA9lKgAGOhMLDF06CwoKGFR6Wj0hGwReS10NXzQTIREhKlkuMz4XDTwUQjRCJUAVjQVPUIoPicOLQJCLxs8RjZnJxY1OQNLKgQCPj83AyBEFSAJEk5UClYjGxVLNBU3FS4DCztENQMuOk5rFVclKz88AAApPgADGFxEEV5eQAF7QBhdQEEBzc5MygCAwlEFzclKRB7FB0uFgwPKgAvLCk2OyFiKxkgIy8BBQYjFy4/E1ktJikrEVlKJVYIHh16NDwtDCU0Vg8JNzoQBwQWOwk1GzZ4FT8fWicwITcRJi8=";
//    final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApNjLzyfaWfAJ5H9sFpB0sFUJAVFdDmenjind6OuxZvkdxYWTv8reLu1vvYLjD0SoovJiweGlwmklkJjwT2WCc5SoZ/cXePAhhQshAILw8NUOBy3n2kVaT11FUmYG6yxNXwxjKfCJPFG3AHPpGhlxjhuF7CF7WVnzazfUwJPLDLbsw2P7dj1cunRFLiIVoBC15BD93xPUd9UUIm2oYIzCY9gv7DBppR16bMBUeaWWvnZK774CIkxQNi06AfCubjHoAr7V2O2EEO3MeJHQuHHrA9bVEj5MKkUzHZ4bBHv7YWJrrBpaQYekclYXb8q8L9tByqDDyhOUQ8GdTCTw9jKDgwIDAQAB";
    final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApNjLzyfaWfAJ5H9sFpB0sFUJAVFdDmenjind6OuxZvkdxYWTv8reLu1vvYLjD0SoovJiweGlwmklkJjwT2WCc5SoZ/cXePAhhQshAILw8NUOBy3n2kVaT11FUmYG6yxNXwxjKfCJPFG3AHPpGhlxjhuF7CF7WVnzazfUwJPLDLbsw2P7dj1cunRFLiIVoBC15BD93xPUd9UUIm2oYIzCY9gv7DBppR16bMBUeaWWvnZK774CIkxQNi06AfCubjHoAr7V2O2EEO3MeJHQuHHrA9bVEj5MKkUzHZ4bBHv7YWJrrBpaQYekclYXb8q8L9tByqDDyhOUQ8GdTCTw9jKDgwIDAQAB";
    private static final List<String> SKUS = Collections.singletonList("com.abheri.laya.monthlysubscription");

    private IabHelper iabHelper;
    private String subscriptionType = "monthly_discounted";
    private String payload = "";
    public boolean mIsSubscriptionPending = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iabHelper = new IabHelper(this, base64EncodedPublicKey);
        iabHelper.enableDebugLogging(true, "IAB_HELPER_DEMO_TAG");
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    Log.d("YOUR_TAG", "Problem setting up In-app Billing: " + result);
                    dispose();
                }

                try {

                    iabHelper.queryInventoryAsync(true,
                            null,
                            SKUS,
                            new IabHelper.QueryInventoryFinishedListener() {
                                @Override
                                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                    SkuDetails sku = inv.getSkuDetails(subscriptionType);
                                    Purchase purchase = inv.getPurchase(subscriptionType);
                                    if(sku!=null){
                                        Log.d("BaseActivitySubs","Sku::"+sku.getSku());
                                        Log.d("BaseActivitySubs","SkuDesc::"+sku.getDescription());
                                        Log.d("BaseActivitySubs","SkuPrice::"+sku.getPrice());
                                        Log.d("BaseActivitySubs","SkuType::"+sku.getType());
                                    } if(purchase!=null){
                                        Log.d("BaseActivitySubs","purchase::"+purchase.getSku());
                                        Log.d("BaseActivitySubs","purchaseState::"+purchase.getPurchaseState());
                                        Log.d("BaseActivitySubs","purchaseTime::"+purchase.getPurchaseTime());
                                        Log.d("BaseActivitySubs","isAutoRenewing::"+purchase.isAutoRenewing());
                                        if(purchase.isAutoRenewing()){
                                            mIsSubscriptionPending = false;
                                        }
                                    }
                                }
                            });

                } catch (IabHelper.IabAsyncInProgressException e) {
                    Log.e("TEST", "EXCEPTION:" + e.getMessage());
                }

            }
        });

    }

    public void launchSubscription() {
        try {
            iabHelper.launchSubscriptionPurchaseFlow( this,
                    subscriptionType,
                    REQUEST_CODE,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            if(info!=null){
                                Log.d("BaseActivitySubs","purchase::"+info.getSku());
                                Log.d("BaseActivitySubs","purchaseState::"+info.getPurchaseState());
                                Log.d("BaseActivitySubs","purchaseTime::"+info.getPurchaseTime());
                                Log.d("BaseActivitySubs","isAutoRenewing::"+info.isAutoRenewing());
                            }
                            if (result.isFailure()) {
                                Log.e("TEST", "Error purchasing: " + result);
                                return;
                            }
                            if (info.getSku().equals(subscriptionType)) {
                                Log.e("TEST", "Thank you for upgrading to premium!");
                                mIsSubscriptionPending = false;
                            }

                        }
                    },
                    payload
            );
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    /*
** Method for releasing resources (dispose of object)
*/
    public void dispose() {
        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            iabHelper = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
