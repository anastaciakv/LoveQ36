package com.peliya.loveq36;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class TimerActivity extends Activity {
    private CountDownTimer timer;
    private static final long COUNT_DOWN_INTERVAL = TimeUnit.SECONDS.toMillis(30);
    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toMillis(1);
    private TextView text;
    private boolean isTimerStarted;
    private int mLevel = 0;
    private ClipDrawable mImageDrawable;
    private Handler mHandler;
    private Runnable animateImage = new Runnable() {

        @Override
        public void run() {
            doTheAnimation();
        }
    };

    private void doTheAnimation() {
        mLevel += 1000;
        mImageDrawable.setLevel(mLevel);
        if (mLevel <= 10000) {
            mHandler.postDelayed(animateImage, 50);
        } else {
            mHandler.removeCallbacks(animateImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView img = (ImageView) findViewById(R.id.ivTimer);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);
        mHandler = new Handler();

        text = (TextView) findViewById(R.id.tvTime);
        timer = new CountDownTimer(COUNT_DOWN_INTERVAL, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText("Time remain:" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                text.setText("You should be in love by now!");
            }
        };
    }

    private void startCountDown() {
        if (!isTimerStarted) {
            timer.start();
            isTimerStarted = true;
            mHandler.post(animateImage);
        } else {
            timer.cancel();
            isTimerStarted = false;
            mLevel = 0;
            mImageDrawable.setLevel(mLevel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start) {
            startCountDown();
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
