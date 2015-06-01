package com.fupm.skeb.androidclient;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


public class Background extends ActionBarActivity {

    //layout
    RadioGroup radioGroup;

    private TextView textView3;

    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);//standard

        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        textView3 = (TextView) findViewById(R.id.textView3);

        int a = LoadPreferences();
        //LoadPreferences();//+

        textView3.setText("" + a + "");
    }

    OnCheckedChangeListener radioGroupOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
            int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

            SavePreferences(KEY_RADIOBUTTON_INDEX, checkedIndex);
        }
    };

    private void SavePreferences(String key, int value) {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int LoadPreferences() {
    //private void LoadPreferences() {//загрузка
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int savedRadioIndex = mSettings.getInt(KEY_RADIOBUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);

        return savedRadioIndex;
    }



}