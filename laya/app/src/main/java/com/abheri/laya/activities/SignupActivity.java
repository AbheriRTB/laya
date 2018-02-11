package com.abheri.laya.activities;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abheri.laya.BuildConfig;
import com.abheri.laya.R;
import com.abheri.laya.util.CloudDataFetcherAsyncTask;
import com.abheri.laya.util.LayamOperations;
import com.abheri.laya.util.Util;
import com.abheri.laya.views.HandleServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class SignupActivity extends AppCompatActivity implements HandleServiceResponse {

    @BindView(R.id.edit_first_name)
    TextView firstname;
    @BindView(R.id.edit_last_name)
    TextView lastname;
    @BindView(R.id.edit_email)
    TextView email;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.reenter_password)
    TextView reenter_password;
    @BindView(R.id.button_signup)
    Button signup;
    @BindView(R.id.text_cancel)
    TextView cancel;
    @BindView(R.id.signupProgressBar)
    ProgressBar signupProgressBar;
    @BindView(R.id.signupLogText)
    TextView signupLogText;
    @BindView(R.id.subscriptionType)
    RadioGroup subscriptionTypeGroup;

    Context self;
    HashMap<String, String> dataMap=new HashMap<String, String>();
    ArrayList<RadioButton> rba = new ArrayList<>();
    ArrayList<Integer> subid = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        self = this;

        subscriptionTypeGroup.setVisibility(View.GONE);
        signupProgressBar.setVisibility(View.GONE);


        //Get the subscription types
        CloudDataFetcherAsyncTask rt = new CloudDataFetcherAsyncTask(this,
                LayamOperations.GETSUBSCRIPTIONS, self);
        String url =  Util.getServiceUrl(LayamOperations.GETSUBSCRIPTIONS);
        rt.execute(url);

    }

    void addSubscriptionTypeOptions(String res){
        JSONArray ja;
        try {
            ja = new JSONArray(res);
            ArrayList<String>  al = new ArrayList<>();

            if (ja != null) {
                for (int i = 0; i < ja.length(); ++i) {
                    JSONObject jo = ja.getJSONObject(i);
                    String sub = jo.getString("subscription_type") + " @ Rs." + jo.getInt("rate");
                    subid.add(jo.getInt("subscription_id"));

                    RadioButton r = new RadioButton(this);
                    r.setText(sub);
                    subscriptionTypeGroup.addView(r);
                    rba.add(r);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        subscriptionTypeGroup.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.button_signup, R.id.text_cancel})
    public void onButtonsClicked(View v) {

        switch (v.getId()){

            case R.id.button_signup:
                subscriptionTypeGroup.getCheckedRadioButtonId();

                dataMap.put("firstname",firstname.getText().toString());
                dataMap.put("lastname", lastname.getText().toString());
                dataMap.put("email", email.getText().toString());
                dataMap.put("password", Util.encodeToBase64(password.getText().toString()));
                dataMap.put("teelephone","8888899999");
                dataMap.put("subscription","1");
                signupProgressBar.setVisibility(View.VISIBLE);
                CloudDataFetcherAsyncTask rt = new CloudDataFetcherAsyncTask(this,
                        LayamOperations.SIGNUP, self, dataMap);
                String url =  Util.getServiceUrl(LayamOperations.SIGNUP);

                rt.execute(url);
                break;
            case R.id.text_cancel:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object result) {
        signupProgressBar.setVisibility(View.GONE);

        String res = (String)result;
        if(res.contains("firstname")){
            signupLogText.setText((String)result);
            signupLogText.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        }
        else{
            addSubscriptionTypeOptions(res);
        }

    }

    @OnFocusChange({R.id.password, R.id.reenter_password})
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            ((TextView)v).setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void onError(Object result) {
        signupProgressBar.setVisibility(View.GONE);
        signupLogText.setText(R.string.loginerror);
        signupLogText.setTextColor(getResources().getColor(R.color.progress_color));
    }
}
