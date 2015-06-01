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

    private TextView mInfoTextView,resultOnline;
    private Client mClient;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SIN_RES = "single_result";
    public static final String APP_PREFERENCES_SIN_GAM = "single_games";
    public static final String APP_PREFERENCES_MUL_RES = "multi_result";
    public static final String APP_PREFERENCES_MUL_GAM = "multi_games";
    private static final String STATISTIC = "statistic";
    private static final String EQUALS = "=";
    private VKAccessToken token;
    private String res_log  ="\n";
    private SharedPreferences mSettings;
    private Results.MyTask task;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        token = getAccessToken();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mInfoTextView = (TextView) findViewById(R.id.resultView);
       resultOnline = (TextView)findViewById(R.id.resultOnline);
       task = (MyTask) new MyTask().execute("");
       while (true) {
           if (mClient != null && mClient.clientHasRun()) {
               mClient.sendMessage(STATISTIC + EQUALS + token.userId);
               break;
           }
       }
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
