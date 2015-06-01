package com.fupm.skeb.androidclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ListOnlineGame extends FragmentActivity {

    private int listNumbers = 0;
    private Button button;
    private ListView listView;
    private ArrayList<String> session = new ArrayList<String>();

    //PREFERENCES
    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    private RelativeLayout mRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online_game);

//        onResume(); // load or change background

        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button3);

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
                startActivity(intent);
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
        //mistake:
        //mRelativeLayout.setBackgroundResource(mBackground.choose(groundIndex));
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
}
