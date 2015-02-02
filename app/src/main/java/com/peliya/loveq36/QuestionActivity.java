package com.peliya.loveq36;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class QuestionActivity extends Activity {
    private TextView tvQuestion;
    private static int position = 0;
    private static String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        questions = getResources().getStringArray(R.array.questionstest);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);

        updateQ();
    }

    private void updateQ() {
        tvQuestion.setText(questions[position]);
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(tvQuestion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNext(View view) {
        if (position < questions.length - 1) {
            position++;
            updateQ();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.msg_last_question))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startTimerActivity();
                        }
                    }).setNegativeButton(android.R.string.cancel, null)
                    .create().show();

        }
    }

    private void startTimerActivity() {
        startActivity(new Intent(QuestionActivity.this, TimerActivity.class));
    }

    public void onPrev(View view) {
        if (position > 0) {
            position--;
            updateQ();
        }
    }
}
