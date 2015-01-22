package com.example.dua.timeshiftproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


public class PushedFragment extends Fragment {
    private ParseQueryAdapter<ParseObject> pushedAdapter;
    private ListView listView;

    public void onStart(){
        super.onStart();
        pushedList();
    }

    private void pushedList() {
        pushedAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), "PushedText");
        pushedAdapter.setTextKey("Test");

        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(pushedAdapter);
        pushedAdapter.loadObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_pushed, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
}