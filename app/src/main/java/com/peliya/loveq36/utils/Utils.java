package com.peliya.loveq36.utils;

import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Peliya on 02.02.2015.
 */
public class Utils {
    public static void updateTextViewFadeIn(TextView label, String text) {
        label.setText(text);

        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(label);
    }
}
