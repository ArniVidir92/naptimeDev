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
 * Created by snorri on 15/11/14.
 */

public class AddContact extends Fragment{

    private Button addContact = null;
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.lay_addcontact, container, false);

        addContact = (Button) view.findViewById(R.id.addContactToDB);

        if(addContact == null)
        {
            Log.d("debugCheck", "HeadFrag: sendButton is null");
            return view;
        }
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

    public void addContactToDB(View v){
        // Get text from name field
        EditText contactName = (EditText) view.findViewById(R.id.inputName);
        String name = contactName.getText().toString();

        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);
    }
}
