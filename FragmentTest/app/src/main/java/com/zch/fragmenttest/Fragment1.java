package com.zch.fragmenttest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Fragment1 extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        String msg = ((TabFragmentActivity)context).sendMsg();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Log.e("TAG", "Fragment-onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "Fragment-onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("TAG", "Fragment-onCreateView: ");
        View view=inflater.inflate(R.layout.fragment_frament1, container, false);

        Bundle b =getArguments();
        String msg = b.getString("msg1");
        ((TextView)view.findViewById(R.id.text1)).setText(msg);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG", "Fragment-onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "Fragment-onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "Fragment-onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG", "Fragment-onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("TAG", "Fragment-onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "Fragment-onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("TAG", "Fragment-onDetach: ");
    }
}
