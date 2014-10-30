package com.development.napptime.paydebt;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by napptime on 10/11/14.
 *
 */
public class MoneyPot extends Fragment {

    private View view = null;
    private Button addEntry = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_money_pot, container, false);

        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry(v);
            }
        });

        return view;
    }

    public void addEntry(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToPotEntry();
    }
}


