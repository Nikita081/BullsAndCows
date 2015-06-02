package com.fupm.skeb.androidclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class ListGame extends FragmentActivity {

    private Button gameComp;
    private Button gameOnline;


    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_game);

        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int groundIndex = mSettings.getInt(BACKGROUND_INDEX, 0);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        mRelativeLayout.setBackgroundResource(groundIndex);

        gameComp = (Button) findViewById(R.id.comp_game);
        gameComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.comp_game:
                        // TODO Call second activity
                        Intent intent = new Intent(ListGame.this, Game.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        gameOnline = (Button) findViewById(R.id.human_game);
        gameOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.human_game:
                        // TODO Call second activity

                        Intent intent = new Intent(ListGame.this, ListOnlineGame.class);
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
        getMenuInflater().inflate(R.menu.menu_list_game, menu);
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
