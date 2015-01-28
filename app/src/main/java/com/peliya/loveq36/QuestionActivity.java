package com.peliya.loveq36;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class QuestionActivity extends Activity {
    private TextView tvQuestion;
    private static int position = 0;
    private static String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questions = getResources().getStringArray(R.array.questions);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        updateQ();
    }

    private void updateQ() {
        tvQuestion.setText(questions[position]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNext(View view) {
        if (position < questions.length - 1) {
            position++;
            updateQ();
        } else {

        }
    }


    public void onPrev(View view) {
        if (position > 0) {
            position--;
            updateQ();
        }
    }
}
