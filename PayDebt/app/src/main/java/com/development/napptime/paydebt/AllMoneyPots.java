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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;


/**
 * Created by napptime on 10/11/14.
 * Contact class serves the purpose of showing and managing contacts with a user friendly layout.
 */
public class AllMoneyPots extends Fragment {

    // DbHelper that serves as a helper for the class
    private DbHelper dbhelper;
    // A database that serves this class
    private SQLiteDatabase db;

    //Instance variables
    private View view = null;
    private ListView listView;
    private String name;
    private int id;
    private Cursor cursor;
    private ImageButton addPot = null;

    private List<String> listItems=new ArrayList<String>();
    private List<Integer> listIds = new ArrayList<Integer>();

    // Extra variables for the search box
    private List<String> listItemsCurrent=new ArrayList<String>();
    private List<Integer> listIdsCurrent = new ArrayList<Integer>();
    //The ArrayAdapter for the listView
    private ArrayAdapter<String> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_allpots, container, false);

        // Initialize as empty
        listItems=new ArrayList<String>();
        listIds = new ArrayList<Integer>();

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.pots_list);

        addPot = (ImageButton) view.findViewById(R.id.buttonAddPot);
        addPot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPot(v);
            }
        });

        if(listItems.isEmpty() ) {
            addToListView();
        }else{
            listView.setAdapter(adapter);
        }
        //Button listener for a button that sends the user to Chosen pot if clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("hallo",""+listItemsCurrent.get(position));
                ((MainActivity)getActivity()).changeFragmentToMoneyPot(listItemsCurrent.get(position), listIdsCurrent.get(position));
            }
        });

        ((MainActivity) getActivity()).setActionBarTitle("Money Pots");

        return view;
    }

    //add all contacts to the listview
    public void addToListView(){
        // Initialize the dbhelper
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"pot_name", "_pot_id"};

        // Selects the column name and puts the column in too cursor.
        cursor = db.query("ALLPOTS",columns,null,null,null,null,"pot_name");

        // Moves through each row of the db and adds
        // the name of each pot to the listItem
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            id = cursor.getInt(1);
            listItems.add(name);
            listIds.add(id);
        }

        listItemsCurrent.addAll(listItems);
        listIdsCurrent.addAll(listIds);

        //Adapts the listItems to our list view using lay_contacts_row
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_allmoneypots_row, R.id.listText, listItemsCurrent);
        listView.setAdapter(adapter);
    }

    public void newPot(View v) {

        EditText potName = (EditText) view.findViewById(R.id.newPot);
        String newPot = potName.getText().toString();

        //create the content values and insert it into the database
        ContentValues contentValues = new ContentValues();
        contentValues.put("pot_name",newPot);
        long id = db.insert("ALLPOTS",null,contentValues);

        addToListView();
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
