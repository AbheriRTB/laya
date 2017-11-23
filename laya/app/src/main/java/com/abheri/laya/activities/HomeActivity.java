package com.abheri.laya.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abheri.laya.R;
import com.abheri.laya.adapters.ListAdapter;
import com.abheri.laya.models.AudioObject;
import com.abheri.laya.util.LoopingPlayer;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sahana on 16/7/17.
 */

public class HomeActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.appbar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.header_layout)
    RelativeLayout headerLayout;
    @InjectView(R.id.list_view)
    ListView listView;
    @InjectView(R.id.btn_play)
    FloatingActionButton btnPlay;
    @InjectView(R.id.btn_pause)
    FloatingActionButton btnPause;
    @InjectView(R.id.switch_kaala)
    SwitchCompat kaalaSwitch;
    @InjectView(R.id.sb_mridanga)
    SeekBar sbMridanga;
    @InjectView(R.id.sb_tamburi)
    SeekBar sbTamburi;
    @InjectView(R.id.shruthi_scroll)
    View shruthiScroll;
    @InjectView(R.id.bpm_70)
    TextView bpm70;
    @InjectView(R.id.bpm_80)
    TextView bpm80;
    @InjectView(R.id.bpm_90)
    TextView bpm90;
    @InjectView(R.id.bpm_100)
    TextView bpm100;
    @InjectView(R.id.bpm_110)
    TextView bpm110;
    @InjectView(R.id.bpm_120)
    TextView bpm120;
    @InjectView(R.id.shruthi_a)
    TextView shruthiA;
    @InjectView(R.id.shruthi_as)
    TextView shruthiAs;
    @InjectView(R.id.shruthi_b)
    TextView shruthiB;
    @InjectView(R.id.shruthi_c)
    TextView shruthiC;
    @InjectView(R.id.shruthi_cs)
    TextView shruthiCs;
    @InjectView(R.id.shruthi_d)
    TextView shruthiD;
    @InjectView(R.id.shruthi_ds)
    TextView shruthiDs;
    @InjectView(R.id.shruthi_e)
    TextView shruthiE;
    @InjectView(R.id.shruthi_f)
    TextView shruthiF;
    @InjectView(R.id.shruthi_fs)
    TextView shruthiFs;
    @InjectView(R.id.shruthi_g)
    TextView shruthiG;
    @InjectView(R.id.shruthi_gs)
    TextView shruthiGs;

    private ArrayList<AudioObject> items = new ArrayList<AudioObject>(); // items for the list

    private LoopingPlayer mPlayer;
    private LoopingPlayer mPlayer2;
    private ListAdapter mCustomListAdapter;
    private int mPositionClicked[] = {0};
    private float mLogMridanga;
    private float mLogTamburi;
    String selectedShruthi;
    String selectedKaala;
    String selectedTala;
    private String mSelectedBpm;
    int mridangamClipToPlay, tamburiClipToPlay;
    String mridangamFileNameToPlay, tamburiFileNameToPlay;
    final int MAX_VOLUME = 100;

    Context self;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        self = this;

        appBarLayout.addOnOffsetChangedListener(this);
        createList(); // Creates the list of all the audios
        mCustomListAdapter = new ListAdapter(this, items, mPositionClicked);
        listView.setAdapter(mCustomListAdapter);
        listView.setOnItemClickListener(this);
        kaalaSwitch.setOnCheckedChangeListener(this); // switch checkedchange for kaala
        initialSetUp();

    }

    private void initialSetUp() {
        mPlayer = new LoopingPlayer(this);
        mPlayer2 = new LoopingPlayer(this);

        selectedShruthi = this.getResources().getString(R.string.shruthi_c);
        mSelectedBpm = this.getResources().getString(R.string.bpm_70);
        selectedTala = this.getResources().getString(R.string.Aditala);
        setTextColor(shruthiC, getShrutiTextViews());
        setTextColor(bpm70, getBpmTextViews());
        kaalaSwitch.setChecked(false);
        setAudioFileName(self);
        setSeekbarProgress();
    }

    private void setInitialVolume() {
        int mProgress = sbMridanga.getProgress();
        int sProgress = sbTamburi.getProgress();

        mPlayer.setVol((float) mProgress / (float) MAX_VOLUME);
        mPlayer2.setVol((float) sProgress / (float) MAX_VOLUME);
    }

    private void createList() {
        //send name of the audio to be displayed, m file, f file
        items.add(setAudioObject(this.getResources().getString(R.string.Aditala)));
        items.add(setAudioObject(this.getResources().getString(R.string.Mishrachapu)));
        items.add(setAudioObject(this.getResources().getString(R.string.Rupakatala)));
        items.add(setAudioObject(this.getResources().getString(R.string.Khandachapu)));
        items.add(setAudioObject(this.getResources().getString(R.string.TishraAdi)));
    }

    public void setAudioFileName(Context c) {

        tamburiFileNameToPlay = "shruti_" + selectedShruthi.toLowerCase();
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
        mridangamFileNameToPlay = selectedTala + "_" + selectedShruthi + "_" + selectedKaala + "_" + String.valueOf(mSelectedBpm);
        mridangamFileNameToPlay = mridangamFileNameToPlay.toLowerCase();
        Log.d("LayaDebug", "mridangamFileNameToPlay:: " + mridangamFileNameToPlay);
        mridangamClipToPlay = c.getResources().getIdentifier(mridangamFileNameToPlay, "raw", self.getPackageName());
        //if(mridangamClipToPlay == 0)
        //    mridangamClipToPlay = c.getResources().getIdentifier("aditala_a_m_70", "raw", self.getPackageName());

        Log.d("layaDebug", tamburiFileNameToPlay.toString() + " " + tamburiClipToPlay);
        Log.d("layaDebug", mridangamFileNameToPlay.toString() + " " + mridangamClipToPlay);
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

    @OnClick(R.id.btn_play)
    public void playClicked() {
        showPause();
        mPlayer.stop();
        mPlayer2.stop();

        setAudioFileName(this);
        //mPlayer = MediaPlayer.create(this, getAudioObjectTobePlayed());
        try {
            mPlayer.play(mridangamClipToPlay);
            setInitialVolume();
        } catch (Exception e) {
            Log.d("Exception", "E:: " + e.toString());
            Toast.makeText(this, R.string.no_tala_file, Toast.LENGTH_SHORT).show();
        }
        try {
            mPlayer2.play(tamburiClipToPlay);
            setInitialVolume();
        } catch (Exception e) {
            Log.d("Exception", "E:: " + e.toString());
            Toast.makeText(this, R.string.no_shruti_file, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_pause)
    public void pauseClicked() {
        hidePause();
        mPlayer.stop();
        mPlayer2.stop();
    }

    /*
    private void pausePlayer2() {
        if(mPlayer2!=null && mPlayer2.isPlaying()){
            mPlayer2.stop();
        }
    }

    private void pausePlayer1() {
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.stop();
        }
    }
    */

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
               /* progress = progress / 5;
                progress = progress * 5;
                mLogMridanga=(float)(Math.log(MAX_VOLUME-progress)/Math.log(MAX_VOLUME));
                if (mPlayer!=null)
                    mPlayer.setVol(1-mLogMridanga); */
                if (mPlayer != null)
                    mPlayer.setVol((float) progress / (float) MAX_VOLUME);
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
                /*progress = progress / 5;
                progress = progress * 5;
                mLogTamburi=(float)((Math.log(MAX_VOLUME-progress)/Math.log(MAX_VOLUME))*1.1);
                if(mPlayer2!=null)
                    mPlayer2.setVol(1-mLogTamburi); */
                if (mPlayer2 != null)
                    mPlayer2.setVol((float) progress / (float) MAX_VOLUME);
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


    @OnClick({R.id.bpm_70, R.id.bpm_80, R.id.bpm_90, R.id.bpm_100, R.id.bpm_110, R.id.bpm_120})
    public void bpmClicked(View v) {

        TextView[] arr = getBpmTextViews();
        switch (v.getId()) {
            case R.id.bpm_70:
            default:
                mSelectedBpm = this.getResources().getString(R.string.bpm_70);
                setTextColor(bpm70, arr);
                break;
            case R.id.bpm_80:
                mSelectedBpm = this.getResources().getString(R.string.bpm_80);
                setTextColor(bpm80, arr);
                break;
            case R.id.bpm_90:
                mSelectedBpm = this.getResources().getString(R.string.bpm_90);
                setTextColor(bpm90, arr);
                break;
            case R.id.bpm_100:
                mSelectedBpm = this.getResources().getString(R.string.bpm_100);
                setTextColor(bpm100, arr);
                break;
            case R.id.bpm_110:
                mSelectedBpm = this.getResources().getString(R.string.bpm_110);
                setTextColor(bpm110, arr);
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
            unselectedTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        selectedTv.setTextColor(ContextCompat.getColor(this, R.color.progress_color));
    }

}
