package com.fupm.skeb.androidclient;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;

import static com.vk.sdk.VKSdk.getAccessToken;



public class Results extends ActionBarActivity {

    //single game
    private TextView SingleText1, SingleText2, SingleText3, SingleText4;

    //multiplayer
    private TextView MultiText1, MultiText2, MultiText3, MultiText4, MultiText5;


    private TextView mInfoTextView,resultOnline;
    private Client mClient;
    public static final String APP_PREFERENCES = "mysettings";

    //single game
    public static final String APP_PREFERENCES_SIN_RES = "single_result";//количество ходов
    public static final String APP_PREFERENCES_SIN_WIN = "single_win";//количество побед
    public static final String APP_PREFERENCES_SIN_LOS = "single_loss";//количество недоигранных игр
    private static final String STATISTIC = "statistic";
    private static final String EQUALS = "=";
    private VKAccessToken token;
    private String res_log  ="\n";
    //multiplayer
    public static final String APP_PREFERENCES_MUL_RES = "multi_result";//количество ходов
    public static final String APP_PREFERENCES_MUL_WIN = "multi_win";//количество побед
    public static final String APP_PREFERENCES_MUL_LOS = "multi_loss";//количество проигрышей
    public static final String APP_PREFERENCES_MUL_DRAW = "multi_draw";//количество ничей

    private SharedPreferences mSettings;
    private Results.MyTask task;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        token = getAccessToken();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        //single game
        SingleText1 = (TextView) findViewById(R.id.SingleText1);
        SingleText2 = (TextView) findViewById(R.id.SingleText2);
        SingleText3 = (TextView) findViewById(R.id.SingleText3);
        SingleText4 = (TextView) findViewById(R.id.SingleText4);

        //multiplayer
        MultiText1 = (TextView) findViewById(R.id.MultiText1);
        MultiText2 = (TextView) findViewById(R.id.MultiText2);
        MultiText3 = (TextView) findViewById(R.id.MultiText3);
        MultiText4 = (TextView) findViewById(R.id.MultiText4);
        MultiText5 = (TextView) findViewById(R.id.MultiText5);

    }

    public void onClick(View v) {
        SharedPreferences.Editor editor = mSettings.edit();
        int mCounter = 0;
        //single game
        editor.putInt(APP_PREFERENCES_SIN_RES, mCounter);
        editor.putInt(APP_PREFERENCES_SIN_WIN, mCounter);
        editor.putInt(APP_PREFERENCES_SIN_LOS, mCounter);
        //multiplayer
        editor.putInt(APP_PREFERENCES_MUL_RES, mCounter);
        editor.putInt(APP_PREFERENCES_MUL_WIN, mCounter);
        editor.putInt(APP_PREFERENCES_MUL_LOS, mCounter);
        editor.putInt(APP_PREFERENCES_MUL_DRAW, mCounter);
        editor.apply();
        onResume();
    }

   @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_SIN_RES)) {
            //single game
            int mCounterWIN = mSettings.getInt(APP_PREFERENCES_SIN_WIN, 0);
            int mCounterLOS = mSettings.getInt(APP_PREFERENCES_SIN_LOS, 0);
            int mCounterRES = mSettings.getInt(APP_PREFERENCES_SIN_RES, 0);

            SingleText1.setText("" + (mCounterWIN + mCounterLOS) + "");
            SingleText2.setText("" + mCounterWIN + "");
            SingleText3.setText("" + mCounterLOS + "");
            SingleText4.setText(numAttempts(mCounterRES));

            //multiplayer
            mCounterWIN = mSettings.getInt(APP_PREFERENCES_MUL_WIN, 0);
            mCounterLOS = mSettings.getInt(APP_PREFERENCES_MUL_LOS, 0);
            int mCounterDRAW = mSettings.getInt(APP_PREFERENCES_MUL_DRAW, 0);
            mCounterRES = mSettings.getInt(APP_PREFERENCES_MUL_RES, 0);

            MultiText1.setText("" + (mCounterWIN + mCounterLOS + mCounterDRAW) + "");
            MultiText2.setText("" + mCounterWIN + "");
            MultiText3.setText("" + mCounterLOS + "");
            MultiText4.setText("" + mCounterDRAW + "");
            MultiText5.setText(numAttempts(mCounterRES));
        }
        else{

            ///////////////НАДО ЧТО-ТО СДЕЛАТЬ!

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

public class MyTask extends AsyncTask<String, String, Client> {

    @Override
    protected Client doInBackground(String... message) {


        mClient = new Client(new Client.OnMessageReceived() {
            @Override

            public void messageReceived(String message) {

                publishProgress(message);
            }
        });
        mClient.run();

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        res_log+=values[0]+"\n";
        resultOnline.setText(res_log);

    }

}

}
