package com.peliya.loveq36;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ClipDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.peliya.loveq36.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Activity that contains 4 minutes timer, animated image and romantic song play
 */
public class TimerActivity extends Activity {
    private static final long COUNT_DOWN_INTERVAL = TimeUnit.MINUTES.toMillis(4);
    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toMillis(1);
    private static final int FULL_LEVEL = 10000;
    private static final int ZERO_LEVEL = 0;

    private CountDownTimer timer;
    private TextView label;
    private boolean isTimerStarted;
    private boolean isPlaying;
    private int level = ZERO_LEVEL;
    private ClipDrawable imageDrawable;
    private Handler handler;
    private MediaPlayer mp;
    private Runnable animateImage = new Runnable() {

        @Override
        public void run() {
            if (level <= FULL_LEVEL) {
                imageDrawable.setLevel(level);
                handler.post(animateImage);
            } else {
                imageDrawable.setLevel(FULL_LEVEL);
                handler.removeCallbacks(animateImage);
            }
        }
    };

    private void doTheAnimation() {
        level += (TICK_INTERVAL * FULL_LEVEL / COUNT_DOWN_INTERVAL);
        handler.post(animateImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView img = (ImageView) findViewById(R.id.ivTimer);
        imageDrawable = (ClipDrawable) img.getDrawable();
        imageDrawable.setLevel(ZERO_LEVEL);
        handler = new Handler();
        initPlayer();
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        label = (TextView) findViewById(R.id.tvTime);
        timer = new CountDownTimer(COUNT_DOWN_INTERVAL, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                label.setText(formatTime(minutes) + ":" + formatTime(seconds));
                doTheAnimation();
            }

            @Override
            public void onFinish() {
                doTheAnimation();
                Utils.updateTextViewFadeIn(label, getString(R.string.msg_in_love));
                YoYo.with(Techniques.FadeIn).playOn(img);
                isTimerStarted = false;
                invalidateOptionsMenu();

                v.vibrate(new long[]{200, 1000}, -1);
            }
        };

        startCountDown();
    }

    private void initPlayer() {
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("music.mp3");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mp.setLooping(true);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatTime(long time) {
        String sTime = String.valueOf(time);
        return time < 10 ? "0".concat(sTime) : sTime;
    }

    private void startCountDown() {
        if (!isTimerStarted) {
            level = ZERO_LEVEL;
            imageDrawable.setLevel(level);
            timer.start();
            isTimerStarted = true;
        } else {
            timer.cancel();
            isTimerStarted = false;
            level = ZERO_LEVEL;
            imageDrawable.setLevel(level);
        }
        invalidateOptionsMenu();
    }

    private void playAudio() {
        if (!isPlaying) {
            mp.start();
            isPlaying = true;
        } else {
            if (mp.isPlaying()) {
                mp.pause();
                isPlaying = false;
            }
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        handler.removeCallbacks(animateImage);
        mp.reset();
        mp.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        menu.findItem(R.id.action_start).setVisible(!isTimerStarted);
        menu.findItem(R.id.action_stop).setVisible(isTimerStarted);

        menu.findItem(R.id.action_play).setVisible(!isPlaying);
        menu.findItem(R.id.action_pause).setVisible(isPlaying);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start) {
            startCountDown();
            return true;
        } else if (id == R.id.action_stop) {
            label.setText("");
            startCountDown();
            return true;
        } else if (id == R.id.action_play || id == R.id.action_pause) {
            playAudio();
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}