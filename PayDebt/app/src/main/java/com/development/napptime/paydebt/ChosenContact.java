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

    // Id of the contact we are currently looking at
    private int cId=-1;

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

    //String, Integer and double for debt name, contact Id and amount
    String name;
    int contactId;
    double amount;

    //Database cursor
    Cursor cursor;

    //Initialize a list of strings
    List<String> listItemsName=new ArrayList<String>();


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId")+1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //this.cId = this.getArguments().getInt("cId");

        Log.d("cId",""+cId);

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

        addDebtsForContactFromDb();

        return view;
    }

    public void addDebtsForContactFromDb(){
        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"_contact_id", "name", "amount"};
        cursor = db.query("DEBTS",columns,null,null,null,null,null);

        // Moves through each row of the db and adds the name and amount of each debt to the listItemsName
        while(cursor.moveToNext()) {
            contactId = cursor.getInt(0);
            name = cursor.getString(1);
            amount = cursor.getDouble(2);
            if(cId == contactId){
                listItemsName.add(name + ":   " + amount);
            }
        }

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.lv_nonscroll_list);

        //Adapts the listItems to our list view using lay_chosen_contact_row
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.lay_chosen_contact_row, R.id.chosenContactName, listItemsName);
        listView.setAdapter(adapter);
    }

    // Adds a new debt to the chosen contact
    public void addDebt(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToAddDebt(this.cId);
        Log.d("Villa","Hallodrasl");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



}
