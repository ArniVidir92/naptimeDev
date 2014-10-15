package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by arni on 10/11/14.
 */
public class Contacts extends Fragment {

    DbHelper dbhelper;
    SQLiteDatabase db;
    private View view = null;
    ListView listView;
    String name;
    Cursor cursor;

    List<String> listItems=new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.lay_contacts, container, false);

        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();
        String[] columns = {"name"};
        cursor = db.query("CONTACTS",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            listItems.add(name);
        }

        listView = (ListView) view.findViewById(R.id.contacts_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.lay_contacts_row, R.id.listText, listItems);
        listView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
