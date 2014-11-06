package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by napptime on 10/11/14.
 *
 * ChosenContact class is a fragment that serves the purpose of displaying contact information and
 * an overview of all the money he ows you. Provides a nice user friendly layout.
 */
public class ChosenContact extends Fragment {

    //Instance variables

    // Id of the contact we are currently looking at
    private int cId = -1;
    private String cName = "";

    //Button that swaps the current fragment for
    //the fragment that adds debts
    private Button addDebt = null;

    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    //Our layouts view
    private View view = null;

    //Our layouts list for debts
    private ListView listView;
    private ArrayAdapter<String> adapter;

    //String, Integer and double for debt name, contact Id and amount
    String name;
    private int debtId;
    double amount;

    //Database cursor
    Cursor cursor;

    //Initialize a list of strings
    List<String> listItemsName=new ArrayList<String>();
    List<String> listDebtName = new ArrayList<String>();
    List<Integer> listDebtIds=new ArrayList<Integer>();


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId");
            this.cName = args.getString("cName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_chosen_contact, container, false);

        // Initialize as empty
        listItemsName=new ArrayList<String>();
        listDebtName = new ArrayList<String>();
        listDebtIds=new ArrayList<Integer>();

        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Adds a description about the contact from db
        setDescription();
        setPhone();

        //Gets our button and sets a listener to catch when user clicks it
        addDebt = (Button) view.findViewById(R.id.buttonAddDebt);
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebt(v);
            }
        });

        //Gets the list view from the layout
        listView = (ListView) view.findViewById(R.id.lv_nonscroll_list);
        //Adapts the listItems to our list view using lay_chosen_contact_row
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_chosen_contact_row, R.id.chosenContactName, listItemsName);

        if( listItemsName.isEmpty() ) {
            addDebtsForContactFromDb();
        }else{
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ((MainActivity)getActivity()).changeFragmentToChosenDebt(listDebtName.get(position), listDebtIds.get(position),cId);
            }
        });

        return view;
    }

    public void addDebtsForContactFromDb(){

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"_debt_id","name", "amount"};
        String where = "_contact_id = "+cId;
        cursor = db.query("DEBTS",columns,where,null,null,null,null);

        // Moves through each row of the db and adds the name
        // and amount of each debt to the listItemsName
        while(cursor.moveToNext()) {
            debtId = cursor.getInt(0);
            name = cursor.getString(1);
            amount = cursor.getDouble(2);
            listItemsName.add(name + ":   " + amount);
            listDebtName.add(name);
            listDebtIds.add(debtId);
        }
        cursor.close();

        listView.setAdapter(adapter);
    }

    public void setDescription(){

        TextView descrTV = (TextView) view.findViewById(R.id.descriptionContact);
        TextView aboutC = (TextView) view.findViewById(R.id.aboutContact);

        aboutC.setVisibility(View.GONE);
        descrTV.setVisibility(View.GONE);

        String description = "";
        String[] columns = {"description"};
        String where = "_contact_id = "+cId+" AND description is not NULL";
        cursor = db.query("CONTACTS",columns,where,null,null,null,null);
        while(cursor.moveToNext()) {
            description = cursor.getString(0);
            Log.d("De", description);
            descrTV.setText(description);
            if( !description.equals("") ){
                descrTV.setVisibility(View.VISIBLE);
                aboutC.setVisibility(View.VISIBLE);
            }
        }
        cursor.close();
    }

    public void setPhone(){
        TextView phone = (TextView) view.findViewById(R.id.contactPhone);
        TextView number = (TextView) view.findViewById(R.id.phoneNumber);

        phone.setVisibility(View.GONE);
        number.setVisibility(View.GONE);

        String phoneNumber = "";
        String[] columns = {"phone"};
        String where = "_contact_id = "+cId+" AND phone is not NULL";
        cursor = db.query("CONTACTS",columns,where,null,null,null,null);
        while(cursor.moveToNext()) {
            phoneNumber = cursor.getString(0);
            Log.d("De", phoneNumber);
            number.setText(phoneNumber);
            if( !phoneNumber.equals("") ){
                phone.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
        }
        cursor.close();
    }

    // Adds a new debt to the chosen contact
    public void addDebt(View v)
    {
        ((MainActivity)getActivity()).changeFragmentToAddDebt(this.cId);
        Log.d("Villa","Hallodrasl");
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
