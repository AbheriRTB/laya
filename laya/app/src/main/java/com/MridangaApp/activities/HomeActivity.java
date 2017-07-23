package com.MridangaApp.activities;

import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.MridangaApp.R;
import com.MridangaApp.views.VerticalSeekBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @InjectView(R.id.iv_ctrl)
    ImageView ivCtrl;
    @InjectView(R.id.sb_mridanga)
    VerticalSeekBar sbMridanga;
    @InjectView(R.id.sb_tamburi)
    VerticalSeekBar sbTamburi;

    private int state = 0;
    MediaPlayer mPlayer;
    MediaPlayer mPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSeekbarProgress();
    }

    private void setSeekbarProgress() {

        sbMridanga.setProgress(100);
        sbTamburi.setProgress(100);
        sbMridanga.incrementProgressBy(5);
        sbTamburi.incrementProgressBy(5);
        sbMridanga.setMax(100);
        sbTamburi.setMax(100);
        final int maxVolume = 100;

        sbMridanga.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 5;
                progress = progress * 5;
                float log1=(float)(Math.log(maxVolume-progress)/Math.log(maxVolume));
                mPlayer.setVolume(1-log1, 1-log1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbTamburi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 5;
                progress = progress * 5;
                float log1=(float)(Math.log(maxVolume-progress)/Math.log(maxVolume));
                mPlayer2.setVolume(1-log1, 1-log1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @OnClick(R.id.iv_ctrl)
    public void ivCtrlClicked(){
        if(state == 1) {
            ivCtrl.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.play_button));
            pauseClicked();
        }else{
            ivCtrl.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pause));
            playClicked();
        }
    }

    private void pauseClicked() {
        if(mPlayer!=null && mPlayer.isPlaying()){
            //If music is playing already
            mPlayer.stop();
            state = 0;
        }
        if(mPlayer2!=null && mPlayer2.isPlaying()){
            //If music is playing already
            mPlayer2.stop();
            state = 0;
        }
    }

    private void playClicked() {
        mPlayer = MediaPlayer.create(this, R.raw.pm80b_f_madhyama_2);
        mPlayer.start();
        state = 1;
        mPlayer2 = MediaPlayer.create(this, R.raw.manma);
        mPlayer2.start();
        state = 1;
    }

}
