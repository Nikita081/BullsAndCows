package com.fupm.skeb.androidclient;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.view.View;


public class Background extends FragmentActivity {

    //layout
    RadioGroup radioGroup;
    private Button backButton;

    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        LoadPreferences();
    }

    OnCheckedChangeListener radioGroupOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
            int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

            SavePreferences(KEY_RADIOBUTTON_INDEX, checkedIndex);
            SavePreferences(BACKGROUND_INDEX, choose(checkedIndex));
        }
    };

    private void SavePreferences(String key, int value) {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void LoadPreferences() {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int savedRadioIndex = mSettings.getInt(KEY_RADIOBUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }

    private int choose(int key) {
        switch (key) {
            case 0:
                return R.drawable.p10;
            case 1:
                return R.drawable.p1;
            case 2:
                return R.drawable.p13;
            case 3:
                return R.drawable.p11;
            case 4:
                return R.drawable.p4;
            case 5:
                return R.drawable.p5;
            case 6:
                return R.drawable.p12;
            case 7:
                return R.drawable.p7;
            case 8:
                return R.drawable.p8;
            case 9:
                return R.drawable.p9;
            case 10:
                return R.drawable.p15;
            case 11:
                return R.drawable.p16;
            case 12:
                return R.drawable.p17;
            case 13:
                return R.drawable.cow;
            default:
                return R.drawable.p10;
        }
    }
}