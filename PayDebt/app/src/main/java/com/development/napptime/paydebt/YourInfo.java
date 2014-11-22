package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
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
 * The AddContact class serves the purpose of adding a contact to our sql database and
 * provides a user friendly form to do so.
 */

public class YourInfo extends Fragment{

    //Instance variables

    //Button for adding contact to sql database
    private Button addYourInfo = null;

    //a editable text field for the phone number
    private EditText phoneNr = null;

    //Our layouts view
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_your_info, container, false);

        // Set a phone number listener to the phone number text edit
        phoneNr = (EditText) view.findViewById(R.id.inputYourPhoneNumber);

        phoneNr.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //Gets our button
        addYourInfo = (Button) view.findViewById(R.id.addInfo);

        //Checks if the button exists
        if(addYourInfo == null)
        {
            Log.d("debugCheck", "HeadFrag: sendButton is null");
            return view;
        }

        //Listener; catches when the user clicks the button
        addYourInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addYourInfoToDB(v);
            }
        });
        ((MainActivity) getActivity()).setActionBarTitle("Add Info");
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    //Adds the info in the EditText field for inputting contact name
    //to our sql database
    public void addYourInfoToDB(View v){
        // Get text from phone number
        String phone = phoneNr.getText().toString();
        // Get text from name field
        EditText contactName = (EditText) view.findViewById(R.id.inputYourName);
        String name = contactName.getText().toString();
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        // Get text from description field
        EditText yourDescription = (EditText) view.findViewById(R.id.yourTextDesc);
        String description = yourDescription.getText().toString();
        // Initialize dbHelper and adds the contacts name to the database.
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",0);
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("phone",phone);
        long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);

        ((MainActivity)getActivity()).changeFragmentToMyDebts();
    }
}
