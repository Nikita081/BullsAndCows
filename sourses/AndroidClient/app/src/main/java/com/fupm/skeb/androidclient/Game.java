package com.fupm.skeb.androidclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    BodyGame bullcow  = new BodyGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
                        winVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
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
}

