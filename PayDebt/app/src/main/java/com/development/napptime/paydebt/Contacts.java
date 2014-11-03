package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.app.LauncherActivity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;


/**
 * Created by napptime on 10/11/14.
 * Contact class serves the purpose of showing and managing contacts with a user friendly layout.
 */
public class Contacts extends Fragment {

    // dbhelper that serves as a helper for the class
    DbHelper dbhelper;
    // A database that serves this class
    SQLiteDatabase db;
    private View view = null;
    ListView listView;
    String name;
    Cursor cursor;

    List<String> listItems=new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_contacts, container, false);

        // Initialize the dbhelper
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"name"};

        // Selects the column name and puts the column in too cursor.
        cursor = db.query("CONTACTS",columns,null,null,null,null,"name");

        // Moves through each row of the db and adds
        // the name of each contact to the listItem
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            listItems.add(name);
        }

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.contacts_list);
        //Adapts the listItems to our list view using lay_contacts_row
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_contacts_row, R.id.listText, listItems);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // String item = ((TextView)view).getText().toString();
                ((MainActivity)getActivity()).changeFragmentToChosenContact(position, listItems.get(position));
                /*String item = listItems.get(position);
                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();*/

            }
        });

        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
