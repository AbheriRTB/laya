package com.abheri.laya.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abheri.laya.R;
import com.abheri.laya.adapters.ListAdapter;
import com.abheri.laya.models.AudioObject;
import com.abheri.laya.util.SoundPoolManager;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by sahana on 16/7/17.
 */

public class HomeActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener,
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.header_layout)
    RelativeLayout headerLayout;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.btn_play)
    FloatingActionButton btnPlay;
    @BindView(R.id.btn_pause)
    FloatingActionButton btnPause;
    @BindView(R.id.switch_kaala)
    SwitchCompat kaalaSwitch;
    @BindView(R.id.sb_mridanga)
    SeekBar sbMridanga;
    @BindView(R.id.sb_tamburi)
    SeekBar sbTamburi;
    @BindView(R.id.shruthi_scroll)
    View shruthiScroll;
    @BindView(R.id.bpm_70)
    TextView bpm70;
    @BindView(R.id.bpm_80)
    TextView bpm80;
    @BindView(R.id.bpm_90)
    TextView bpm90;
    @BindView(R.id.bpm_100)
    TextView bpm100;
    @BindView(R.id.bpm_110)
    TextView bpm110;
    @BindView(R.id.bpm_120)
    TextView bpm120;
    @BindView(R.id.shruthi_a)
    TextView shruthiA;
    @BindView(R.id.shruthi_as)
    TextView shruthiAs;
    @BindView(R.id.shruthi_b)
    TextView shruthiB;
    @BindView(R.id.shruthi_c)
    TextView shruthiC;
    @BindView(R.id.shruthi_cs)
    TextView shruthiCs;
    @BindView(R.id.shruthi_d)
    TextView shruthiD;
    @BindView(R.id.shruthi_ds)
    TextView shruthiDs;
    @BindView(R.id.shruthi_e)
    TextView shruthiE;
    @BindView(R.id.shruthi_f)
    TextView shruthiF;
    @BindView(R.id.shruthi_fs)
    TextView shruthiFs;
    @BindView(R.id.shruthi_g)
    TextView shruthiG;
    @BindView(R.id.shruthi_gs)
    TextView shruthiGs;
    @BindView(R.id.shruti_base)
    RadioGroup shrutiBase;
    @BindView(R.id.shruti_sa)
    RadioButton shrutiSa;
    @BindView(R.id.shruti_pa)
    RadioButton shrutiPa;
    @BindView(R.id.shruti_ma)
    RadioButton shrutiMa;
    @BindView(R.id.shruti_ni)
    RadioButton shrutiNi;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.mute_mridanga)
    Button muteMridanga;
    @BindView(R.id.mute_tamburi)
    Button muteTamburi;


    private ArrayList<AudioObject> items = new ArrayList<AudioObject>(); // items for the list

    //private LoopingPlayer soundPlayer_m;
    //private LoopingPlayer soundPlayer_t;
    private SoundPoolManager soundPlayer_m; //For Mridangam
    private SoundPoolManager soundPlayer_t; //For Tamburi
    private ListAdapter mCustomListAdapter;
    private int mPositionClicked[] = {0};
    private float mLogMridanga;
    private float mLogTamburi;
    String selectedShruthi;
    String selectedKaala;
    String selectedTala;
    String selectedShrutiSwara;
    private String mSelectedBpm;
    int mridangamClipToPlay, tamburiClipToPlay;
    String mridangamFileNameToPlay, tamburiFileNameToPlay;
    final int MAX_VOLUME = 100;
    boolean isKorvai = false;
    MediaPlayer kPlayer;
    private boolean isPlaying = false;
    private int loadingDelay = 1000;

    private final int OVERLAY_PERMISSION_REQ_CODE = 1;  // Choose any value

    Context self;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        self = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }

        loadPreferences();

        String ver = "v" + getVersionString();
        appVersion.setText(ver);

        appBarLayout.addOnOffsetChangedListener(this);
        createList(); // Creates the list of all the audios
        mCustomListAdapter = new ListAdapter(this, items, mPositionClicked);
        ViewCompat.setNestedScrollingEnabled(listView, true);
        listView.setAdapter(mCustomListAdapter);
        listView.setOnItemClickListener(this);
        kaalaSwitch.setOnCheckedChangeListener(this); // switch checkedchange for kaala
        initialSetUp();

        //IntentFilter phoneStateFilter = new IntentFilter(Intent.ACTION_CALL);
        //registerReceiver(new EventBroadcastReceiver(), phoneStateFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long inst = getAppFirstInstallTime(self);
        long curr = System.currentTimeMillis();
        long diff = (curr - inst) / 1000;
        //Toast.makeText(this, "AppInstalled: "+diff+" sec back.", Toast.LENGTH_LONG).show();

        setCurrentActivity(this);

    }

    private void initialSetUp() {
        //soundPlayer_m = new LoopingPlayer(this);
        //soundPlayer_t = new LoopingPlayer(this);


        selectedShruthi = this.getResources().getString(R.string.shruthi_a);
        mSelectedBpm = this.getResources().getString(R.string.bpm_80);
        selectedTala = this.getResources().getString(R.string.Aditala);
        selectedShrutiSwara = "Sa";
        setTextColor(shruthiA, getShrutiTextViews());
        setTextColor(bpm80, getBpmTextViews());
        kaalaSwitch.setChecked(false);
        setAudioFileName(self);
        setSeekbarProgress();

        grayMuteButton(muteMridanga);
        grayMuteButton(muteTamburi);


        soundPlayer_m = new SoundPoolManager(self, mridangamClipToPlay);
        soundPlayer_t = new SoundPoolManager(self, tamburiClipToPlay);

        shrutiBase.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                getSelectedShrutiSwara();
                pauseClicked();
            }
        });

        if (isPlaying) {
            showPause();
        } else {
            hidePause();
        }
    }

    private void setInitialVolume() {
        int mProgress = sbMridanga.getProgress();
        int sProgress = sbTamburi.getProgress();

        soundPlayer_m.setVol((float) mProgress / (float) MAX_VOLUME);
        soundPlayer_t.setVol((float) sProgress / (float) MAX_VOLUME);
    }

    private void createList() {
        //send name of the audio to be displayed, m file, f file
        items.add(setAudioObject(this.getResources().getString(R.string.Aditala)));
        items.add(setAudioObject(this.getResources().getString(R.string.Mishrachapu)));
        items.add(setAudioObject(this.getResources().getString(R.string.Rupakatala)));
        items.add(setAudioObject(this.getResources().getString(R.string.Khandachapu)));
        items.add(setAudioObject(this.getResources().getString(R.string.TishraAdi)));
        items.add(setAudioObject(this.getResources().getString(R.string.AditalaMM)));
        items.add(setAudioObject(this.getResources().getString(R.string.MishrachapuMM)));
        items.add(setAudioObject(this.getResources().getString(R.string.RupakatalaMM)));
        items.add(setAudioObject(this.getResources().getString(R.string.KhandachapuMM)));
    }

    public void setAudioFileName(Context c) {

        selectedShrutiSwara = getSelectedShrutiSwara();
        tamburiFileNameToPlay = selectedShruthi.toLowerCase() + "_" + selectedShrutiSwara;
        tamburiClipToPlay = c.getResources().getIdentifier(tamburiFileNameToPlay, "raw", self.getPackageName());
        //if(tamburiClipToPlay == 0)
        //    tamburiClipToPlay = c.getResources().getIdentifier("shruti_a", "raw", self.getPackageName());


        if (kaalaSwitch.isChecked()) {
            selectedKaala = this.getResources().getString(R.string.kaala_v);
        } else {
            selectedKaala = this.getResources().getString(R.string.kaala_m);
        }
        selectedTala = items.get(mPositionClicked[0]).getAudioName();
        Log.d("HomeActivity", "selectedTala:: " + selectedTala);
        Log.d("HomeActivity", "selectedShruthi:: " + selectedShruthi);
        Log.d("HomeActivity", "selectedKaala:: " + selectedKaala);
        Log.d("HomeActivity", "selectedBPM:: " + mSelectedBpm);

        if (selectedTala.toLowerCase().contains("korvai")) {
            if (selectedTala.toLowerCase().contains("aditala"))
                mridangamFileNameToPlay = "aditala_mohra_korvai";
            if (selectedTala.toLowerCase().contains("rupakatala"))
                mridangamFileNameToPlay = "rupakatala_mohra_korvai";
            if (selectedTala.toLowerCase().contains("mishrachapu"))
                mridangamFileNameToPlay = "mishrachapu_mohra_korvai";
            if (selectedTala.toLowerCase().contains("khandachapu"))
                mridangamFileNameToPlay = "khandachapu_mohra_korvai";

            isKorvai = true;

        } else {
            mridangamFileNameToPlay = selectedTala + "_" + selectedShruthi + "_" + selectedKaala + "_" + String.valueOf(mSelectedBpm);
            mridangamFileNameToPlay = mridangamFileNameToPlay.toLowerCase();
        }

        Log.d("LayaDebug", "mridangamFileNameToPlay:: " + mridangamFileNameToPlay);
        mridangamClipToPlay = c.getResources().getIdentifier(mridangamFileNameToPlay, "raw", self.getPackageName());
        //if(mridangamClipToPlay == 0)
        //    mridangamClipToPlay = c.getResources().getIdentifier("aditala_a_m_70", "raw", self.getPackageName());

        Log.d("layaDebug", tamburiFileNameToPlay.toString() + " " + tamburiClipToPlay);
        Log.d("layaDebug", mridangamFileNameToPlay.toString() + " " + mridangamClipToPlay);
    }

    public String getSelectedShrutiSwara() {
        // Is the button now checked?
        int selectedRB = shrutiBase.getCheckedRadioButtonId();
        String swara = "sa";

        // Check which radio button was clicked
        switch (selectedRB) {
            case R.id.shruti_sa:
                swara = "sa";
                break;
            case R.id.shruti_pa:
                swara = "pa";
                break;
            case R.id.shruti_ma:
                swara = "ma";
                break;
            case R.id.shruti_ni:
                swara = "ni";
                break;
        }

        return swara;
    }

    public AudioObject setAudioObject(String name) {
        AudioObject audioObject = new AudioObject();
        audioObject.setAudioName(name);
        return audioObject;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d("HomeActivity", "onOffsetChanged:: " + verticalOffset);
        if (verticalOffset < -200) {
            headerLayout.setVisibility(View.GONE);
        } else {
            headerLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_mridanga_vol)
    public void launchReact(){
        Intent i = new Intent(getBaseContext(), MyReactActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.btn_play)
    public void playClicked() {
        showPause();
        //soundPlayer_m.stop();
        //soundPlayer_t.stop();

        setAudioFileName(this);

        //Korvai files are long hence soundpool wouldn't play. Need to use
        //MediaPlayer.
        if (isKorvai) {
            kPlayer = MediaPlayer.create(self, mridangamClipToPlay);
            kPlayer.start();
            float vol = (float) sbMridanga.getProgress() / (float) MAX_VOLUME;
            kPlayer.setVolume(vol, vol);

            isPlaying = true;
            return;
        }


        //If not Korvai, do the normal stuff
        try {
            soundPlayer_m.load(mridangamClipToPlay);
            while (!soundPlayer_m.loaded) {
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Wait to load and play after 500ms
                    soundPlayer_m.play(mridangamClipToPlay);
                }
            }, loadingDelay);

            setInitialVolume();
        } catch (Exception e) {
            Log.d("Exception", "E:: " + e.toString());
            Toast.makeText(this, R.string.no_tala_file, Toast.LENGTH_SHORT).show();
        }
        try {
            soundPlayer_t.load(tamburiClipToPlay);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Wait to load and play after 500ms
                    soundPlayer_t.play(mridangamClipToPlay);
                }
            }, loadingDelay);
            setInitialVolume();
        } catch (Exception e) {
            Log.d("Exception", "E:: " + e.toString());
            Toast.makeText(this, R.string.no_shruti_file, Toast.LENGTH_SHORT).show();
        }

        isPlaying = true;
    }

    @OnClick(R.id.btn_pause)
    public void pauseClicked() {
        hidePause();
        soundPlayer_m.stop();
        soundPlayer_t.stop();

        if (isKorvai && kPlayer != null) {
            kPlayer.stop();
            kPlayer.release();
            kPlayer = null;
        }
        isKorvai = false;

        isPlaying = false;

    }


    public void showPause() {
        btnPause.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.GONE);
    }

    public void hidePause() {
        btnPause.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mPositionClicked[0] = i;
        mCustomListAdapter.notifyDataSetChanged();
        //playClicked();
        pauseClicked();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //pauseClicked();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //playClicked();
        pauseClicked();
    }

    private void setSeekbarProgress() {
        //progress bar config for mridanga and tamburi volume
        sbMridanga.setProgress(100);
        sbTamburi.setProgress(35);
        sbMridanga.incrementProgressBy(5);
        sbTamburi.incrementProgressBy(5);
        sbMridanga.setMax(100);
        sbTamburi.setMax(100);


        sbMridanga.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (soundPlayer_m != null) {
                    soundPlayer_m.setVol((float) progress / (float) MAX_VOLUME);
                    grayMuteButton(muteMridanga);
                }
                if (kPlayer != null)
                    kPlayer.setVolume((float) progress / (float) MAX_VOLUME,
                            (float) progress / (float) MAX_VOLUME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbTamburi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (soundPlayer_t != null) {
                    soundPlayer_t.setVol((float) progress / (float) MAX_VOLUME);
                    grayMuteButton(muteTamburi);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public TextView[] getShrutiTextViews() {
        TextView[] arr =
                {
                        shruthiA, shruthiAs, shruthiB, shruthiC, shruthiCs,
                        shruthiD, shruthiDs, shruthiE, shruthiF, shruthiFs, shruthiG, shruthiGs
                };

        return arr;
    }

    @OnClick({R.id.mute_tamburi, R.id.mute_mridanga})
    public void muteClicked(View v) {

        switch (v.getId()) {

            case R.id.mute_mridanga:
                if (soundPlayer_m.getVol() < 0.1) {
                    int mProgress = sbMridanga.getProgress();
                    soundPlayer_m.setVol((float) mProgress / (float) MAX_VOLUME);
                    grayMuteButton(muteMridanga);
                } else {
                    soundPlayer_m.setVol(0.0f);
                    v.getBackground().setColorFilter(null);
                }
                break;
            case R.id.mute_tamburi:
                if (soundPlayer_t.getVol() < 0.1) {
                    int sProgress = sbTamburi.getProgress();
                    soundPlayer_t.setVol((float) sProgress / (float) MAX_VOLUME);
                    grayMuteButton(muteTamburi);
                } else {
                    soundPlayer_t.setVol(0.0f);
                    v.getBackground().setColorFilter(null);
                }
                break;
        }
    }

    private void grayMuteButton(Button mbutton){
        mbutton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grayedOut),
                PorterDuff.Mode.MULTIPLY);
    }

    @OnClick({R.id.shruthi_a, R.id.shruthi_b, R.id.shruthi_c, R.id.shruthi_d, R.id.shruthi_e, R.id.shruthi_f, R.id.shruthi_g,
            R.id.shruthi_as, R.id.shruthi_cs, R.id.shruthi_ds, R.id.shruthi_fs, R.id.shruthi_gs})
    public void shruthiClicked(View v) {

        TextView[] arr = getShrutiTextViews();
        switch (v.getId()) {
            case R.id.shruthi_a:
            default:
                setTextColor(shruthiA, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_a);
                break;
            case R.id.shruthi_as:
                setTextColor(shruthiAs, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_as);
                break;
            case R.id.shruthi_b:
                setTextColor(shruthiB, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_b);
                break;
            case R.id.shruthi_c:
                setTextColor(shruthiC, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_c);
                break;
            case R.id.shruthi_cs:
                setTextColor(shruthiCs, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_cs);
                break;
            case R.id.shruthi_d:
                setTextColor(shruthiD, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_d);
                break;
            case R.id.shruthi_ds:
                setTextColor(shruthiDs, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_ds);
                break;
            case R.id.shruthi_e:
                setTextColor(shruthiE, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_e);
                break;
            case R.id.shruthi_f:
                setTextColor(shruthiF, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_f);
                break;
            case R.id.shruthi_fs:
                setTextColor(shruthiFs, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_fs);
                break;
            case R.id.shruthi_g:
                setTextColor(shruthiG, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_g);
                break;
            case R.id.shruthi_gs:
                setTextColor(shruthiGs, arr);
                selectedShruthi = this.getResources().getString(R.string.shruthi_gs);
                break;
        }
        //playClicked();
        pauseClicked();
    }


    //TODO: Change this for production app
    @OnClick({R.id.bpm_70, R.id.bpm_80, R.id.bpm_90, R.id.bpm_100, R.id.bpm_110, R.id.bpm_120})
    //@OnClick({R.id.bpm_80, R.id.bpm_120})
    public void bpmClicked(View v) {

        TextView[] arr = getBpmTextViews();

        switch (v.getId()) {
            case R.id.bpm_70:
            default:
                if(!mIsSubscriptionPending) {
                    mSelectedBpm = this.getResources().getString(R.string.bpm_70);
                    setTextColor(bpm70, arr);
                }else {
                    launchProductOptions();
                }
                break;
            case R.id.bpm_80:
                mSelectedBpm = this.getResources().getString(R.string.bpm_80);
                setTextColor(bpm80, arr);
                break;
            case R.id.bpm_90:
                if(!mIsSubscriptionPending) {
                    mSelectedBpm = this.getResources().getString(R.string.bpm_90);
                    setTextColor(bpm90, arr);
                }else {
                    launchProductOptions();
                }
                break;
            case R.id.bpm_100:
                if(!mIsSubscriptionPending) {
                    mSelectedBpm = this.getResources().getString(R.string.bpm_100);
                    setTextColor(bpm100, arr);
                }else{
                    launchProductOptions();
                }
                break;
            case R.id.bpm_110:
                if(!mIsSubscriptionPending) {
                    mSelectedBpm = this.getResources().getString(R.string.bpm_110);
                    setTextColor(bpm110, arr);
                }else {
                    launchProductOptions();
                }
                break;
            case R.id.bpm_120:
                mSelectedBpm = this.getResources().getString(R.string.bpm_120);
                setTextColor(bpm120, arr);
                break;
        }
        //playClicked();
        pauseClicked();
    }

    private TextView[] getBpmTextViews() {
        return new TextView[]{
                bpm70, bpm80, bpm90, bpm100, bpm110, bpm120
        };
    }

    private void setTextColor(TextView selectedTv, TextView[] unselectedTvArr) {
        for (TextView unselectedTv : unselectedTvArr) {
            unselectedTv.setTextColor(ContextCompat.getColor(this, R.color.grayedOut));

            //If 80 or 120 BPM, render in white color as they are the only free BPMs available
            //Other BPMs are released after subscribing
            if (unselectedTv.getText().equals("80") || unselectedTv.getText().equals("120")) {
                unselectedTv.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            //If user has subscribed to a plan, release other BPMs too. Visually make them white
            if(!mIsSubscriptionPending){
                unselectedTv.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }

        selectedTv.setTextColor(ContextCompat.getColor(this, R.color.progress_color));
    }


    /**
     * The time at which the app was first installed. Units are as per currentTimeMillis().
     *
     * @param context
     * @return
     */
    public static long getAppFirstInstallTime(Context context) {
        PackageInfo packageInfo;
        try {
            if (Build.VERSION.SDK_INT > 8/*Build.VERSION_CODES.FROYO*/) {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return packageInfo.firstInstallTime;
            } else {
                //firstinstalltime unsupported return last update time not first install time
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
                String sAppFile = appInfo.sourceDir;
                return new File(sAppFile).lastModified();
            }

        } catch (PackageManager.NameNotFoundException e) {
            //should never happen
            return 0;
        }
    }

    public String getVersionString() {
        int v = 0;
        String vn = "";
        try {
            String pkgname = self.getPackageName();
            PackageManager pm = self.getPackageManager();
            v = pm.getPackageInfo(pkgname, 0).versionCode;
            vn = pm.getPackageInfo(pkgname, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vn;
    }


    private void savePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("state", isPlaying);
        editor.commit();   // I missed to save the data to preference here,.
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean state = sharedPreferences.getBoolean("state", false);
        isPlaying = state;
    }

    private void clearPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("state", false);
        editor.commit();   // I missed to save the data to preference here,.
    }


    public void onDestroy() {
        super.onDestroy();
        clearPreferences();
        clearReferences();
        pauseClicked();
    }

    @Override
    public void onBackPressed() {
        //savePreferences();

        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        alert.setTitle("Do you want to stop Layam audio?");
        // alert.setMessage("Message");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Your action here
                Activity ca = getCurrentActivity();
                if (ca != null)
                    ca.finish();
            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();
    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    private void clearReferences() {
        Activity currActivity = getCurrentActivity();
        if (this.equals(currActivity))
            setCurrentActivity(null);
    }


}
