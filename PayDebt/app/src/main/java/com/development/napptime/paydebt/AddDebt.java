package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by arni on 10/11/14.
 */
public class AddDebt extends Fragment{

    private Button button;


    private Button addDebt = null;
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_add_debt, container, false);

        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);

        if(addDebt == null)
        {
            Log.d("debugCheck", "HeadFrag: sendButton is null");
            return view;
        }
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebtToDatabase(v);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void addDebtToDatabase(View v){
        // Get text from name field
        EditText nameET = (EditText) view.findViewById(R.id.editName);
        String name = nameET.getText().toString();
        // Get amount from EditText box
        EditText amountET = (EditText) view.findViewById(R.id.editTextAmount);
        double amount = Double.parseDouble(amountET.getText().toString());
        // Get text from date field
        EditText dateET = (EditText) view.findViewById(R.id.editTextDate);
        int date = Integer.parseInt(dateET.getText().toString());
        // Get text from due field
        EditText dueET = (EditText) view.findViewById(R.id.editTextDue);
        int due = Integer.parseInt(dueET.getText().toString());
        // Get text from descr field
        EditText descriptionET = (EditText) view.findViewById(R.id.editTextDesc);
        String description = descriptionET.getText().toString();
        // Find the reminder checkbox
        CheckBox reminderCB = (CheckBox) view.findViewById(R.id.checkBoxReminder);
        int reminder;
        if(reminderCB.isChecked()){
            reminder = 1;
        }
        else{
            reminder = 0;
        }

        DbHelper dbHelper = new DbHelper(getActivity());
        Log.d("Database", "bla villa");
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_contact_id",1);
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("date",date);
        contentValues.put("due",due);
        contentValues.put("amount", amount);
        contentValues.put("reminder", reminder);
        long id = sqLiteDatabase.insert("DEBTS",null,contentValues);
        Log.d("Database", "add database" + id);
    }
}
