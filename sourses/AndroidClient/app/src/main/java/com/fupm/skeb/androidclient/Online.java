package com.fupm.skeb.androidclient;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;

import static com.vk.sdk.VKSdk.getAccessToken;


public class Online extends FragmentActivity implements GameFragment.AttemptsListener,ChatFragment.ChatListener {

    private Client mClient;
    private static final String RIDDLE = "riddle";
    private static final String NUMBER = "number";
    private static final String EQUALS = "=";
    private static final String NEW_GAME = "newgame";
    private static final String OWN_ATTEMPT = "own_attempt";
    private static final String GAME = "game";
    private static final String CHAT = "chat";
    private static final String ENEMY_ATTEMPT = "enemy_attempt";
    private static final String YOUR_RIDDLE = "�� �������� ����� ";
    private static final String STARTCHAT = "startChat";


    private String TAG = "Life circle";
    private String enemy_log = "\n";
    private String own_log = "\n";
    private String chat_log = "\n";
    private GameFragment game_fragment;
    private ChatFragment chat_fragment;
    private VKAccessToken token;
    private StringBuilder build_new_game_message;
    private String new_game_message;
    private FragmentTransaction transaction;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        game_fragment = new GameFragment();
        chat_fragment = new ChatFragment();
        token = getAccessToken();
        Log.i(TAG, "userID: " + token.userId);
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, game_fragment);
        transaction.add(R.id.container,chat_fragment);
        transaction.hide(chat_fragment);
        transaction.show(game_fragment);
        boolean tmp = transaction.isAddToBackStackAllowed();
        Log.i(TAG, "stack allow:" + tmp);
        transaction.addToBackStack(null);
        transaction.commit();
        Log.i(TAG, "try open game fragment");
        new MyTask().execute("");

        build_new_game_message = new StringBuilder();
        build_new_game_message.append(NEW_GAME + EQUALS).append(token.userId);
        new_game_message = build_new_game_message.toString();

        while (true) {
            if (mClient != null && mClient.clientHasRun()) {
                mClient.sendMessage(new_game_message);
                break;
            }
        }
        Log.i(TAG, "send token");
    }

    @Override
    public void doAttempt(String s) {
        if (s.startsWith(RIDDLE)) {
            if (mClient != null) {
                boolean a = mClient.sendMessage(s);
                if (!a) {
                    Log.i(TAG, "can't send riddle");
                } else {
                    String[] array = s.split(EQUALS);
                    isend(Integer.parseInt(array[1]));
                }
            }
        } else if (s.startsWith(GAME)) {
            if (mClient != null) {
                boolean a = mClient.sendMessage(s);
                if (!a) {
                    Log.i(TAG, "can't send attempt");
                }
            }
        } else if (s.startsWith(STARTCHAT)) {
            transaction = manager.beginTransaction();
            transaction.show(chat_fragment);
            transaction.hide(game_fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

    }

    @Override
    public void ChatListener(String s) {
        if (mClient != null) {
            boolean a = mClient.sendMessage(s);
            String [] array = s.split(EQUALS);

            chat_log +="@"  + array[1] + "\n\n";
            ((TextView) chat_fragment.getView().findViewById(R.id.chatLog)).setText(chat_log);

            if (!a) {
                Log.i(TAG, "can't send attempt");
            }
        }
    }

    public boolean check(){
        if(manager.getBackStackEntryCount()>1) return true;
        else return false;
    }
    public void onBackPressed(){
        if(check()){
            manager.popBackStack();
            transaction = manager.beginTransaction();
            transaction.show(game_fragment);
            transaction.hide(chat_fragment);
            transaction.commit();
        }
        else
            onNavigateUp();

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

            if (values[0].startsWith(GAME + EQUALS + OWN_ATTEMPT)) {
                String[] own_attempt_array = values[0].split(EQUALS);
                own_log += own_attempt_array[2].trim() + "\n";
                ((TextView) game_fragment.getView().findViewById(R.id.ownLog)).setText(own_log);
                Log.i(TAG, own_attempt_array[1] + ":::::" + own_log);
            } else if (values[0].startsWith(GAME + EQUALS + ENEMY_ATTEMPT)) {
                String[] enemy_attempt_array = values[0].split(EQUALS);
                enemy_log += enemy_attempt_array[2].trim() + "\n";
                ((TextView) game_fragment.getView().findViewById(R.id.enemyLog)).setText(enemy_log);
                Log.i(TAG, enemy_attempt_array[1] + ":::::" + enemy_log);
            } else if (values[0].startsWith(CHAT)) {
                String[] chat_array = values[0].split(EQUALS);
                chat_log += chat_array[1].trim() + "\n";
                ((TextView) chat_fragment.getView().findViewById(R.id.chatLog)).setText(chat_log);
            } else {
                Toast.makeText(game_fragment.getActivity(), values[0], Toast.LENGTH_SHORT).show();
                Log.i(TAG, values[0]);

            }

        }

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void isend(int rid) {
        (game_fragment.getView().findViewById(R.id.buttonRiddle)).setVisibility(View.INVISIBLE);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setText(YOUR_RIDDLE + rid);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setEnabled(false);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setCursorVisible(false);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setBackgroundColor(Color.TRANSPARENT);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setKeyListener(null);
        (game_fragment.getView().findViewById(R.id.tryNumber)).setEnabled(true);
        (game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.VISIBLE);
    }

}
