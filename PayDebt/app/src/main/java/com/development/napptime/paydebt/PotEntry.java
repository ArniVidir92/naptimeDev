package com.development.napptime.paydebt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Snorri on 30.10.2014.
 */
public class PotEntry extends Fragment {

    private View view = null;
    private Button addEntry = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_moneypot, container, false);

        return view;
    }
}
