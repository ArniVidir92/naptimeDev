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
 * Created by napptime on 5.11.2014.
 *
 * the chosen entry pot serves the purpose of providing user with functionality to
 * see more details about a specific entry in the money pot as well as to delete it.
 */
public class ChosenPotEntry extends Fragment {

    //Instance variables

    //Id of the pot we are currently looking at
    private String pName = "";
    private int pId=-1;

    //Id of the entry we are currently looking at
    private int eId = -1;
    private String eName;

    private Button DeleteDebt = null;

    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    //Our layouts view
    private View view = null;

    String name;

    //Database cursor
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.pId = args.getInt("pId");
            this.eId = args.getInt("eId");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_chosen_pot_entry, container, false);

        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Adds a description about the contact from db
        setInfo();

        //Gets our button and sets a listener to catch when user clicks it
        DeleteDebt = (Button) view.findViewById(R.id.buttonDeleteDebt);
        DeleteDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePotEntry(v);
            }
        });

        // Change the title in the actionBar
        ((MainActivity) getActivity()).setActionBarTitle(eName);
        return view;
    }

    public void setInfo(){
        // Initializing variables
        pName = "";
        String eDescription = "", amount = "", date = "";

        //Get the contact name that paid this entry
        String[] columnsC = {"pot_name"};
        String where = "_pot_id = "+this.pId;
        cursor = db.query("AllPOTS",columnsC,where,null,null,null,null);
        while(cursor.moveToNext()){
            pName = cursor.getString(0);
        }
        cursor.close();

        // Getting the information for this chosen debt
        String[] columnsD = {"name", "description", "amount","date"};
        where = "_pot_entry = "+ this.eId;
        cursor = db.query("POTS",columnsD,where,null,null,null,null);
        while(cursor.moveToNext()) {
            eName = cursor.getString(0);
            eDescription = cursor.getString(1);
            amount = cursor.getString(2);
            date = cursor.getString(3);
        }
        cursor.close();

        //Variables for information about the debt we are currently looking at.
        TextView contact = (TextView) view.findViewById(R.id.ContactName);
        TextView entryAmount = (TextView) view.findViewById(R.id.DebtAmount);
        TextView entryDate = (TextView) view.findViewById(R.id.Date);
        TextView entryDateText = (TextView) view.findViewById(R.id.DateText);
        TextView entryDescription = (TextView) view.findViewById(R.id.Description);
        TextView entryDescriptionText = (TextView) view.findViewById(R.id.DescriptionText);

        TextView header = (TextView) view.findViewById(R.id.entryName);


        //Give the Textview variables their proper value for information.
        header.setText(pName);
        contact.setText(eName);
        entryAmount.setText(amount);
        entryDate.setText(date);
        entryDescription.setText(eDescription);

        // Hide date and due only show if they apply
        if( date.equals("") ){
            entryDate.setVisibility(View.GONE);
            entryDateText.setVisibility(View.GONE);
        }
        if( eDescription.equals("") ){
            entryDescription.setVisibility(View.GONE);
            entryDescriptionText.setVisibility(View.GONE);
        }

    }

    //Deletes the debt we are currently looking at
    private void deletePotEntry(View v) {
        //delete the debt from the database
        db.delete("POTS", "_pot_entry = " + eId, null);

        getActivity().onBackPressed();
    }

    //Edits the debt we are currently looking at

}
