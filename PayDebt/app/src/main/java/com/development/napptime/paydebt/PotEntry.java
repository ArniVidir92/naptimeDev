package com.development.napptime.paydebt;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Snorri on 30.10.2014.
 */
public class PotEntry extends Fragment {

    private View view = null;
    private Button addEntry = null;
    private List<Integer> listIds = new ArrayList<Integer>();
    private ArrayList<String> list=new ArrayList<String>();
    String name;
    Integer id;

    Integer numOfContacts;
    Integer totalAmount;
    Integer entryAmount;

    Cursor cursor;
    Cursor cursorFA;

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

        String[] columns = {"name", "_contact_id"};
        String[] amounts = {"amount"};

        // Selects the column name and puts the column in too cursor.
        cursor = sqLiteDatabase.query("CONTACTS",columns,null,null,null,null,"name");
        cursorFA = sqLiteDatabase.query("POTS",amounts,null,null,null,null,null);

        spinner = (Spinner) view.findViewById(R.id.contactsSpinner);

        numOfContacts = 0;
        HashSet test = new HashSet();

        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            id = cursor.getInt(1);
            list.add(name);
            listIds.add(id);

            test.add(name);
        }
        Integer blaff = test.size();
        String bla= blaff.toString();

        Toast.makeText(getActivity(), bla,
                Toast.LENGTH_SHORT).show();


        totalAmount = 0;

        while(cursorFA.moveToNext()) {
            entryAmount = cursorFA.getInt(0);
            totalAmount +=entryAmount;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        return view;
    }

    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }

    public void addEntryToPotDatabase(View v){
        // Get text from name field
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

        String nameTest = spinner.getSelectedItem().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",1);
        contentValues.put("name",nameTest);
        contentValues.put("description",description);
        if(entryAmount != -1){contentValues.put("amount", entryAmount);}
        long id = sqLiteDatabase.insert("POTS",null,contentValues);
    }
}
