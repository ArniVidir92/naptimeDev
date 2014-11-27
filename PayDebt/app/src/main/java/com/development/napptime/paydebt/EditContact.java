package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by napptime on 12/11/14.
 *
 * The Edit contact class serves the purpose of editing a contact in our sql database and
 * provides a user friendly form to do so.
 */

public class EditContact extends Fragment{

    //Instance variables

    //id of the contact being changed
    private int cId = 1;

    //Button for adding contact to sql database
    private Button confirm = null;

    //a editable text field for the phone number
    private EditText phoneNr = null;

    //a editable text field for the phone number
    private EditText description = null;

    //a editable text field for the phone number
    private EditText name = null;

    // a variable to contains whether contact should be favorite
    private int favoriteCheck = 0;
    //Our layouts view
    private View view = null;

    //Database cursor
    Cursor cursor;
    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting the sent variable
        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_edit_contact, container, false);

        connectToLayout();

        phoneNr.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //Listener; catches when the user clicks the button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContactInDB(v);
            }
        });

        populateLayoutsInformation();

        return view;
    }

    private void connectToLayout()
    {
        // Set a phone number listener to the phone number text edit
        phoneNr = (EditText) view.findViewById(R.id.phoneNumber);

        name = (EditText) view.findViewById(R.id.name);

        description = (EditText) view.findViewById(R.id.description);

        //Gets our button
        confirm = (Button) view.findViewById(R.id.buttonConfirm);
    }

    private void populateLayoutsInformation()
    {
        String[] columns = {"phone","favorite","name", "description"};
        String where = "_contact_id = "+cId;
        //the select query for the database
        cursor = db.query("CONTACTS",columns,where,null,null,null,null);
        String phone;
        while(cursor.moveToNext()) {
            phone = cursor.getString(0);
            phoneNr.setText(phone);

            if(cursor.getInt(1)==1)
            {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxFavorite);
                checkBox.setChecked(true);
            }
            name.setText(cursor.getString(2));
            description.setText(cursor.getString(3));
        }
        //close the database commection
        cursor.close();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxFavorite);
        if (checkBox.isChecked()) {
            favoriteCheck = 1;
        }
    }

    //Adds the info in the EditText field for inputting contact name
    //to our sql database
    public void editContactInDB(View v){

        // Get text from phone number
        String phone = phoneNr.getText().toString();
        // Get text from name field
        EditText contactName = (EditText) view.findViewById(R.id.name);
        String name = contactName.getText().toString();

        //cancel operation if contact has no name or amount and notifies the user
        if(name.equals(""))
        {
            ((MainActivity) getActivity()).toastIt("He had a name just a moment ago.");
            return;
        }

        name = name.substring(0,1).toUpperCase() + name.substring(1);
        // Get text from description field
        EditText contactDescription = (EditText) view.findViewById(R.id.description);
        String description = contactDescription.getText().toString();
        onCheckboxClicked(view);


        // Initialize dbHelper and adds the contacts name to the database.
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("phone",phone);
        contentValues.put("favorite", favoriteCheck);
        //long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);
        db.update("CONTACTS", contentValues, "_contact_id ="+this.cId, null);

        // Change to fragment Contacts
        getActivity().onBackPressed();
    }
}
