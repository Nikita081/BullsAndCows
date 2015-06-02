package com.fupm.skeb.androidclient;




import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static com.vk.sdk.VKSdk.getAccessToken;


public class Online extends FragmentActivity implements GameFragment.AttemptsListener,ChatFragment.ChatListener {

    private Client mClient;
    private static final String RIDDLE = "riddle";

    private static final String EQUALS = "=";
    private static final String NEW_GAME = "newgame";
    private static final String OWN_ATTEMPT = "own_attempt";
    private static final String GAME = "game";
    private static final String CHAT = "chat";
    private static final String RENEW = "renew";
    private static final String ENEMY_TOKEN = "enemytoken";
    private static final String ENEMY_ATTEMPT = "enemy_attempt";
    private static final String STARTCHAT = "startChat";
    private static final String GAME_PREFERENCES = "game";
    private static final String ENEMY_LOG = "enemylog";
    private static final String OWN_LOG = "ownlog";
    private static final String CHAT_LOG = "chatlog";
    private static final String OWN_RIDDLE = "ownriddle";
    private static final String ENEMY_ID = "enemyid";

    public static final String APP_PREFERENCES= "tokensettings";
    public static final String APP_PREFERENCES_TOKEN = "usertoken";
    private SharedPreferences token_prefarences;
    private String TAG = "Life circle";
    private String enemy_log = "\n";
    private String own_log = "\n";
    private String chat_log = "\n";
    private String enemy_token;
    private GameFragment game_fragment;
    private ChatFragment chat_fragment;
    private String token;
    private  String own_riddle;
    private StringBuilder build_new_game_message;
    private String new_game_message;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private int id;
    private volatile MyTask task;
    private Intent intent;
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    private SharedPreferences game_pref;
    private String flag;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =

            new LinkedBlockingQueue<Runnable>(10);

    private static final Executor executor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        intent = getIntent();
        id = intent.getIntExtra("id", -1);
        flag = intent.getStringExtra("flag");
        Log.i(TAG, "online item id:" + id);
        Log.i(TAG, "online flag:" + flag);

        game_pref = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        token_prefarences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = token_prefarences.getString(APP_PREFERENCES_TOKEN, "!!!");

        Log.i(TAG, "get token!!!!!" + token);
        Log.i(TAG, "create MainActivity: " + this.hashCode());

