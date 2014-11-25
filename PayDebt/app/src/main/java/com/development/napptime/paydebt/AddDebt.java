package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by napptime on 10/11/14.
 *
 * AddDebt class serves the purpose of adding a debt (and its info) to the sql database and
 * provides a user friendly form to do so.
 */



public class AddDebt extends Fragment{

    //Instance variables

    // Contact name
    private String cName = "";

    //Button, adds debts to database when used
    private Button addDebt = null;

    //Our layouts view
    private View view = null;

    //Variable for which datePicker is used
    private String dateOrDue;

    //Variable for our calendar
    final Calendar c = Calendar.getInstance();

    //Contact Id
    private int cId = -1;

    //Overrides the onCreate of parent
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId");
            this.cName = args.getString("cName");
        }
    }

    //Overrides the OnCreateView of parent
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.fragment_add_debt, container, false);

        //Gets our button and the date and due fields
        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);
        EditText dateET = (EditText) view.findViewById(R.id.editTextDate);
        EditText dueET = (EditText) view.findViewById(R.id.editTextDue);

        //Listener; catches when the user clicks the button
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebtToDatabase(v);
            }
        });

        //Listener; catches when the user clicks the field
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDate(v);
            }
        });

        //Listener; catches when the user clicks the field
        dueET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDueDate(v);
            }
        });

        // Changing the actionBarTitle
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.adddebt));


        return view;
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

    //Sets our dateOrDue variable to the corresponding field
    public void clickDueDate(View view){
        dateOrDue = getString(R.string.AddDebt_Due);
        new DatePickerDialog( getActivity(), date,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    //Fetches the datePicker value and puts it into the corresponding field
    public void setCurrentDateOnViewDate(){
        String dateFormat = getString(R.string.AddDebt_dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.UK);

        EditText dueET = (EditText) view.findViewById(R.id.editTextDue);
        EditText dateET = (EditText) view.findViewById(R.id.editTextDate);
        if(dateOrDue.equals(getString(R.string.AddDebt_date))){
            dateET.setText(sdf.format(c.getTime()));
        }
        else if(dateOrDue.equals(getString(R.string.AddDebt_Due))){
            dueET.setText(sdf.format(c.getTime()));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //Converts date to integer if it's not empty
    public static int dateToInt(String date){
        if(date.equals(""))
            return -1;
        String dayStr = date.substring(0,2);
        String monthStr = date.substring(3,5);
        String yearStr = date.substring(6,10);

        return Integer.parseInt(dayStr + monthStr + yearStr);
    }

    //Converts string to double if it's not empty
    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }


    //Adds debt information to the database
    public void addDebtToDatabase(View v){
        // Get text from name field
        EditText nameET = (EditText) view.findViewById(R.id.editName);
        String name = nameET.getText().toString();
        // Get amount from EditText box
        EditText amountET = (EditText) view.findViewById(R.id.editTextAmount);
        String amount = amountET.getText().toString();
        // Get text from date field
        EditText dateET = (EditText) view.findViewById(R.id.editTextDate);
        String date = dateET.getText().toString();
        // Get text from due field
        EditText dueET = (EditText) view.findViewById(R.id.editTextDue);
        String due = dueET.getText().toString();
        // Get text from description field
        EditText descriptionET = (EditText) view.findViewById(R.id.editTextDesc);
        String description = descriptionET.getText().toString();
        // Find the reminder checkbox
        CheckBox reminderCB = (CheckBox) view.findViewById(R.id.checkBoxReminder);

        double dbAmount = stringToDouble(amount);

        //Sets our reminder to 1, or 0 depending on context
        int reminder;
        if(reminderCB.isChecked()){
            reminder = 1;
        }
        else{
            reminder = 0;
        }

        //cancel operation if debt has no name or amount and notifies the user
        if (amount.equals("") && name.equals(""))
        {
            ((MainActivity) getActivity()).toastIt("A debt needs amount and name.");
            return;
        }
        else if (amount.equals("")||dbAmount==0)
        {
            ((MainActivity) getActivity()).toastIt("You need to specify an amount.");
            return;
        }
        else if (name.equals(""))
        {
            ((MainActivity) getActivity()).toastIt("You can't add a nameless debt.");
            return;
        }

        //Initialize DbHelper and creates a sql database object and puts it into the
        //database
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",this.cId);
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("date",date);
        contentValues.put("due",due);
        if(dbAmount != -1){contentValues.put("amount", dbAmount);}
        contentValues.put("reminder", reminder);
        long id = sqLiteDatabase.insert("DEBTS",null,contentValues);

        getActivity().onBackPressed();
    }

}
