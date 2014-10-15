package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by arni on 10/11/14.
 */
public class ChosenContact extends Fragment {

    private String _name;
    private Button addDebt = null;

    DbHelper dbhelper;
    SQLiteDatabase db;
    private View view = null;
    ListView listView;
    String name;
    double amount;
    Cursor cursor;

    List<String> listItemsName=new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_chosen_contact, container, false);

        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebt(v);
            }
        });


        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();
        String[] columns = {"name", "amount"};
        cursor = db.query("DEBTS",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getDouble(1);

            listItemsName.add(name + ":   " + amount);
        }

        listView = (ListView) view.findViewById(R.id.lv_nonscroll_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.lay_chosen_contact_row, R.id.chosenContactName, listItemsName);

        listView.setAdapter(adapter);


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
