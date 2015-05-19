package com.fupm.skeb.androidclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class OnlineGame extends ActionBarActivity {
    private Button check,riddle;
    private EditText try_number,create_number;
    private ListView own,enemy;
    private Client mClient;
    private ArrayList<String> myList,enemyList;
    private MyAdapter myAdapter;
    private EnemyAdapter enemyAdapter;
    private int createNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_online);
        check = (Button)findViewById(R.id.buttonSet);
        riddle =(Button)findViewById(R.id.buttonRiddle);
        try_number = (EditText)findViewById(R.id.tryNumber);
        create_number = (EditText)findViewById(R.id.setRiddle);
        own = (ListView)findViewById(R.id.listView);
        enemy = (ListView)findViewById(R.id.listView2);


        //createNumber = Integer.parseInt(create_number.getText().toString());
        //isCorrectInput(createNumber);




        myAdapter = new MyAdapter(this,myList);
        own.setAdapter(myAdapter);
        //enemyAdapter = new EnemyAdapter(this,enemyList);
        //enemy.setAdapter(enemyAdapter);

        new MyTask().execute("");

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = try_number.getText().toString();

                if (mClient != null) {
                    mClient.sendNumber(number);
                }

            }
        });
    }

    /*private boolean isCorrectInput(int tryNumber){

        int[] array = new int[4];
        for(int i = 0; i < 4; i++){
            array[i] = tryNumber % 10;
            tryNumber /= 10;
        }
        int k = 0;
        for(int i = 0; i < 3; i++)
            for(int j = i + 1; j < 4; j++)
                if (array[i] == array[j]) k++;
        if (k == 0) return true;
        else return false;
    }*/


    public class MyTask extends AsyncTask<String,String,Client> {

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


        }
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
}
