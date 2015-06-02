package com.fupm.skeb.androidclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ChatFragment extends Fragment {

    private ChatListener mListener;
    private Button send;
    private EditText message;
    private TextView chat_log;
    private StringBuilder message_builder;
    private static final String CHAT = "chat";
    private static final String EQUALS = "=";
    private int groundIndex;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String KEY_RADIOBUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    public static final String BACKGROUND_INDEX = "BACKGROUND_DRAWABLE_INDEX";
    private RelativeLayout mRelativeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        groundIndex = mSettings.getInt(BACKGROUND_INDEX, 0);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat,null);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.chat_relative_layout);
        mRelativeLayout.setBackgroundResource(groundIndex);
        send = (Button)v.findViewById(R.id.buttonSend);
        message = (EditText)v.findViewById(R.id.message);
        chat_log = (TextView)v.findViewById(R.id.chatLog);
        message_builder = new StringBuilder();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cMessage = message.getText().toString();
                message.setText("");
                message_builder.delete(0,message_builder.length());
                message_builder.append(CHAT).append(EQUALS).append(cMessage);
                cMessage = message_builder.toString();
                mListener.ChatListener(cMessage);
            }
        });
        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ChatListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ChatListener {
        // TODO: Update argument type and name
        public void ChatListener(String s);
    }

}
