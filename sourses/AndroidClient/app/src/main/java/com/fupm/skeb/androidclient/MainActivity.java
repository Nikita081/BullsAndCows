package com.fupm.skeb.androidclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello1 = (TextView)findViewById(R.id.textView1);
        hello2 = (TextView)findViewById(R.id.textView1);

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
                try {
                    URL uri = new URL("http://192.168.0.101:10100");
                    HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();

                        urlConnection.setDoOutput(true);
                        urlConnection.setChunkedStreamingMode(0);

                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(urlConnection.getOutputStream())), true);
                        out.println("test message!!!!!!!");

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                } catch (MalformedURLException e) {
                    Log.e("MalformedURLException"+e,TAG);
                } catch (IOException e) {
                    Log.e("IOException"+e,TAG);
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
