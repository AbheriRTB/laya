package com.abheri.laya.util;

/**
 * Created by prasanna.ramaswamy on 12/12/17.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;


public class SoundPoolManager {

    private SoundPool soundPool;
    private int soundID, streamID;
    public boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter, resourceToPlay;
    Context context;

    /** Called when the activity is first created. */
    public SoundPoolManager(Context c, int res) {


        resourceToPlay=res;
        context=c;

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Hardware buttons setting to adjust the media sound
        //context.getApplicationContext().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // the counter will help us recognize the stream id of the sound played  now
        counter = 0;

        // Load the sounds
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(context, resourceToPlay, 1);

    }

    public void load(int res) {
        soundID = soundPool.load(context, res, 1);
    }

    public void playOnce(int res) {
        //soundID = soundPool.load(context, res, 1);
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
            plays = true;
        }
    }

    public void play(int res) {
        //soundID = soundPool.load(context, res, 1);
        //loaded=true;
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            // the sound will play for ever if we put the loop parameter -1
            streamID = soundPool.play(soundID, volume, volume, 1, -1, 1f);
            plays = true;
        }
    }

    public void playLoop(int res) {
        //soundID = soundPool.load(context, res, 1);
        //loaded=true;
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            // the sound will play for ever if we put the loop parameter -1
            streamID = soundPool.play(soundID, volume, volume, 1, -1, 1f);
            plays = true;
        }
    }

    public void pause() {
        // Is the sound loaded already?
        if (plays) {
            soundPool.pause(streamID);
            soundID = soundPool.load(context, resourceToPlay, counter);
            plays = false;
        }
    }

    public void stop() {
        // Is the sound loaded already?
        if (plays) {
            soundPool.stop(streamID);
            soundID = soundPool.load(context, resourceToPlay, counter);
            plays = false;
        }
    }

    public void setVol(float vol){
        volume = vol;
        soundPool.setVolume(streamID, volume, volume);
    }

    public float getVol(){
        return volume;
    }
}