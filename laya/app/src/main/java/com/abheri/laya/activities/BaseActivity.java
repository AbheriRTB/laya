package com.abheri.laya.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abheri.laya.subscriptionUtils.IabHelper;
import com.abheri.laya.subscriptionUtils.IabResult;
import com.abheri.laya.subscriptionUtils.Inventory;
import com.abheri.laya.subscriptionUtils.Purchase;
import com.abheri.laya.subscriptionUtils.SkuDetails;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sahana on 8/7/17.
 */
public class BaseActivity extends AppCompatActivity{


    private static final int REQUEST_CODE = 1001;
    final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApNjLzyfaWfAJ5H9sFpB0sFUJAVFdDmenjind6OuxZvkdxYWTv8reLu1vvYLjD0SoovJiweGlwmklkJjwT2WCc5SoZ/cXePAhhQshAILw8NUOBy3n2kVaT11FUmYG6yxNXwxjKfCJPFG3AHPpGhlxjhuF7CF7WVnzazfUwJPLDLbsw2P7dj1cunRFLiIVoBC15BD93xPUd9UUIm2oYIzCY9gv7DBppR16bMBUeaWWvnZK774CIkxQNi06AfCubjHoAr7V2O2EEO3MeJHQuHHrA9bVEj5MKkUzHZ4bBHv7YWJrrBpaQYekclYXb8q8L9tByqDDyhOUQ8GdTCTw9jKDgwIDAQAB";

    private static final List<String> SKUS =
                                    Arrays.asList(  "com.abheri.laya.weekly_75rs_25mar18",
                                                        "com.abheri.laya.monthly_150rs_25mar18",
                                                        "com.abheri.laya.halfyearly_499rs_25mar18",
                                                        "com.abheri.laya.yearly_799rs_25mar18");

    private IabHelper iabHelper;
    private String subscriptionType = "monthly_discounted";
    private SkuDetails skuList[] = new SkuDetails[SKUS.size()];
    private String payload = "";
    public boolean mIsSubscriptionPending = true;
    Context self;
    int prodIndex = 0;
    boolean doProductSelection=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        iabHelper = new IabHelper(this, base64EncodedPublicKey);
        self = this;

        if(getIntent().getExtras() != null &&
                getIntent().hasExtra("SELECTED_SKU") &&
                getIntent().hasExtra("DO_PROD_SELECTION")) {
            prodIndex = (int) getIntent().getExtras().get("SELECTED_SKU");
            doProductSelection = (boolean) getIntent().getExtras().get("DO_PROD_SELECTION");
            getIntent().removeExtra("SELECTED_SKU");
            getIntent().removeExtra("DO_PROD_SELECTION");
        }

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
                                Purchase purchase;
                                SkuDetails sku;
                                @Override
                                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                    for(int i=0; i< SKUS.size(); ++i){
                                        skuList[i] = inv.getSkuDetails(SKUS.get(i));
                                        purchase = inv.getPurchase(SKUS.get(i));
                                        sku = skuList[i];
                                        if(prodIndex == i)
                                            subscriptionType = sku.getSku();
                                        if(purchase != null){
                                            break;
                                        }
                                    }
                                     //sku = inv.getSkuDetails(subscriptionType);
                                     //purchase = inv.getPurchase(subscriptionType);
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

                                    if(mIsSubscriptionPending && doProductSelection){
                                        launchSubscription();
                                }

                                }
                            });

                } catch (IabHelper.IabAsyncInProgressException e) {
                    Log.e("TEST", "EXCEPTION:" + e.getMessage());
                }

            }
        });



    }

    public void launchProductOptions(){
        if(mIsSubscriptionPending){
            //Invoke product selection activity
            Intent psActivity = new Intent(self, ProductSelectionActivity.class);
            psActivity.putExtra("SKULIST", skuList);
            startActivity(psActivity);
        }
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