        game_fragment = new GameFragment();
        Log.i(TAG, "create game fragment"+game_fragment.hashCode());
        chat_fragment = new ChatFragment();
        Log.i(TAG, "create chatfragment" + chat_fragment.hashCode());

        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, game_fragment);
        transaction.add(R.id.container, chat_fragment);
        transaction.hide(chat_fragment);
        transaction.show(game_fragment);
        //(game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.INVISIBLE);
        //(game_fragment.getView().findViewById(R.id.bar)).setVisibility(View.INVISIBLE);
        boolean tmp = transaction.isAddToBackStackAllowed();
        Log.i(TAG, "stack allow:" + tmp);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.i(TAG, "try open game fragment");


        if (flag.equals("notrenew")) {
            task = new MyTask();
            task.executeOnExecutor(executor);
            build_new_game_message = new StringBuilder();
            build_new_game_message.append(NEW_GAME + EQUALS).append(token);
            new_game_message = build_new_game_message.toString();

            while (true) {
                if (mClient != null && mClient.clientHasRun()) {
                    mClient.sendMessage(new_game_message);
                    break;
                }
            }
            Log.i(TAG, "send token");
            Log.i(TAG, "create MyTask: " + task.hashCode());
            Log.i(TAG, "task:" + task);
           // Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Online onCreate()");

        } else if(flag.equals(RENEW)){
            task = new MyTask();
            task.executeOnExecutor(executor);
            loadPreferences();
            Log.i(TAG, "after load pref");
            build_new_game_message = new StringBuilder();
            build_new_game_message.append(RENEW + EQUALS).append(token).append(EQUALS+OWN_RIDDLE+EQUALS+own_riddle+EQUALS+ENEMY_TOKEN+EQUALS+enemy_token);
            new_game_message = build_new_game_message.toString();
            Log.i(TAG, "after build message");
            while (true) {
                if (mClient != null && mClient.clientHasRun()) {
                    mClient.sendMessage(new_game_message);
                    break;
                }
            }
            Log.i(TAG, "after send message");

            Log.i(TAG, "new game message when renew " + new_game_message);
            Log.i(TAG, "create MyTask: " + task.hashCode());
            Log.i(TAG, "task:" + task);


            //Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Online onCreate()");

        }
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
                        own_riddle = array[1];
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
    public void onBackPressed() {
        if (check()) {
            manager.popBackStack();
            transaction = manager.beginTransaction();
            transaction.show(game_fragment);
            transaction.hide(chat_fragment);
            transaction.commit();

        } else {
            Intent intent  = new Intent();

            if (mClient != null) {
                boolean a = mClient.sendMessage(RENEW);
                if (!a) {
                    Log.i(TAG, "can't send riddle");
                }
            }
            Log.i(TAG, "befor stop client");
            mClient.stopClient();
            Log.i(TAG, "cafter stop client");
            saveDataToReferences();

            Log.i(TAG, "after save data");
            intent.putExtra("flag",RENEW);
            Log.i(TAG, "can't send riddle");
            setResult(RESULT_OK,intent);

            finish();
            super.onBackPressed();
        }

    }

    private void saveDataToReferences(){
        SharedPreferences.Editor  editor = game_pref.edit();
        editor.remove(ENEMY_TOKEN+id);
        editor.remove(ENEMY_LOG+id);
        editor.remove(OWN_LOG+id);
        editor.remove(CHAT_LOG+id);
        editor.remove(OWN_RIDDLE+id);
        editor.putString(ENEMY_LOG+id,enemy_log);
        editor.putString(OWN_LOG+id,own_log);
        editor.putString(CHAT_LOG+id,chat_log);
        editor.putString(OWN_RIDDLE+id,own_riddle);
        editor.putString(ENEMY_TOKEN+id,enemy_token);
        editor.apply();

    }

    public class MyTask extends AsyncTask<String, String, Client> {




        @Override
        protected Client doInBackground(String... message) {


            Log.i(TAG, "befor client");

            mClient = new Client(new Client.OnMessageReceived() {
                @Override

                public void messageReceived(String message) {

                    publishProgress(message);
                }
            },task,0);
            Log.i(TAG, "after client");
            mClient.run();
            Log.i(TAG, "after run");
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
                    Log.i(TAG, enemy_attempt_array[1] + ":::::" + enemy_log);
                    ((TextView) game_fragment.getView().findViewById(R.id.enemyLog)).setText(enemy_log);
                    Log.i(TAG, enemy_attempt_array[1] + ":::::" + enemy_log);
                } else if (values[0].startsWith(CHAT)) {
                    String[] chat_array = values[0].split(EQUALS);
                    chat_log += chat_array[1].trim() + "\n";
                    ((TextView) chat_fragment.getView().findViewById(R.id.chatLog)).setText(chat_log);
                }else if(values[0].startsWith(ENEMY_TOKEN)){
                    String [] token_aray = values[0].split(EQUALS);
                    enemy_token = token_aray[1];
                    Log.i(TAG, values[0]);
                }
                else {
                    //Toast.makeText(game_fragment.getActivity(), values[0], Toast.LENGTH_SHORT).show();
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
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setText("Your riddle is " + rid);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setEnabled(false);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setCursorVisible(false);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setBackgroundColor(Color.TRANSPARENT);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setKeyListener(null);
        (game_fragment.getView().findViewById(R.id.tryNumber)).setEnabled(true);
        (game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.VISIBLE);
        (game_fragment.getView().findViewById(R.id.buttonChat)).setVisibility(View.VISIBLE);
    }



    private void loadPreferences(){

        enemy_log = game_pref.getString(ENEMY_LOG+id,"shit enemy");
        own_log = game_pref.getString(OWN_LOG+id,"shit own");
        chat_log = game_pref.getString(CHAT_LOG+id,"shit chat");
        own_riddle = game_pref.getString(OWN_RIDDLE+id,"1234");
        enemy_token = game_pref.getString(ENEMY_TOKEN+id,"1111111");

    }

    private void setSaves(){
        (game_fragment.getView().findViewById(R.id.buttonRiddle)).setVisibility(View.INVISIBLE);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setText("your riddle" + own_riddle);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setEnabled(false);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setCursorVisible(false);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setBackgroundColor(Color.TRANSPARENT);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setKeyListener(null);
        (game_fragment.getView().findViewById(R.id.tryNumber)).setEnabled(true);
        (game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.VISIBLE);
        ((TextView) game_fragment.getView().findViewById(R.id.ownLog)).setText(own_log);
        ((TextView) game_fragment.getView().findViewById(R.id.enemyLog)).setText(enemy_log);
        ((TextView) chat_fragment.getView().findViewById(R.id.chatLog)).setText(chat_log);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onStart()");
    }

    @Override
    protected void onResume() {

        super.onResume();
        if(flag.equals(RENEW)){
        setSaves();
        Log.i(TAG, "after set saves");}
        else if(flag.equals("notrenew")){
            (game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.INVISIBLE);
            (game_fragment.getView().findViewById(R.id.buttonChat)).setVisibility(View.INVISIBLE);
        }
        //Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

       //Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onRestart()");



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }



}
