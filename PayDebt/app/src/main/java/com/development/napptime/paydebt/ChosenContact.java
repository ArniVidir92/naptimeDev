package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by napptime on 10/11/14.
 *
 * ChosenContact class is a fragment that serves the purpose of displaying contact information and
 * an overview of all the money he ows you. Provides a nice user friendly layout.
 */
public class ChosenContact extends Fragment {

    //Instance variables

    //Button that swaps the current fragment for
    //the fragment that adds debts
    private Button addDebt = null;

    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    //Our layouts view
    private View view = null;

    //Our layouts list for debts
    ListView listView;

    //String and double for debt name and amount
    String name;
    double amount;

    //Database cursor
    Cursor cursor;

    //Initialize a list of strings
    List<String> listItemsName=new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_chosen_contact, container, false);

        //Gets our button and sets a listener to catch when user clicks it
        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebt(v);
            }
        });

        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();
        
        //Denotes the columns that we want to fetch from the database
        String[] columns = {"name", "amount"};
        cursor = db.query("DEBTS",columns,null,null,null,null,null);

        // Moves through each row of the db and adds the name
        // and amount of each debt to the listItemsName
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getDouble(1);

            listItemsName.add(name + ":   " + amount);
        }

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.lv_nonscroll_list);

        //Adapts the listItems to our list view using lay_chosen_contact_row
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_chosen_contact_row, R.id.chosenContactName, listItemsName);
        listView.setAdapter(adapter);

        return view;
    }

    // Adds a new debt to the chosen contact
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
