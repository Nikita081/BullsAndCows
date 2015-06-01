package com.fupm.skeb.androidclient;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;


public class MainActivity extends FragmentActivity {
    private String TAG  = "MainAct";
    private String uri = "http://192.168.0.101:10100/test";
    private Button btnActTwo, btnResult, button2;
    private Button background;
    private TextView textView1;

    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActTwo = (Button) findViewById(R.id.btnActTwo);
        btnResult = (Button) findViewById(R.id.btnResult);
        textView1 = (TextView) findViewById(R.id.textView1);
        background = (Button) findViewById(R.id.background);

        onResume(); // load or change background

//        button2.setOnClickListener(new  View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final VKRequest request = VKApi.users().get();
//                request.executeWithListener(new VKRequest.VKRequestListener() {
//                    @Override
//                    public void onComplete(VKResponse response) {
//                        super.onComplete(response);
//
//                        //Log.v("VK SDK",response.json.toString());
//
//                        VKList list = (VKList) response.parsedModel;
//                        VKApiUser user = (VKApiUser) list.get(0);
//
//                        if (user == null) {
//                            Log.v("Ошибка", "Ничего не загрузилось");
//                        } else {
//                            Log.v("User name:", user.first_name + user.last_name);
//                            textView1.setText(user.first_name + " " + user.last_name);
//                        }
//                    }
//                });
//            }
//        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.background:
                        // TODO Call second activity

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
    protected void onResume() {
        super.onResume();

        ChangeBackground mBackground = new ChangeBackground();

        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int groundIndex = mSettings.getInt(KEY_RADIOBUTTON_INDEX, 0);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        mRelativeLayout.setBackgroundResource(mBackground.choose(groundIndex));
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
