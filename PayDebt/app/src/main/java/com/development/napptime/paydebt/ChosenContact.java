package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by arni on 10/11/14.
 */
public class ChosenContact extends Fragment {

    private String _name;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DbHelper db = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        return inflater.inflate(R.layout.lay_chosen_contact, container, false);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



}
