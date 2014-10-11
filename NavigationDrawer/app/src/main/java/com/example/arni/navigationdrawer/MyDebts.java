package com.example.arni.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by arni on 10/9/14.
 */
public class MyDebts extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_mydebts, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.textViewMyDebts);
        tv.setText("asdfasdf");
        return rootView;
    }
}
