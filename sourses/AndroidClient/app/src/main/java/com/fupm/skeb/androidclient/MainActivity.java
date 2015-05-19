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
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends ActionBarActivity {
    private TextView hello1,hello2;
    private String TAG  = "MainAct";

    private Button btnActTwo,button,btnResult;

    private static int j=0;
    RequestQueue queue;



    private Client mClient;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello1 = (TextView)findViewById(R.id.textView1);
        hello2 = (TextView)findViewById(R.id.textView1);
        queue =  Volley.newRequestQueue(this);
        btnActTwo = (Button) findViewById(R.id.btnActTwo);

        button = (Button)findViewById(R.id.button);

        btnResult = (Button) findViewById(R.id.btnResult);


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
                URL uri = null;
                try {
                    uri = new URL("http://192.168.0.101:10100?data=testmessage");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) uri.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);


                    BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    String mes = "test message!";
                    byte message[] = mes.getBytes();

                    Log.i(TAG, "message  :::  " + mes);
                    out.write(message);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }


            }
        });


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
