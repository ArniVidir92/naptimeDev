package com.development.napptime.paydebt;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napptime on 10/11/14.
 *
 */
public class Favorites extends Fragment {

    // dbhelper that serves as a helper for the class
    DbHelper dbhelper;
    // A database that serves this class
    SQLiteDatabase db;

    //Initialize variables

    private View view = null;
    ListView listView;
    String name;
    int id;
    Cursor cursor;

    //initialize array lists
    List<String> listItems = new ArrayList<String>();
    List<Integer> listIds = new ArrayList<Integer>();

    //The ArrayAdapter for the listView
    private ArrayAdapter<String> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_favorites, container, false);

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.favorite_list);

        if( listItems.isEmpty() ) {
            addToListView();
        }else{
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // String item = ((TextView)view).getText().toString();
                ((MainActivity)getActivity()).changeFragmentToChosenContact( listItems.get(position), listIds.get(position) );
                /*String item = listItems.get(position);
                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();*/

            }
        });
        
        return view;
    }

    //Add all favorite contacts to the listview
    public void addToListView(){
        // Initialize the dbhelper
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"name", "_contact_id"};

        // Selects the column name and puts the column in too cursor.
        cursor = db.query("CONTACTS", columns, "favorite = 1", null, null, null, "name");

        // Moves through each row of the db and adds the name of each contact to the listItem
        while (cursor.moveToNext()) {
            name = cursor.getString(0);
            id = cursor.getInt(1);
            listItems.add(name);
            listIds.add(id);
        }

        //Adapts the listitems to our list view using lay_contacts_row
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.lay_contacts_row, R.id.listText, listItems);

        listView.setAdapter(adapter);
    }
}
