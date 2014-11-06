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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by napptime on 10/11/14.
 *
 */
public class MoneyPot extends Fragment {

    private View view = null;
    private Button addEntry = null;

    private TextView showTotalAmount;

    DbHelper dbhelper;
    SQLiteDatabase db;

    Cursor cursor;

    ListView listView;

    Integer total_amount;
    Integer numOfContacts;

    String name;
    Integer amount;

    List<String> listItemsName=new ArrayList<String>();
    List<String> calculatedPayments=new ArrayList<String>();

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
        total_amount = 0;

        numOfContacts = 0;
        HashSet uniqueNames = new HashSet();

        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getInt(1);
            total_amount += amount;

            uniqueNames.add(name);

            listItemsName.add(name + ":   " + amount);
        }

        numOfContacts = uniqueNames.size();

        String bla= numOfContacts.toString();
        Toast.makeText(getActivity(), bla,
                Toast.LENGTH_SHORT).show();

        showTotalAmount = (TextView) view.findViewById(R.id.showTotalAmount);
        showTotalAmount.setText(String.valueOf(total_amount));

        listView = (ListView) view.findViewById(R.id.mp_nonscroll_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_money_pot_row, R.id.rowEntry, listItemsName);
        listView.setAdapter(adapter);

        String contact;
        Integer split;
        Integer paid;
        Integer newAmount;
        Integer gets;
        Integer pays;
        split = total_amount/numOfContacts;
        //select name, sum(amount) from pots group by name;
        Cursor cursorAmount = db.rawQuery(
                "SELECT name, sum(amount) FROM POTS GROUP BY name", null);
        while(cursorAmount.moveToNext()) {
            contact = cursorAmount.getString(0);
            paid = cursorAmount.getInt(1);
            newAmount = paid-split;
            if(paid>split) {
                gets = newAmount;
                Toast.makeText(getActivity(), contact +" gets " + gets.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            if(paid<split) {
                pays = -newAmount;
                Toast.makeText(getActivity(),contact + " pays " + pays.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            if(paid.equals(split)) {
                Toast.makeText(getActivity(),contact + " is good",
                    Toast.LENGTH_SHORT).show();
            }

        }


        return view;
    }

    public void calculateSplit(View v){

        /*Pseudo code
        split = total_amount/numOfContacts;
        amount=paid-split;
        if (paid>split) contactGets = amount;
        if (paid<split) contactPays = amount;
        if (paid=split) do nothing;
        */
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


