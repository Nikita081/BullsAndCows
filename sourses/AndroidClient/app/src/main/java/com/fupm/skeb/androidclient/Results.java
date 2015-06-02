package com.fupm.skeb.androidclient;


import android.os.AsyncTask;

import android.support.v4.app.FragmentActivity;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;

import static com.vk.sdk.VKSdk.getAccessToken;



public class Results extends FragmentActivity {

    //single game
    private TextView SingleText1, SingleText2, SingleText3, SingleText4;

    //multiplayer
    private TextView multiText1, multiText2, multiText3, multiText4, multiText5;
    private String [] game_array;
    private String [] win_array;
    private String [] best_array;
    private String [] draw_array;

    private TextView mInfoTextView,resultOnline;
    private Client mClient;
    public static final String APP_PREFERENCES = "mysettings";

    //single game
    public static final String APP_PREFERENCES_SIN_RES = "single_result";//количество ходов
    public static final String APP_PREFERENCES_SIN_WIN = "single_win";//количество побед
    public static final String APP_PREFERENCES_SIN_LOS = "single_loss";//количество недоигранных игр
    private static final String STATISTIC = "statistic";
    private static final String EQUALS = "=";
    private static final String WIN = "win";
    private static final String GAME = "game";
    private static final String ATTEMPT = "attempt";
    private static final String DRAW = "draw";
    private static final String LOSE = "lose";


    private VKAccessToken token;
    private String res_log  ="\n";
    private Button buttonCrowsCounter;


    private SharedPreferences mSettings;
    private Results.MyTask task;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        token = getAccessToken();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        task = (MyTask)new MyTask().execute("");
        while(true){
            if(mClient !=null && mClient.clientHasRun()){
                mClient.sendMessage(STATISTIC+EQUALS+token.userId);
                break;
            }
        }

        buttonCrowsCounter = (Button) findViewById(R.id.buttonCrowsCounter);
        //single game
        SingleText1 = (TextView) findViewById(R.id.SingleText1);
        SingleText2 = (TextView) findViewById(R.id.SingleText2);
        SingleText3 = (TextView) findViewById(R.id.SingleText3);
        SingleText4 = (TextView) findViewById(R.id.SingleText4);

        //multiplayer
        multiText1 = (TextView) findViewById(R.id.MultiText1);
        multiText2 = (TextView) findViewById(R.id.MultiText2);
        multiText3 = (TextView) findViewById(R.id.MultiText3);
        multiText4 = (TextView) findViewById(R.id.MultiText4);
        multiText5 = (TextView) findViewById(R.id.MultiText5);

       buttonCrowsCounter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               switch (v.getId()) {
                   case R.id.buttonCrowsCounter:
                       // TODO Call second activity

                       nullPreferences();
                       break;
                   default:
                       break;
               }
           }
       });

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

        }
        else{

            nullPreferences();
        }
    }

    private void nullPreferences() {
        SharedPreferences.Editor editor = mSettings.edit();
        int mCounter = 0;
        //single game
        editor.putInt(APP_PREFERENCES_SIN_RES, mCounter);
        editor.putInt(APP_PREFERENCES_SIN_WIN, mCounter);
        editor.putInt(APP_PREFERENCES_SIN_LOS, mCounter);
        editor.apply();

        onResume();
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
        if(values[0].startsWith(GAME)){
            game_array  = values[0].split(EQUALS);
            multiText1.setText(game_array[1]);
        }
        else if(values[0].startsWith(WIN)){
            win_array = values[0].split(EQUALS);
            multiText2.setText(win_array[1]);


        }
        else if(values[0].startsWith(ATTEMPT)){
            best_array = values[0].split(EQUALS);
            multiText5.setText(best_array[1]);
        }
        else if(values[0].startsWith(DRAW)){
            draw_array = values[0].split(EQUALS);
            multiText4.setText(draw_array[1]);
        }
        else if(values[0].startsWith(LOSE)){
            String [] lose_aray  = values[0].split(EQUALS);
            multiText3.setText(lose_aray[1]);
        }
    }
}
}
