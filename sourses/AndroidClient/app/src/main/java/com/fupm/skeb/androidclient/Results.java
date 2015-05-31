package com.fupm.skeb.androidclient;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;


public class Results extends ActionBarActivity {

    private TextView textView2;
    private TextView textView4;
    private TextView textView6;
    private TextView textView8;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SIN_RES = "single_result";
    public static final String APP_PREFERENCES_SIN_GAM = "single_games";
    public static final String APP_PREFERENCES_MUL_RES = "multi_result";
    public static final String APP_PREFERENCES_MUL_GAM = "multi_games";
    private SharedPreferences mSettings;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView8 = (TextView) findViewById(R.id.textView8);
    }

    public void onClick(View v) {
        SharedPreferences.Editor editor = mSettings.edit();
        int mCounter = 0;
        editor.putInt(APP_PREFERENCES_SIN_RES, mCounter);
        editor.putInt(APP_PREFERENCES_SIN_GAM, mCounter);
        editor.putInt(APP_PREFERENCES_MUL_RES, mCounter);
        editor.putInt(APP_PREFERENCES_MUL_GAM, mCounter);
        editor.apply();
        onResume();
    }

   @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_SIN_RES)) {
            int mCounter = mSettings.getInt(APP_PREFERENCES_SIN_RES, 0);
            int mCounter2 = mSettings.getInt(APP_PREFERENCES_SIN_GAM, 0);
            int mCounter3 = mSettings.getInt(APP_PREFERENCES_MUL_RES, 0);
            int mCounter4 = mSettings.getInt(APP_PREFERENCES_MUL_GAM, 0);

            textView2.setText(numAttempts(mCounter));
            textView4.setText("" + mCounter2 + "");
            textView6.setText(numAttempts(mCounter3));
            textView8.setText("" + mCounter4 + "");
        }
        else{

        }
    }

    private String numAttempts(int attempt){
        String message = attempt + " ";
        if (attempt >= 5 && attempt <= 20) message += getString(R.string.step_ov);
        else switch(attempt % 10) {
            case 0: message = " - "; break;
            case 1: message += getString(R.string.step); break;
            case 2:
            case 3:
            case 4: message += getString(R.string.step_a); break;
            default: message += getString(R.string.step_ov); break;
        }
        return message;
    }
}
