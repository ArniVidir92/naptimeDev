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
import android.widget.EditText;

/**
 * Created by napptime on 12/11/14.
 *
 * The EditDebt class serves the purpose of editing a debt in our sql database and
 * provides a user friendly form to do so.
 */

public class EditDebt extends Fragment{

    //Instance variables

    //id of the contact being changed
    private int dId = 1;

    //Button for editiing debt in sql database
    private Button confirm = null;

    //a editable text field for the phone number
    private EditText amount = null;

    //a editable text field for the description
    private EditText description = null;

    //a editable text field for the name
    private EditText name = null;

    //a editable text field for the due date
    private EditText due = null;

    //a editable text field for the date of debt
    private EditText date = null;

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
            this.dId = args.getInt("dId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_edit_debt, container, false);

        // Set a amount number listener to the amount number text edit
        amount = (EditText) view.findViewById(R.id.amount);

        name = (EditText) view.findViewById(R.id.name);

        description = (EditText) view.findViewById(R.id.description);

        due = (EditText) view.findViewById(R.id.due);

        date = (EditText) view.findViewById(R.id.date);

        amount.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //Gets our button
        confirm = (Button) view.findViewById(R.id.buttonConfirm);

        //Checks if the button exists
        if(confirm == null)
        {
            Log.d("debugCheck", "HeadFrag: sendButton is null");
            return view;
        }

        //Listener; catches when the user clicks the button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDebtInDB(v);
            }
        });

        String[] columns = {"amount","due","name", "description","date"};
        String where = "_debt_id = "+dId;
        //the select query for the database
        cursor = db.query("DEBTS",columns,where,null,null,null,null);
        String amount;
        while(cursor.moveToNext()) {
            amount = cursor.getString(0);
            this.amount.setText(amount);
            name.setText(cursor.getString(2));
            description.setText(cursor.getString(3));
            due.setText(cursor.getString(1));
            date.setText(cursor.getString(4));
        }
        //close the database commection
        cursor.close();

        return view;
    }

    //Converts string to double if it's not empty
    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    //Adds the info in the EditText field for inputting contact name
    //to our sql database
    public void editDebtInDB(View v){

        // Get text from phone number
        String debtAmount = amount.getText().toString();
        // Get text from name field
        EditText debtName = (EditText) view.findViewById(R.id.name);
        String name = debtName.getText().toString();
        // Get text from description field
        EditText debtDescription = (EditText) view.findViewById(R.id.description);
        String description = debtDescription.getText().toString();

        String debtDue = due.getText().toString();
        String debtDate = date.getText().toString();

        //cancel operation if debt has no name or amount and notifies the user
        if (amount.equals("") && name.equals(""))
        {
            ((MainActivity) getActivity()).toastIt("A debt needs amount and name.");
            return;
        }
        else if (debtAmount.equals("")||stringToDouble(debtAmount)==0)
        {
            ((MainActivity) getActivity()).toastIt("What happened to the amount?");
            return;
        }
        else if (name.equals(""))
        {
            ((MainActivity) getActivity()).toastIt("What happened to the name?");
            return;
        }

        // Initialize dbHelper and adds the contacts name to the database.
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("amount",debtAmount);
        contentValues.put("date",debtDate);
        contentValues.put("due",debtDue);
        //long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);
        db.update("DEBTS", contentValues, "_debt_id ="+this.dId, null);

        // Change to fragment Contacts
        getActivity().onBackPressed();
    }
}
