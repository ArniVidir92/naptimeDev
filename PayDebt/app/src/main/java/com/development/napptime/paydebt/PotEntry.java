package com.development.napptime.paydebt;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snorri on 30.10.2014.
 */
public class PotEntry extends Fragment {

    private View view = null;
    private Button addEntry = null;

    Cursor cursor;

    Spinner spinner;

    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_moneypot, container, false);

        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntryToPotDatabase(v);
            }
        });

        dbHelper = new DbHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        String[] columns = {"name"};
        spinner = (Spinner) view.findViewById(R.id.contactsSpinner);

        cursor = sqLiteDatabase.query("CONTACTS",columns,null,null,null,null,null);

        int i=0;
        for (cursor.moveToFirst(); i < cursor.getCount(); i++) {
            List list = new ArrayList();
            
            cursor.moveToNext();
        }

        return view;
    }

    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }

    public void addEntryToPotDatabase(View v){
        // Get text from name field
        EditText nameET = (EditText) view.findViewById(R.id.inputName);
        String name = nameET.getText().toString();
        // Get amount from EditText box
        EditText amountET = (EditText) view.findViewById(R.id.inputAmount);
        String amount = amountET.getText().toString();

        // Get text from description field
        EditText descriptionET = (EditText) view.findViewById(R.id.inputDesc);
        String description = descriptionET.getText().toString();

        double entryAmount = stringToDouble(amount);

        //Initialize DbHelper and creates a sql database object and puts it into the
        //database
        dbHelper = new DbHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",1);
        contentValues.put("name",name);
        contentValues.put("description",description);
        if(entryAmount != -1){contentValues.put("amount", entryAmount);}
        long id = sqLiteDatabase.insert("POTS",null,contentValues);
    }
}
