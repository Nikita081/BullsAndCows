package com.fupm.skeb.androidclient;

import android.content.Intent;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import java.util.ArrayList;


public class ListOnlineGame extends FragmentActivity {

    private int listNumbers = 0;
    private Button button;
    private ListView listView;

    private String TAG = "ListOnline: ";
    private ArrayList<String> catnames = new ArrayList<String>();
    private int idd;
    private AsyncTask task  = null;
    private final int REQUEST_CODE_1 = 0;
    private final int REQUEST_CODE_2 = 1;
    private final int REQUEST_CODE_3 = 2;
    private final int REQUEST_CODE_4 = 3;
    private final int REQUEST_CODE_5 = 4;
    private final int REQUEST_CODE_6 = 5;
    private final int REQUEST_CODE_7 = 6;
    private final int REQUEST_CODE_8 = 7;
    private final int REQUEST_CODE_9 = 8;
    private final int REQUEST_CODE_10 = 9;
    private String [] renew_st_array = new String[10];
    private ArrayList<String> session = new ArrayList<String>();


    public static final String APP_PREFERENCES = "mysettings";

    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";

    private RelativeLayout mRelativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online_game);


        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int groundIndex = mSettings.getInt(BACKGROUND_INDEX, 0);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        mRelativeLayout.setBackgroundResource(groundIndex);


        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button3);
        for(int i = 0; i < 10; i++) renew_st_array[i] = "notrenew";
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_1, session);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.add(0, Integer.toString(++listNumbers) + " session");
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(ListOnlineGame.this, Online.class);
                intent.putExtra("flag", renew_st_array[(int)id]);
                intent.putExtra("id",(int)id);
                startWithOwnRequest((int)id,intent);
            }
        });
        //Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onCreate()");
    }

    private void startWithOwnRequest(int id,Intent intent){
        switch (id) {
            case REQUEST_CODE_1:
                startActivityForResult(intent, REQUEST_CODE_1);
                break;

            case REQUEST_CODE_2:
                startActivityForResult(intent, REQUEST_CODE_2);
                break;

            case REQUEST_CODE_3:
                startActivityForResult(intent, REQUEST_CODE_3);
                break;

            case REQUEST_CODE_4:
                startActivityForResult(intent, REQUEST_CODE_4);
                break;
            case REQUEST_CODE_5:
                startActivityForResult(intent, REQUEST_CODE_5);
                break;
            case REQUEST_CODE_6:
                startActivityForResult(intent, REQUEST_CODE_6);
                break;
            case REQUEST_CODE_7:
                startActivityForResult(intent, REQUEST_CODE_7);
                break;
            case REQUEST_CODE_8:
                startActivityForResult(intent, REQUEST_CODE_8);
                break;
            case REQUEST_CODE_9:
                startActivityForResult(intent, REQUEST_CODE_9);
                break;
            case REQUEST_CODE_10:
                startActivityForResult(intent, REQUEST_CODE_10);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // запишем в лог значения requestCode и resultCode
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        // если пришло ОК
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_1:
                    renew_st_array[REQUEST_CODE_1] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_2:
                    renew_st_array[REQUEST_CODE_2] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_3:
                    renew_st_array[REQUEST_CODE_3] = data.getStringExtra("flag");
                    break;

                case REQUEST_CODE_4:
                    renew_st_array[REQUEST_CODE_4] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_5:
                    renew_st_array[REQUEST_CODE_5] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_6:
                    renew_st_array[REQUEST_CODE_6] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_7:
                    renew_st_array[REQUEST_CODE_7] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_8:
                    renew_st_array[REQUEST_CODE_8] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_9:
                    renew_st_array[REQUEST_CODE_9] = data.getStringExtra("flag");
                    break;
                case REQUEST_CODE_10:
                    renew_st_array[REQUEST_CODE_10] = data.getStringExtra("flag");
                    break;
            }
            // если вернулось не ОК
        } else {
            //Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onResume()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_online_game, menu);
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
    @Override
    protected void onStart() {
        super.onStart();

        //Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onStart()");
    }



    @Override
    protected void onPause() {
        super.onPause();

        //Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ListOnline onDestroy()");
    }


}
