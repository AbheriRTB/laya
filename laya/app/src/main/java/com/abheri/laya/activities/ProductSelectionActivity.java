package com.abheri.laya.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.abheri.laya.R;
import com.abheri.laya.subscriptionUtils.SkuDetails;

import org.solovyev.android.checkout.Sku;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductSelectionActivity extends AppCompatActivity {

    @BindView(R.id.productSelectionView)
    LinearLayout prodSelectionLayout;
    @BindView(R.id.productGroup)
    RadioGroup productGroup;
    @BindView(R.id.subscribe)
    Button subscribe;

    SkuDetails skuList[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);
        ButterKnife.bind(this);

        skuList = (SkuDetails[]) getIntent().getExtras().get("SKULIST");
        createRadioButtons(skuList);

    }

    protected void createRadioButtons(SkuDetails prodList[]) {

        productGroup.removeAllViews();
        for (SkuDetails sku : prodList) {
            RadioButton rb;

            rb = new RadioButton(this);
            rb.setText(sku.getTitle() + "@" + sku.getPrice());
            productGroup.addView(rb);
        }
    }

    @OnClick({R.id.subscribe})
    public void subscribeClicked(View v) {

        int checkedIndex=0;
        int rbid = productGroup.getCheckedRadioButtonId();
        for(int i=0; i<productGroup.getChildCount(); ++i){
            RadioButton rb = (RadioButton)productGroup.getChildAt(i);
            if(rbid == rb.getId()){
                checkedIndex = i;
                break;
            }
        }
        Intent baseActivity = new Intent(this, HomeActivity.class);
        baseActivity.putExtra("SELECTED_SKU", checkedIndex);
        baseActivity.putExtra("DO_PROD_SELECTION", true);
        startActivity(baseActivity);

    }

}
