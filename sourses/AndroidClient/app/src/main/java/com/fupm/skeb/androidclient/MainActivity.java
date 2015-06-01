package com.fupm.skeb.androidclient;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {
    private String TAG  = "MainAct";
    private String uri = "http://192.168.0.101:10100/test";
    private Button btnActTwo, btnResult;
    private Button change;

    public RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActTwo = (Button) findViewById(R.id.btnActTwo);
        btnResult = (Button) findViewById(R.id.btnResult);
        change = (Button) findViewById(R.id.change);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        mRelativeLayout.setBackgroundResource(R.drawable.ferma);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.change:
                        // TODO Call second activity

                        //mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.backgraund_colour));

                        //ListGame trew = new ListGame();
                        //mRelativeLayout.setBackgroundResource(trew.check());

                        Intent intent = new Intent(MainActivity.this, Background.class);
                        startActivity(intent);

                        break;
                    default:
                        break;
                }
            }
        });

        btnActTwo.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
