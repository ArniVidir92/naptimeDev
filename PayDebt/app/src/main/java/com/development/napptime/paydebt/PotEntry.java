package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by napptime on 30.10.2014.
 *
 * This class serves the purpose of adding a new entry to an already existing money pot with
 * all the required information and providing a user friendly form to do so
 */
public class PotEntry extends Fragment {

    //Define variables for the various widgets of the layout
    private View view = null;
    private Button addEntry = null;
    private List<Integer> listIds = new ArrayList<Integer>();
    private ArrayList<String> list=new ArrayList<String>();
    //various other instance variables
    String name;
    Integer id;

    Integer numOfContacts;
    Integer totalAmount;
    Integer entryAmount;

    private String dateOrDue;

    Cursor cursor;
    Cursor cursorFA;

    Spinner spinner;

    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    //Variable for our calendar
    final Calendar c = Calendar.getInstance();

    // Contact name
    String pName = "";

    //Contact Id
    private int pId = -1;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.pId = args.getInt("pId");
            this.pName = args.getString("pName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view
        this.view = inflater.inflate(R.layout.lay_moneypot, container, false);

        //gets the entry button from the layout and connects it's functionality
        // to the on click listener
        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntryToPotDatabase(v);
            }
        });

        EditText dateET = (EditText) view.findViewById(R.id.editPotEntryDate);
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDate(v);
            }
        });

        //initialize the database helper
        dbHelper = new DbHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        //define what we need
        String[] columns = {"name", "_contact_id"};
        String[] amounts = {"amount"};

        // Selects the column name and puts the column in too cursor.
        cursor = sqLiteDatabase.query("CONTACTS",columns,null,null,null,null,"name");
        cursorFA = sqLiteDatabase.query("POTS",amounts,null,null,null,null,null);

        //get the spinner from the layout
        spinner = (Spinner) view.findViewById(R.id.contactsSpinner);

        //define the hash and set the number of contacts as 0 to begin with
        numOfContacts = 0;
        HashSet test = new HashSet();

        //go through all contacts
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            id = cursor.getInt(1);
            list.add(name);
            listIds.add(id);

            test.add(name);
        }

        totalAmount = 0;

        // count
        while(cursorFA.moveToNext()) {
            entryAmount = cursorFA.getInt(0);
            totalAmount +=entryAmount;
        }

        //create an adapter and use it to move our list into the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        ((MainActivity) getActivity()).setActionBarTitle(pName);

        return view;
    }

    //converts a string to double and returns -1 if it's the empty string
    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }

    //add the entry we created to the database
    public void addEntryToPotDatabase(View v){
        // Get text from name field
        // Get amount from EditText box
        EditText amountET = (EditText) view.findViewById(R.id.inputAmount);
        String amount = amountET.getText().toString();

        // Get text from description field
        EditText descriptionET = (EditText) view.findViewById(R.id.description);
        String description = descriptionET.getText().toString();

        double entryAmount = stringToDouble(amount);

        //Initialize DbHelper and creates a sql database object and puts it into the
        //database
        dbHelper = new DbHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        //get the info from the spinner
        String nameTest = spinner.getSelectedItem().toString();

        EditText dateET = (EditText) view.findViewById(R.id.editPotEntryDate);
        String date = dateET.getText().toString();

        //cancel operation if debt has no name or amount and notifies the user
        if (amount.equals("") || entryAmount==0)
        {
            ((MainActivity) getActivity()).toastIt("You need to specify an amount.");
            return;
        }

        //create the content values and insert it into the database
        ContentValues contentValues = new ContentValues();
        contentValues.put("_pot_id", this.pId );
        contentValues.put("date", date);
        contentValues.put("name",nameTest);
        contentValues.put("description",description);
        if(entryAmount != -1){contentValues.put("amount", entryAmount);}
        long id = sqLiteDatabase.insert("POTS",null,contentValues);

        // Change to fragment MoneyPot
        getActivity().onBackPressed();
    }

    //Set the selected date
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCurrentDateOnViewDate();
        }
    };

    //Sets our dateOrDue variable to the corresponding field
    public void clickDate(View view){
        dateOrDue = getString(R.string.AddDebt_date);
        new DatePickerDialog( getActivity(), date,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    //Fetches the datePicker value and puts it into the corresponding field
    public void setCurrentDateOnViewDate(){
        String dateFormat = getString(R.string.AddDebt_dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.UK);

        EditText dateET = (EditText) view.findViewById(R.id.editPotEntryDate);
        dateET.setText(sdf.format(c.getTime()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}
