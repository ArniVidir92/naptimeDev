package com.development.napptime.paydebt;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Alexander on 5.11.2014.
 */
public class ChosenDebt extends Fragment {

    private int cId=-1;

    private Button DeleteDebt = null;

    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    //Our layouts view
    private View view = null;

    //Our layouts list for debts
    ListView listView;

    //String, Integer and double for debt name, contact Id and amount
    String name;
    int contactId;
    double amount;

    //Database cursor
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_chosen_debt, container, false);

        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Adds a description about the contact from db
        setDescription();


        //Gets our button and sets a listener to catch when user clicks it
        DeleteDebt = (Button) view.findViewById(R.id.buttonDeleteDebt);
        DeleteDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDebt(v);
            }
        });

        //addDebtsForContactFromDb();

        return view;
    }



    public void setDescription(){
        TextView aboutC = (TextView) view.findViewById(R.id.descriptionDebt);
        aboutC.setVisibility(View.GONE);

        String description = "";
        String[] columns = {"description"};
        String where = "_contact_id = "+cId+" AND description is not NULL";
        cursor = db.query("DEBTS",columns,where,null,null,null,null);
        while(cursor.moveToNext()) {
            description = cursor.getString(0);
            Log.d("De", description);
            aboutC.setText(description);
            aboutC.setVisibility(View.VISIBLE);
        }
        cursor.close();


    }


    private void DeleteDebt(View v) {
    }

}
