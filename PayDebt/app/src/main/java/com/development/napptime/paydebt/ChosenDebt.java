package com.development.napptime.paydebt;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by napptime on 5.11.2014.
 *
 * the chosen debts class serves the purpose of showing and working around a
 * single selected debt from a selected user.
 */
public class ChosenDebt extends Fragment {

    //Instance variables

    //Id of the contact we are currently looking at
    private String cName = "";
    private int cId=-1;

    //Id of the debt we are currently looking at
    private int dId = -1;
    private String dName;

    private Button DeleteDebt = null;
    private Button EditDebt = null;

    //Variables for our database
    DbHelper dbhelper;
    SQLiteDatabase db;

    //Our layouts view
    private View view = null;

    //String, Integer and double for debt name, contact Id and amount
    String name;

    //Database cursor
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            this.cId = args.getInt("cId");
            this.dId = args.getInt("dId");
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
        setInfo();

        // Change the title in the actionBar
        ((MainActivity) getActivity()).setActionBarTitle(dName);
        return view;
    }

    public void createListeners()
    {
        //Gets our button and sets a listener to catch when user clicks it
        DeleteDebt = (Button) view.findViewById(R.id.buttonDeleteDebt);
        DeleteDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDebt(v);
            }
        });

        EditDebt = (Button) view.findViewById(R.id.buttonEditDebt);
        EditDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDebt(v);
            }
        });
    }

    public void setInfo(){
        // Initializing variables
        dName = "";
        String dDescription = "", amount = "", date = "", due = "";

        // Get the contact name that owes you this debt
        String[] columnsC = {"name"};
        String where = "_contact_id = "+this.cId;
        cursor = db.query("CONTACTS",columnsC,where,null,null,null,null);
        while(cursor.moveToNext()){
            cName = cursor.getString(0);
        }
        cursor.close();

        // Getting the information for this chosen debt
        String[] columnsD = {"name", "description", "amount","date","due"};
        where = "_debt_id = "+ this.dId;
        cursor = db.query("DEBTS",columnsD,where,null,null,null,null);
        while(cursor.moveToNext()) {
            dName = cursor.getString(0);
            dDescription = cursor.getString(1);
            amount = cursor.getString(2);
            date = cursor.getString(3);
            due = cursor.getString(4);
        }
        cursor.close();

        //Variables for information about the debt we are currently looking at.
        TextView contact = (TextView) view.findViewById(R.id.ContactName);
        TextView debtName = (TextView) view.findViewById(R.id.DebtName);
        TextView debtAmount = (TextView) view.findViewById(R.id.DebtAmount);
        TextView debtDate = (TextView) view.findViewById(R.id.Date);
        TextView debtDateText = (TextView) view.findViewById(R.id.DateText);
        TextView debtDue = (TextView) view.findViewById(R.id.Due);
        TextView debtDueText = (TextView) view.findViewById(R.id.DueText);
        TextView debtDescription = (TextView) view.findViewById(R.id.Description);
        TextView debtDescriptionText = (TextView) view.findViewById(R.id.DescriptionText);


        //Give the Textview variables their proper value for information.
        contact.setText(cName);
        debtName.setText(dName);
        debtAmount.setText(amount);
        debtDate.setText(date);
        debtDue.setText(due);
        debtDescription.setText(dDescription);

        // Hide date and due only show if they apply
        if( date.equals("") ){
            debtDate.setVisibility(View.GONE);
            debtDateText.setVisibility(View.GONE);
        }
        if( due.equals("") ){
            debtDue.setVisibility(View.GONE);
            debtDueText.setVisibility(View.GONE);
        }
        if( dDescription.equals("") ){
            debtDescription.setVisibility(View.GONE);
            debtDescriptionText.setVisibility(View.GONE);
        }

    }

    //Deletes the debt we are currently looking at
    private void DeleteDebt(View v) {
        //delete the debt from the database
        db.delete("DEBTS", "_debt_id = " + dId, null);

        getActivity().onBackPressed();
    }

    //Edits the debt we are currently looking at
    private void EditDebt(View v) {
        ((MainActivity)getActivity()).changeFragmentToEditDebt(this.dId);
    }

}
