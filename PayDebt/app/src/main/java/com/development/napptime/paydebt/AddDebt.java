package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by arni on 10/11/14.
 */
public class AddDebt extends Fragment{


    private Button addDebt = null;
    private View view = null;

    private String dateOrDue;

    final Calendar c = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_add_debt, container, false);

        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);
        EditText dateET = (EditText) view.findViewById(R.id.editTextDate);
        EditText dueET = (EditText) view.findViewById(R.id.editTextDue);

        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebtToDatabase(v);
            }
        });
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDate(v);
            }
        });

        dueET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDueDate(v);
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCurrentDateOnViewDate();
        }
    };

    public void clickDate(View view){
        dateOrDue = getString(R.string.AddDebt_date);
        new DatePickerDialog( getActivity(), date, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void clickDueDate(View view){
        dateOrDue = getString(R.string.AddDebt_Due);
        new DatePickerDialog( getActivity(), date, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

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

    public static int dateToInt(String date){
        if(date.equals(""))
            return -1;
        String dayStr = date.substring(0,2);
        String monthStr = date.substring(3,5);
        String yearStr = date.substring(6,10);

        return Integer.parseInt(dayStr + monthStr + yearStr);
    }

    public static double stringToDouble(String str){
        if(str.equals(""))
            return -1;

        return Double.parseDouble(str);
    }

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
        int dbDate = dateToInt(date);
        int dbDue = dateToInt(due);



        int reminder;
        if(reminderCB.isChecked()){
            reminder = 1;
        }
        else{
            reminder = 0;
        }

        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",1);
        contentValues.put("name",name);
        contentValues.put("description",description);
        if(dbDate != -1){contentValues.put("date",dbDate);}
        if(dbDue != -1){contentValues.put("due",dbDue);}
        if(dbAmount != -1){contentValues.put("amount", dbAmount);}
        contentValues.put("reminder", reminder);
    }
}
