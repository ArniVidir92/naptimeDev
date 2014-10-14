package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by arni on 10/11/14.
 */
public class Contacts extends Fragment {

    private String _name;
    DbHelper db = new DbHelper(getActivity());

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lay_contacts, container, false);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

}
