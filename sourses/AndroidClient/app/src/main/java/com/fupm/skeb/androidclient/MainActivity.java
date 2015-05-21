package com.fupm.skeb.androidclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import static android.os.SystemClock.sleep;


public class MainActivity extends ActionBarActivity {
    private TextView hello1,hello2;
    private String TAG  = "MainAct";
    private Button btnActTwo,button,btnResult;
    private HttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello1 = (TextView)findViewById(R.id.textView1);
        hello2 = (TextView)findViewById(R.id.textView1);

        btnActTwo = (Button) findViewById(R.id.btnActTwo);

        button = (Button)findViewById(R.id.button);

        btnResult = (Button) findViewById(R.id.btnResult);

        new MyTask().execute("");
        btnActTwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnActTwo:
                        // TODO Call second activity
                        Intent intent = new Intent(MainActivity.this, ListGame.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnResult:
                        // TODO Call second activity
                        Intent intent = new Intent(MainActivity.this, Results.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number ="Test Message!";

                if (mClient != null) {
                    try {
                        mClient.sendMessage(number);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public class MyTask extends AsyncTask<String,String,Client> {

        @Override
        protected Client doInBackground(String... message) {


            mClient = new HttpClient(new HttpClient.OnMessageReceived() {
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
            hello1.setText(values[0]);
            sleep(100);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
