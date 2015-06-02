package com.fupm.skeb.androidclient;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.vk.sdk.VKAccessToken;
import static com.vk.sdk.VKSdk.getAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;


public class MainActivity extends FragmentActivity {
    private String TAG  = "MainAct";
    private String uri = "http://192.168.0.101:10100/test";

    public static final String APP_PREFERENCES_1 = "tokensettings";
    public static final String APP_PREFERENCES_TOKEN = "usertoken";
    private SharedPreferences token_prefarences;
    private VKAccessToken token;

    private Button btnActTwo, btnResult;
    private Button background, rules;
    private MyTextView textView4;

    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";

    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActTwo = (Button) findViewById(R.id.btnActTwo);
        btnResult = (Button) findViewById(R.id.btnResult);

        token_prefarences = getSharedPreferences(APP_PREFERENCES_1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = token_prefarences.edit();
        if(!token_prefarences.contains(APP_PREFERENCES_TOKEN)){
            token = getAccessToken();
            editor.putString(APP_PREFERENCES_TOKEN,token.userId);
            editor.apply();
        }

        textView4 = (MyTextView) findViewById(R.id.textView4);
        background = (Button) findViewById(R.id.background);
        rules = (Button) findViewById(R.id.rules);

        final VKRequest request = VKApi.users().get();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKList list = (VKList) response.parsedModel;
                VKApiUser user = (VKApiUser) list.get(0);

                if (user == null) {
                    Log.v("Ошибка", "Ничего не загрузилось");
                } else {
                    Log.v("User name:", user.first_name + user.last_name);
                    textView4.setText(user.first_name + " " + user.last_name);
                }
            }
        });

        onResume(); // load or change background

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

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rules:
                        // TODO Call second activity

                        Intent intent = new Intent(MainActivity.this, Rules.class);
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

        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        if (mSettings.contains(BACKGROUND_INDEX)) {

            int groundIndex = mSettings.getInt(BACKGROUND_INDEX, 0);
            mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
            mRelativeLayout.setBackgroundResource(groundIndex);
        }
        else{

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(BACKGROUND_INDEX, R.drawable.p12);
            editor.apply();

            onResume();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
