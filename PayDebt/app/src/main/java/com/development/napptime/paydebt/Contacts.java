package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.app.LauncherActivity;
import android.content.Context;
import android.text.Editable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    // DbHelper that serves as a helper for the class
    private DbHelper dbhelper;
    // A database that serves this class
    private SQLiteDatabase db;
    private View view = null;
    private ListView listView;
    private String name;
    private int id;
    private Cursor cursor;
    private ImageButton addContact = null;
    private ImageButton favoritesButton = null;
    private EditText inputSearch;

    private List<String> listItems=new ArrayList<String>();
    private List<Integer> listIds = new ArrayList<Integer>();
    //The ArrayAdapter for the listView
    private ArrayAdapter<String> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_contacts, container, false);

        // Initialize as empty
        listItems=new ArrayList<String>();
        listIds = new ArrayList<Integer>();

        addContact = (ImageButton) view.findViewById(R.id.buttonAddContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact(v);
            }
        });

        favoritesButton = (ImageButton) view.findViewById(R.id.favoriteButton);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorites(v);
            }
        });


        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.contacts_list);

        if( listItems.isEmpty() ) {
            addToListView();
        }else{
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ((MainActivity)getActivity()).changeFragmentToChosenContact(listItems.get(position), listIds.get(position));
            }
        });

        return view;
    }

    public void addToListView(){
        // Initialize the dbhelper
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"name", "_contact_id"};

        // Selects the column name and puts the column in too cursor.
        cursor = db.query("CONTACTS",columns,null,null,null,null,"name");

        // Moves through each row of the db and adds
        // the name of each contact to the listItem
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            id = cursor.getInt(1);
            listItems.add(name);
            listIds.add(id);
        }

        //Adapts the listItems to our list view using lay_contacts_row
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_contacts_row, R.id.listText, listItems);
        listView.setAdapter(adapter);

        inputSearch = (EditText) view.findViewById(R.id.searchBox);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    public void addContact(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToAddContact();
    }

    public void Favorites(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToFavorites();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
