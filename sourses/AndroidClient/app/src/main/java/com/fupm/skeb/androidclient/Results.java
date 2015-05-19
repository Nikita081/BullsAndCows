package com.fupm.skeb.androidclient;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;


public class Results extends ActionBarActivity {

    private int mCounter;
    private TextView mInfoTextView;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "single_result";
    private SharedPreferences mSettings;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

       mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
       mInfoTextView = (TextView) findViewById(R.id.resultView);
    }

    public void onClick(View v) {
        // Выводим на экран
        mInfoTextView.setText("Лучший результат " + ++mCounter + " ходов");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            // Получаем число из настроек
            mCounter = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
            // Выводим на экран данные из настроек
            mInfoTextView.setText("Лучший результат " + ++mCounter + " ходов");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_COUNTER, mCounter);
        editor.apply();
    }
}
