package com.fupm.skeb.androidclient;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class GameOnline extends FragmentActivity {
    private Button check, riddle;
    private EditText try_number, create_number;
    private TextView own, enemy;
    private Client mClient;
    private static final String RIDDLE = "riddle";
    private static final String NUMBER = "number";
    private static final String EQUALS = "=";
    private static final String NEW_GAME = "newgame";
    private static final String OWN_ATTEMPT = "own_attempt";
    private static final String ENEMY_ATTEMPT = "enemy_attempt";


    private String TAG = "Life circle";
    private String new_game_message;
    private StringBuilder build_new_game_message;
    private StringBuilder log = new StringBuilder();
    private String enemy_log;
    private String own_log;
    private String vkToken = "12349";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_online);

        check = (Button) findViewById(R.id.buttonSet);
        riddle = (Button) findViewById(R.id.buttonRiddle);
        try_number = (EditText) findViewById(R.id.tryNumber);
        create_number = (EditText) findViewById(R.id.setRiddle);
        enemy = (TextView) findViewById(R.id.enemyLog);
        own = (TextView) findViewById(R.id.ownLog);
        check.setVisibility(View.INVISIBLE);

        new MyTasks().execute("");
        build_new_game_message = new StringBuilder();
        build_new_game_message.append(NEW_GAME + EQUALS).append(vkToken);
        new_game_message = build_new_game_message.toString();
        while (true) {
            if (mClient != null && mClient.clientHasRun()) {
                mClient.sendMessage(new_game_message);
                break;
            }
        }


        riddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_number.getText().toString().length() == 4) {
                    String riddle = create_number.getText().toString();
                    int send = Integer.parseInt(riddle);
                    create_number.setText("");

                    if (checkRid(send)) {
                        build_new_game_message.delete(0, build_new_game_message.length());
                        build_new_game_message.append(RIDDLE + EQUALS).append(riddle);
                        riddle = build_new_game_message.toString();

                        if (mClient != null) {
                        boolean a = mClient.sendMessage(riddle);
                        if (!a) {
                            Log.i(TAG, "can't send riddle");
                        } else {
                            isend(send);
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.correctInputNumber, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.only4num, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (try_number.getText().toString().length() == 4) {
                    String number = try_number.getText().toString();
                    build_new_game_message.delete(0, build_new_game_message.length());
                    build_new_game_message.append(NUMBER + EQUALS).append(number);
                    number = build_new_game_message.toString();

                    if (mClient != null) {
                        mClient.sendMessage(number);
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.only4num, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        //Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate()");
    }


    public class MyTasks extends AsyncTask<String, String, Client> {

        @Override
        protected Client doInBackground(String... message) {


            /*mClient = new Client(new Client.OnMessageReceived() {
                @Override

                public void messageReceived(String message) {

                    publishProgress(message);
                }
            });
            mClient.run();*/

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if (values[0].startsWith(OWN_ATTEMPT)) {
                String[] own_attempt_array = values[0].split(EQUALS);
                own_log += own_attempt_array[1].trim() + "\n";
                own.setText(own_log);
                Log.i(TAG, own_attempt_array[1] + ":::::" + own_log);
            } else if (values[0].startsWith(ENEMY_ATTEMPT)) {
                String[] enemy_attempt_array = values[0].split(EQUALS);
                enemy_log += enemy_attempt_array[1].trim() + "\n";
                enemy.setText(enemy_log);
                Log.i(TAG, enemy_attempt_array[1] + ":::::" + enemy_log);
            } else {
                Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
                Log.i(TAG, values[0]);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    public void onBackPressed() {

        //Toast.makeText(getApplicationContext(), "Back pressed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Back pressed");

        onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_online, menu);
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
        riddle.setVisibility(View.INVISIBLE);
        create_number.setText(R.string.yourRiddle +rid);
        create_number.setEnabled(false);
        create_number.setCursorVisible(false);
        create_number.setBackgroundColor(Color.TRANSPARENT);
        create_number.setKeyListener(null);
        try_number.setEnabled(true);
        check.setVisibility(View.VISIBLE);
    }

    public boolean checkRid(int ridd){
        boolean a;
        int [] riddle = new int [4];
        for (int i = 0; i < 4; i++){
            riddle[i] = ridd % 10;
            ridd = ridd / 10;
        }

        if ( (riddle[0] == riddle[1] || riddle[0] == riddle[2] || riddle[0] == riddle[3]) || (riddle[1] == riddle[2] || riddle[1] == riddle[3]) || (riddle[2] == riddle[3]) ){
            a=false;
        }
        else {
            a = true;
        }
        return a;
    }
}
