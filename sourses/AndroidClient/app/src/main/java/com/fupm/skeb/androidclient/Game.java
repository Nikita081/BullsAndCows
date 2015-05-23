package com.fupm.skeb.androidclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Game extends ActionBarActivity {

    private EditText edTryNumber;
    private int tryNumber = 0;
    private String tryNumberString;
    private Button buttonSet;
    private Vibrator winVib;
    private int[] mycount = new int[10];
    private Button [] arrayButton = new Button[10];
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "single_result";
    private SharedPreferences mSettings;// = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


    BodyGame bullcow  = new BodyGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        set(arrayButton, mycount);
        for (int j = 0; j < 10; j++){
            change(arrayButton[j], mycount[j]);
        }

        buttonSet = (Button) findViewById(R.id.buttonSet);
        edTryNumber = (EditText) findViewById(R.id.tryNumber);
        bullcow.riddle();

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int len = edTryNumber.getText().toString().length();
                if (len == 4)
                {
                    tryNumber = Integer.parseInt(edTryNumber.getText().toString());
                    tryNumberString = edTryNumber.getText().toString();
                    edTryNumber.setText("");

                    TextView text = (TextView)findViewById(R.id.logtext);

                    int bulls = bullcow.countALL(tryNumber);

                    if (bulls == 4) {
                        winVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        int attemptCurrent = bullcow.getAttempt();
                        int attemptSettings = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
                        if (attemptCurrent < attemptSettings || attemptSettings == 0) {
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putInt(APP_PREFERENCES_COUNTER, attemptCurrent);
                            editor.apply();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                        builder.setTitle(R.string.compliment1)
                                .setMessage(bullcow.numberAttempts())
                                .setCancelable(false)
                                .setNeutralButton(R.string.compliment2,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        }

                    String st = bullcow.giveLog(tryNumberString);
                    text.setText(st);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.only4num, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    private void set(Button[] a, int[] count){
        a [0] = (Button)findViewById(R.id.number01);
        a [1] = (Button)findViewById(R.id.number02);
        a [2] = (Button)findViewById(R.id.number03);
        a [3] = (Button)findViewById(R.id.number04);
        a [4] = (Button)findViewById(R.id.number05);
        a [5] = (Button)findViewById(R.id.number06);
        a [6] = (Button)findViewById(R.id.number07);
        a [7] = (Button)findViewById(R.id.number08);
        a [8] = (Button)findViewById(R.id.number09);
        a [9] = (Button)findViewById(R.id.number00);

        for (int i = 0; i < 10; i++){
            a[i].setTextColor(getResources().getColor(R.color.grey));
            count[i] = 0;
        }
    }


    private void change (final Button a, final int count) {
        a.setOnClickListener(new View.OnClickListener() {
            int mycount = count;
            @Override
            public void onClick(View v) {


                if (mycount == 0) {
                    a.setTextColor(getResources().getColor(R.color.green));
                    mycount = 1;
                    return;
                }

                if (mycount == 1) {
                    a.setTextColor(getResources().getColor(R.color.red));
                    mycount = 2;
                    return;
                }

                if (mycount == 2) {
                    a.setTextColor(getResources().getColor(R.color.grey));
                    mycount = 0;
                    return;
                }
            }
        });

    }
}

