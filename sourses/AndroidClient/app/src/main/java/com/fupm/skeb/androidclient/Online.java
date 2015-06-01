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
    private static final String ENEMY_ATTEMPT = "enemy_attempt";
    private static final String STARTCHAT = "startChat";
    private static final String GAME_PREFERENCES = "game";
    private static final String ENEMY_LOG = "enemylog";
    private static final String OWN_LOG = "ownlog";
    private static final String CHAT_LOG = "chatlog";
    private static final String TASK_LOG = "tasklog";
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
    private long id;
    private volatile MyTask task;
    private Intent intent;
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
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
        pref = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        editor = pref.edit();
        id = intent.getLongExtra("id", 0);


        Log.i(TAG, "task get from intent" + task);
        Log.i(TAG, "online item id:" + id);
        Log.i(TAG, "create MainActivity: " + this.hashCode());

        game_fragment = new GameFragment();
        chat_fragment = new ChatFragment();
        token = getAccessToken();
        Log.i(TAG, "userID: " + token.userId);

        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, game_fragment);
        transaction.add(R.id.container, chat_fragment);
        transaction.hide(chat_fragment);
        transaction.show(game_fragment);
        boolean tmp = transaction.isAddToBackStackAllowed();
        Log.i(TAG, "stack allow:" + tmp);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.i(TAG, "try open game fragment");

        task = (MyTask) getLastCustomNonConfigurationInstance();
        Log.i(TAG, "task after getcustom  : "+task);
        if (task == null) {
            task = new MyTask();
            task.executeOnExecutor(executor);
            task.link(this);
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
            Log.i(TAG, "create MyTask: " + task.hashCode());
            Log.i(TAG, "task:" + task);
            Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Online onCreate()");
            task.link(this);
        } else {
            Log.i(TAG, "create MyTask: " + task.hashCode());
            Log.i(TAG, "task:" + task);


            Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Online onCreate()");
            task.link(this);
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

          onRetainCustomNonConfigurationInstance();
            super.onBackPressed();
        }

    }

    public class MyTask extends AsyncTask<String, String, Client> {
        Online activity;


        void link(Online act) {
            activity = act;
        }

        void unLink() {
            activity = null;
        }
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
            if(activity!=null) {
                if (values[0].startsWith(GAME + EQUALS + OWN_ATTEMPT)) {
                    String[] own_attempt_array = values[0].split(EQUALS);
                    own_log += own_attempt_array[2].trim() + "\n";
                    ((TextView) activity.game_fragment.getView().findViewById(R.id.ownLog)).setText(own_log);
                    Log.i(TAG, own_attempt_array[1] + ":::::" + own_log);
                } else if (values[0].startsWith(GAME + EQUALS + ENEMY_ATTEMPT)) {
                    String[] enemy_attempt_array = values[0].split(EQUALS);
                    enemy_log += enemy_attempt_array[2].trim() + "\n";
                    ((TextView) activity.game_fragment.getView().findViewById(R.id.enemyLog)).setText(enemy_log);
                    Log.i(TAG, enemy_attempt_array[1] + ":::::" + enemy_log);
                } else if (values[0].startsWith(CHAT)) {
                    String[] chat_array = values[0].split(EQUALS);
                    chat_log += chat_array[1].trim() + "\n";
                    ((TextView) activity.chat_fragment.getView().findViewById(R.id.chatLog)).setText(chat_log);
                } else {
                    Toast.makeText(activity.game_fragment.getActivity(), values[0], Toast.LENGTH_SHORT).show();
                    Log.i(TAG, values[0]);


                }

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
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setText(R.string.yourRiddle + rid);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setEnabled(false);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setCursorVisible(false);
        (game_fragment.getView().findViewById(R.id.setRiddle)).setBackgroundColor(Color.TRANSPARENT);
        ((EditText)game_fragment.getView().findViewById(R.id.setRiddle)).setKeyListener(null);
        (game_fragment.getView().findViewById(R.id.tryNumber)).setEnabled(true);
        (game_fragment.getView().findViewById(R.id.buttonSet)).setVisibility(View.VISIBLE);
    }


    private void savePreferences(){
        SharedPreferences online_saves = getSharedPreferences(GAME_PREFERENCES+id, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = online_saves.edit();
        editor.putString(ENEMY_LOG,enemy_log);
        editor.putString(OWN_LOG,own_log);
        editor.putString(CHAT_LOG,chat_log);
        editor.commit();

    }

    private void loaddPreferences(){
        SharedPreferences online_saves = getSharedPreferences(GAME_PREFERENCES+id, Context.MODE_PRIVATE);
        enemy_log = online_saves.getString(ENEMY_LOG,"shit enemy");
        own_log = online_saves.getString(OWN_LOG,"shit own");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

       Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Online onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    public Object onRetainCustomNonConfigurationInstance (){
        task.unLink();
        return task;
    }


}
