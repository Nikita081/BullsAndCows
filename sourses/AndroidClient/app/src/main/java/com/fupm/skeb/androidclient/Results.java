package com.fupm.skeb.androidclient;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;


public class Results extends ActionBarActivity {

    private TextView mInfoTextView;

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
        mInfoTextView = (TextView) findViewById(R.id.resultView);
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
            mInfoTextView.setText(getString(R.string.singleRes) + numAttempts(mCounter) +
                    getString(R.string.singleGames) + mCounter2 +
                    getString(R.string.multiRes) + numAttempts(mCounter3) +
                    getString(R.string.multiGames) + mCounter4);
        }
        else{
            mInfoTextView.setText(R.string.nonPlayed);
        }
    }

    private String numAttempts(int attempt){
        String message = attempt + " ";
        if (attempt >= 5 && attempt <= 20) message += "ходов";
        else switch(attempt % 10) {
            case 0: message = " - "; break;
            case 1: message += "ход"; break;
            case 2:
            case 3:
            case 4: message += "хода"; break;
            default: message += "ходов"; break;
        }
        return message;
    }
/*
    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_COUNTER, mCounter);
        editor.apply();
    }*/
}
