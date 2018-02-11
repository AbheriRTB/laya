package com.abheri.laya.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abheri.laya.R;
import com.abheri.laya.util.CloudDataFetcherAsyncTask;
import com.abheri.laya.util.LayamOperations;
import com.abheri.laya.util.Util;
import com.abheri.laya.views.HandleServiceResponse;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class LoginActivity extends AppCompatActivity implements HandleServiceResponse{

    Context self;

    @BindView(R.id.login_email)
    TextView email;
    @BindView(R.id.login_password)
    TextView password;
    @BindView(R.id.login_signin)
    TextView signin;
    @BindView(R.id.login_signup)
    TextView signup;
    @BindView(R.id.login_cancel)
    TextView cancel;
    @BindView(R.id.logText)
    TextView logText;
    @BindView(R.id.loginProgressBar)
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        self = this;

        loginProgressBar.setVisibility(View.GONE);

    }

    @OnClick({R.id.login_signup, R.id.login_signin, R.id.login_cancel})
    public void onButtonsClicked(View v) {

        switch (v.getId()){

            case R.id.login_signin:
                loginProgressBar.setVisibility(View.VISIBLE);
                CloudDataFetcherAsyncTask rt = new CloudDataFetcherAsyncTask(this,
                                                                            LayamOperations.LOGIN, self);
                String url =  Util.getServiceUrl(LayamOperations.LOGIN);
                url += "?" + "email=" + email.getText() + "&password=" +
                            Util.encodeToBase64(password.getText().toString());
                rt.execute(url);
                break;
            case R.id.login_signup:
                Intent myIntent = new Intent(self, SignupActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                self.startActivity(myIntent);
                break;
            case R.id.login_cancel:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @OnFocusChange({R.id.login_email, R.id.login_password})
    public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && v.getId() == R.id.login_password) {
                password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
    }

    @Override
    public void onSuccess(Object result) {
        loginProgressBar.setVisibility(View.GONE);
        logText.setText((String)result);
        logText.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    @Override
    public void onError(Object result) {
        loginProgressBar.setVisibility(View.GONE);
        logText.setText(R.string.loginerror);
        logText.setTextColor(getResources().getColor(R.color.progress_color));
    }
}
