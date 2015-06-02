package com.fupm.skeb.androidclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;

public class GameFragment extends Fragment {
    private Button check, riddle,chat;
    private EditText try_number, create_number;
    private String TAG = "gameFragment";
    private static final String RIDDLE = "riddle";
    private static final String NUMBER = "number";
    private static final String EQUALS = "=";
    private static final String GAME = "game";
    private static final String STARTCHAT = "startChat";
    private StringBuilder build_new_game_message;
    public static final String APP_PREFERENCES = "mysettings";
    private int groundIndex;
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";
    private RelativeLayout mRelativeLayout;
    public interface AttemptsListener{
        public void doAttempt(String s);
    }
    private AttemptsListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AttemptsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AttemptListener");
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        groundIndex = mSettings.getInt(BACKGROUND_INDEX, 0);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game,null);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.game_fragment_relative);
        mRelativeLayout.setBackgroundResource(groundIndex);
        build_new_game_message = new StringBuilder();
        check = (Button) v.findViewById(R.id.buttonSet);
        chat = (Button)v.findViewById(R.id.buttonChat);
        riddle = (Button) v.findViewById(R.id.buttonRiddle);
        try_number = (EditText) v.findViewById(R.id.tryNumber);
        create_number = (EditText) v.findViewById(R.id.setRiddle);

        riddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_number.getText().toString().length() == 4) {
                    String riddle = create_number.getText().toString();
                    int send = Integer.parseInt(riddle);
                    create_number.setText("");

                    if (checkRid(send)) {
                        build_new_game_message.delete(0, build_new_game_message.length());
                        build_new_game_message.append(RIDDLE + EQUALS).append(riddle);
                        riddle = build_new_game_message.toString();
                        mListener.doAttempt(riddle);


                    } else {
                        Toast toast = Toast.makeText(getActivity(),
                                R.string.correctInputNumber, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            R.string.only4num, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (try_number.getText().toString().length() == 4) {
                    String number = try_number.getText().toString();
                    try_number.setText("");
                    build_new_game_message.delete(0, build_new_game_message.length());
                    build_new_game_message.append(GAME + EQUALS + NUMBER + EQUALS).append(number);
                    number = build_new_game_message.toString();

                    mListener.doAttempt(number);

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            R.string.only4num, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.doAttempt(STARTCHAT);
            }
        });


        return v;
    }


    public boolean checkRid(int ridd){
        boolean a;
        int [] riddle = new int [4];
        for (int i = 0; i < 4; i++){
            riddle[i] = ridd % 10;
            ridd = ridd / 10;
        }

        a = !((riddle[0] == riddle[1] || riddle[0] == riddle[2] || riddle[0] == riddle[3]) || (riddle[1] == riddle[2] || riddle[1] == riddle[3]) || (riddle[2] == riddle[3]));
        return a;
    }


}


