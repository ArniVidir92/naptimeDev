package com.development.napptime.paydebt;

import android.app.Fragment;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napptime on 10/11/14.
 *
 * My debts serves as a way of managing money that you owe to others and is in a way just a more
 * friendly way of adding yourself as a contact in our app.
 */
public class MyDebts extends Fragment{

    // The view we are in
    private View view;

    // Your name
    private String name;
    private boolean hasDebt = true;

    // The buttons and textViews from the layout
    private Button addDebt;
    private Button editC;
    private TextView phoneNr;
    private TextView phoneNrInput;
    private TextView descrInput;
    private TextView descr;
    private View descrDiv;
    private View phoneDiv;

    //Variables for our database
    private DbHelper dbhelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    // We set the contact Id of yourself as 0
    private final int cId = 0;

    //Our layouts list for debts
    private ListView listView;
    private ArrayAdapter<String> adapter;

    //Initialize a list of strings
    private List<String> listItemsName=new ArrayList<String>();
    private List<String> listDebtName = new ArrayList<String>();
    private List<Integer> listDebtIds=new ArrayList<Integer>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_mydebts, container, false);

        connectToLayout();

        //Initializes the database helper with the fragment's parent activity's context
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        //Adds a description about the contact from db
        setDescription();
        setPhone();

        // Initialize as empty
        listItemsName=new ArrayList<String>();
        listDebtName = new ArrayList<String>();
        listDebtIds=new ArrayList<Integer>();

        //Sets a listener to catch when user clicks it
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDebt(v);
            }
        });

        editC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContact(v);
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
                goToDebt(position);
            }
        });

        // Changing the action bar title
        ((MainActivity) getActivity()).setActionBarTitle("My Debts");

        return view;
    }

    private void connectToLayout(){
        addDebt = (Button) view.findViewById(R.id.myButtonAddDebt);
        editC = (Button) view.findViewById(R.id.myButtonEdit);
        phoneNr = (TextView) view.findViewById(R.id.myPhone);
        phoneNrInput = (TextView) view.findViewById(R.id.myPhoneNumber);
        phoneDiv = view.findViewById(R.id.phoneDiv);
        descrInput = (TextView) view.findViewById(R.id.myDescriptionContact);
        descr = (TextView) view.findViewById(R.id.myAboutContact);
        descrDiv = view.findViewById(R.id.descriptionDiv);
    }

    private void addDebt(View v){
        ((MainActivity)getActivity()).changeFragmentToAddDebt(this.cId);
    }

    //sets the description for the chosen context
    private void setDescription(){

        // sets the visibility of the fields
        descr.setVisibility(View.GONE);
        descrInput.setVisibility(View.GONE);
        descrDiv.setVisibility(View.GONE);


        String description;
        String[] columns = {"description"};
        String where = "_contact_id = "+this.cId+" AND description is not NULL";
        //the select query for the database
        cursor = db.query("CONTACTS",columns,where,null,null,null,null);
        while(cursor.moveToNext()) {
            description = cursor.getString(0);
            this.descrInput.setText(description);
            if( !description.equals("") ){
                this.descrInput.setVisibility(View.VISIBLE);
                this.descr.setVisibility(View.VISIBLE);
                this.descrDiv.setVisibility(View.VISIBLE);
            }
        }
        //close the database connection
        cursor.close();
    }

    //updates the phone field for the chosen contact
    private void setPhone(){


        //set the visibility of the text fields
        phoneNr.setVisibility(View.GONE);
        phoneNrInput.setVisibility(View.GONE);
        phoneDiv.setVisibility(View.GONE);

        String phoneNumber = "";
        String[] columns = {"phone"};
        String where = "_contact_id = "+this.cId+" AND phone is not NULL";
        //the select query for the database
        cursor = db.query("CONTACTS",columns,where,null,null,null,null);
        while(cursor.moveToNext()) {
            phoneNumber = cursor.getString(0);
            phoneNrInput.setText(phoneNumber);
            if( !phoneNumber.equals("") ){
                phoneNr.setVisibility(View.VISIBLE);
                phoneNrInput.setVisibility(View.VISIBLE);
                phoneDiv.setVisibility(View.VISIBLE);
            }
        }
        cursor.close();
    }

    private void addDebtsForContactFromDb(){

        int debtId;
        double amount;

        //Denotes the columns that we want to fetch from the database
        String[] columns = {"_debt_id","name", "amount"};
        String where = "_contact_id = "+this.cId;
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

        if(listItemsName.isEmpty())
        {
            listItemsName.add("Congratulations you have no debts!");
            hasDebt=false;
        }
        else{hasDebt=true;}


        listView.setAdapter(adapter);
    }

    //Edits the contact we are currently looking at
    private void EditContact(View v) {
        ((MainActivity)getActivity()).changeFragmentToEditContact(this.cId);
    }

    private void goToDebt(int position)
    {
        if(hasDebt)
            ((MainActivity)getActivity()).changeFragmentToChosenDebt(listDebtName.get(position), listDebtIds.get(position),cId);
    }
}
