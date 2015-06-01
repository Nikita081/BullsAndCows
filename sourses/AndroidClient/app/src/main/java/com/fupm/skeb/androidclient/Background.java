package com.fupm.skeb.androidclient;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class Background extends ActionBarActivity {

    RadioGroup radioGroup;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        LoadPreferences();
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
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(KEY_RADIOBUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}