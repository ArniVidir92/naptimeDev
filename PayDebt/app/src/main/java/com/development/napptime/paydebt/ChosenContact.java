package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by arni on 10/11/14.
 */
public class ChosenContact extends Fragment {

    private String _name;
    private Button addDebt = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_chosen_contact, container, false);
        DbHelper db = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);


        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebt(v);
            }
        });


        return view;


    }

    public void addDebt(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToAddDebt();
        Log.d("Villa","Hallodrasl");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



}
