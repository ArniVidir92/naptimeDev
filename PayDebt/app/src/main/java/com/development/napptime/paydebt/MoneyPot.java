package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napptime on 10/11/14.
 *
 */
public class MoneyPot extends Fragment {

    private View view = null;
    private Button addEntry = null;

    DbHelper dbhelper;
    SQLiteDatabase db;

    Cursor cursor;

    ListView listView;

    String name;
    double amount;

    List<String> listItemsName=new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.lay_money_pot, container, false);

        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry(v);
            }
        });

        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        String[] columns = {"name", "amount"};

        cursor = db.query("POTS",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getDouble(1);

            listItemsName.add(name + ":   " + amount);
        }

        listView = (ListView) view.findViewById(R.id.mp_nonscroll_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_money_pot_row, R.id.rowEntry, listItemsName);
        listView.setAdapter(adapter);

        return view;
    }
    public void addEntry(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToPotEntry();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}


