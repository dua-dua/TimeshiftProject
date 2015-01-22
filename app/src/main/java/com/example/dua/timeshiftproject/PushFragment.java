package com.example.dua.timeshiftproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;


public class PushFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_push, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        initControls();
    }

    private void initControls(){
        final Button button = (Button) getView().findViewById(R.id.btnPushSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText)getView().findViewById(R.id.pushText);
                String pushText = editText.getText().toString();

                ParseObject pushedText = new ParseObject("PushedText");
                pushedText.put("Test", pushText);
                pushedText.saveInBackground();

                Toast toast = Toast.makeText(getActivity(), "Item pushed", Toast.LENGTH_LONG);
                toast.show();
                ((EditText)getView().findViewById(R.id.pushText)).setText("");
            }
        });
    }


}