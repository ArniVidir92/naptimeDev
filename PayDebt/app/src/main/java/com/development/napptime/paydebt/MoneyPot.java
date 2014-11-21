package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
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
 * The money pot class serves the purpose of creating and working with
 * the moneypot feature. The moneypot feature allows users to divide the debts from a selection
 * of payments paid by separate people.
 */
public class MoneyPot extends Fragment {

    //Initialize variables
    private View view = null;
    private Button addEntry = null;

    private TextView showTotalAmount;
    
    //initialize variables for the database
    DbHelper dbhelper;
    SQLiteDatabase db;
    ContentValues contentvalues;
    Cursor cursor;

    ListView listView;
    ListView listSplit;

    //Initalize variables for later calculations
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
        //Get the addEntry button from the layout
        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry(v);
            }
        });

        //database variables
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //only get name and amount of money paid from the database
        String[] columns = {"name", "amount"};

        cursor = db.query("POTS",columns,null,null,null,null,null);
        total_amount = 0;

        numOfContacts = 0;
        HashSet uniqueNames = new HashSet();

        //Sum up the total amount of the moneypot
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getInt(1);
            total_amount += amount;

            uniqueNames.add(name);

            listItemsName.add(name + ":   " + amount);
        }

        //Number of unique names in the moneypot
        numOfContacts = uniqueNames.size();

        //Toast message to show the number of unique contacts
        String bla= numOfContacts.toString();
        Toast.makeText(getActivity(), bla,
                Toast.LENGTH_SHORT).show();

        //Show the sum of all the payments in the moneypot
        showTotalAmount = (TextView) view.findViewById(R.id.showTotalAmount);
        showTotalAmount.setText(String.valueOf(total_amount));

        //Get the list from the layout
        listView = (ListView) view.findViewById(R.id.mp_nonscroll_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_money_pot_row, R.id.rowEntry, listItemsName);
        listView.setAdapter(adapter);

        String contact;
        Integer split = 0;
        Integer paid;
        Integer newAmount;
        Integer gets;
        Integer pays;

        //NumOfContacts != 0
        if(!numOfContacts.equals(0)) {
            split = total_amount / numOfContacts;
        }

        //select name, sum(amount) from pots group by name;
        Cursor cursorAmount = db.rawQuery(
                "SELECT name, sum(amount) FROM POTS GROUP BY name", null);

        String[][] array = new String[numOfContacts][2];
        Integer i = 0;

        //Calculate which contact has to pay to the moneypot and which gets money from it.
        while(cursorAmount.moveToNext()) {
            contact = cursorAmount.getString(0);
            paid = cursorAmount.getInt(1);
            newAmount = paid-split;
            //if you pay more money than the average money paid, you will get money from the pot
            if(paid>split) {
                gets = newAmount;

                array[i][0] = contact;
                array[i][1] = gets.toString();

                calculatedPayments.add(contact + " gets:   " + gets);
            }
            //if you pay less money than the average money paid, you will have to pay to the pot
            if(paid<split) {
                pays = -newAmount;

                array[i][0] = contact;
                array[i][1] = pays.toString();

                calculatedPayments.add(contact + " pays:   " + pays);
            }
            //if you paid the exact average of the money paid to the pot, you will neither get
            //money nor have to pay money
            if(paid.equals(split)) {
                calculatedPayments.add(contact + ":   " + paid);
            }
            i +=1;
        }

        int swag;
        int nextSwag;
        int newAgeSwag;

        String swaggerOne = "";
        String swaggerTwo = "";

        for (int j = 0; j< array.length; j++) {
            for (int k=1; k< array.length; k++) {
                swag = Integer.parseInt(array[j][1]);
                nextSwag = Integer.parseInt(array[k][1]);

                swaggerOne = array[j][0];
                swaggerTwo = array[k][0];

                if (swag>nextSwag) {
                    newAgeSwag = split-swag;
                    calculatedPayments.add(swaggerOne + " pays: " + newAgeSwag + " to: " +  swaggerTwo);
                }

                if (swag<nextSwag) {
                    newAgeSwag = split-nextSwag;
                    calculatedPayments.add(swaggerOne + " gets: " + newAgeSwag + " from: " +  swaggerTwo);
                }

                if (swag ==nextSwag) {
                    //do nothing
                }
            }

        }

        listSplit = (ListView) view.findViewById(R.id.calculatedSplit);

        ArrayAdapter<String> adapterSplit = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_money_pot_row, R.id.rowEntry, calculatedPayments);
        listSplit.setAdapter(adapterSplit);
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

    //change the fragment to potEntry
    public void addEntry(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToPotEntry();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}


