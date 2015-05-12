package com.fupm.skeb.androidclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private TextView hello1,hello2;
    private EditText email,password;
    private Button btnActTwo,button;
    private Client mClient;
    private static int j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello1 = (TextView)findViewById(R.id.textView);
        hello2 = (TextView)findViewById(R.id.textView1);
        email = (EditText)findViewById(R.id.editText1);
        password = (EditText)findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        btnActTwo = (Button) findViewById(R.id.btnActTwo);

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

        new MyTask().execute("");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (mClient != null) {
                    //mClient.sendEP(mail,pass);
                }

            }
        });
    }

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

            if(j==0){
                hello1.setText(values[0]);
                j++;
            }
            else if(j==1){
                hello2.setText(values[0]);
                j--;
            }
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
