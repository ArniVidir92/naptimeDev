package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by napptime on 12/11/14.
 * The AddContact class serves the purpose of adding a contact to our sql database and
 * provides a user friendly form to do so.
 */

public class AddContact extends Fragment{

    //Instance variables

    //Button for adding contact to sql database
    private Button addContact = null;

    //Our layouts view
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_addcontact, container, false);

        //Gets our button
        addContact = (Button) view.findViewById(R.id.addContactToDB);

        //Checks if the button exists
        if(addContact == null)
        {
            Log.d("debugCheck", "HeadFrag: sendButton is null");
            return view;
        }

        //Listener; catches when the user clicks the button
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToDB(v);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //Adds the info in the EditText field for inputting contact name
    //to our sql database
    public void addContactToDB(View v){
        // Get text from name field
        EditText contactName = (EditText) view.findViewById(R.id.inputName);
        String name = contactName.getText().toString();

        // Initialize dbHelper and adds the contacts name to the database.
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);
    }
}
