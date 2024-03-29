package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by napptime on 10/11/14.
 *
 * The money pot class serves the purpose of creating and working with
 * the moneypot feature. The moneypot feature allows users to divide the debts from a selection
 * of payments paid by separate people.
 */
public class MoneyPot extends Fragment {

    //Initialize variables
    private View view = null;
    private Button addEntry = null;
    private Button deletePot = null;

    private TextView showTotalAmount;
    private TextView showSplit;
    
    //initialize variables for the database
    DbHelper dbhelper;
    SQLiteDatabase db;
    ContentValues contentvalues;
    Cursor cursor;

    ListView listView;
    ListView listSplit;

    //Initalize variables for later calculations
    Integer total_amount;
    Integer numOfContacts;

    String[][] array;

    //Variables for listview of entries
    String name;
    Integer amount;

    //Super important split variable
    Integer split;

    //Variables for gets or pays
    String contact;
    float paid;
    float newAmount;
    float gets;
    float pays;

    //Variables for calculating who owes whom
    float eAmount;
    float eNextAmount = 0;
    float checkValue = 0;

    String eContact = "";
    String eNextContact = "";

    String getsOrPays = "";
    String nextGetsOrPays = "";

    List<String> listItemsName=new ArrayList<String>();
    List<String> calculatedPayments=new ArrayList<String>();
    List<Integer> entryIds = new ArrayList<Integer>();

    private int pId = -1;
    private String pName = "";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting the sent variable
        Bundle args = getArguments();
        if (args != null) {
            this.pId = args.getInt("pId");
            Log.d("koppur",""+this.pId);
            this.pName = args.getString("pName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.lay_money_pot, container, false);
        //Get the addEntry button from the layout
        addEntry = (Button) view.findViewById(R.id.buttonAddEntry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry(v);
            }
        });

        deletePot = (Button) view.findViewById(R.id.deletePotButton);
        deletePot.setText("Delete " + pName);
        deletePot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePot(v);
            }
        });

        listItemsName=new ArrayList<String>();
        calculatedPayments=new ArrayList<String>();
        entryIds = new ArrayList<Integer>();

        //database variables
        dbhelper = new DbHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        populateListViewWithEntries();
        calculateSplit();
        getPaysOrGets();
        calculateWhoOwesWhom();

        ((MainActivity) getActivity()).setActionBarTitle(pName);

        return view;
    }

    public void calculateWhoOwesWhom() {

        Boolean again = true;

        while (again) {
            if(array.length<=1) {
                break;
            }
            if (checkArray(array)) {
                break;
            }

            for (int j = 0; j< array.length; j++) {

                eAmount = Float.parseFloat(array[j][1]);
                eContact = array[j][0];
                getsOrPays = array[j][2];

                Log.i("While loop check", "Values in array: " + eAmount);

                for (int k =0; k < array.length; k++) {

                    eNextAmount = Float.parseFloat(array[k][1]);
                    eNextContact = array[k][0];
                    nextGetsOrPays = array[k][2];

                    if (!eContact.equals(eNextContact)) {

                        if (getsOrPays.equals("gets") && nextGetsOrPays.equals("pays")) {
                            array[j][1] = String.valueOf(eAmount - eNextAmount);
                            array[k][1] = String.valueOf(0);

                            if (eNextAmount !=0) {
                                calculatedPayments.add(eContact + " gets " + Math.round(eNextAmount) + " from " + eNextContact);
                            }

                            checkValue = eAmount - eNextAmount;

                            if (checkValue < 0) {
                                array[j][2] = "pays";
                                array[j][1] = String.valueOf(Math.abs(Float.parseFloat(array[j][1])));
                            }
                            if (checkValue > 0) {
                                array[j][2] = "gets";
                            }
                            if (checkValue == 0) {
                                array[j][2] = "";
                            }
                            break;
                        }

                        if (getsOrPays.equals("pays") && nextGetsOrPays.equals("gets")) {
                            array[j][1] = String.valueOf(0);
                            array[k][1] = String.valueOf(eNextAmount - eAmount);

                            if (eAmount !=0) {
                                calculatedPayments.add(eNextContact + " gets " + Math.round(eAmount) + " from " + eContact);
                            }

                            checkValue = eNextAmount - eAmount;

                            if (checkValue < 0) {
                                array[k][2] = "pays";
                                array[k][1] = String.valueOf(Math.abs(Float.parseFloat(array[k][1])));
                            }
                            if (checkValue > 0) {
                                array[k][2] = "gets";
                            }
                            if (checkValue == 0) {
                                array[k][2] = "";
                            }
                            break;
                        }
                    }
                }
            }
        }

        listSplit = (ListView) view.findViewById(R.id.calculatedSplit);

        ArrayAdapter<String> adapterSplit = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_mp_row, R.id.rowEntry, calculatedPayments);
        listSplit.setAdapter(adapterSplit);
    }

    public void getPaysOrGets() {

        //select name, sum(amount) from pots group by name;
        Cursor cursorAmount = db.rawQuery(
                "SELECT name, sum(amount) FROM POTS WHERE _pot_id=" +  this.pId + " GROUP BY name", null);

        array = new String[numOfContacts][3];
        Integer i = 0;

        //Calculate which contact has to pay to the moneypot and which gets money from it.
        while(cursorAmount.moveToNext()) {
            contact = cursorAmount.getString(0);
            paid = cursorAmount.getInt(1);
            newAmount = paid-split;
            //if you pay more money than the average money paid, you will get money from the pot
            if(paid>split) {
                gets = newAmount;

                array[i][0] = contact;
                array[i][1] = String.valueOf(gets);
                array[i][2] = "gets";

                calculatedPayments.add(contact + " gets:   " + Math.round(gets));
            }
            //if you pay less money than the average money paid, you will have to pay to the pot
            if(paid<split) {
                pays = -newAmount;

                array[i][0] = contact;
                array[i][1] = String.valueOf(pays);
                array[i][2] = "pays";

                calculatedPayments.add(contact + " pays:   " + Math.round(pays));
            }
            //if you paid the exact average of the money paid to the pot, you will neither get
            //money nor have to pay money
            if(paid == split) {
                calculatedPayments.add(contact + " paid exactly the amount to split: " + Math.round(paid));
            }
            i +=1;

            if(cursorAmount.isLast()) {
                calculatedPayments.add("");
            }
        }

        cursor.close();
    }

    public void calculateSplit() {
        //NumOfContacts != 0
        split = 0;
        if(!numOfContacts.equals(0)) {
            split = total_amount / numOfContacts;
        }
        showSplit = (TextView) view.findViewById(R.id.showSplit);
        showSplit.setText(String.valueOf(split));
    }

    public void populateListViewWithEntries() {
        //only get name and amount of money paid from the database
        String[] columns = {"name", "amount", "_pot_entry"};
        String where = "_pot_id = "+pId;

        cursor = db.query("POTS",columns,where,null,null,null,null);
        total_amount = 0;

        numOfContacts = 0;
        HashSet uniqueNames = new HashSet();
        int eId;

        //Sum up the total amount of the moneypot
        while(cursor.moveToNext()) {
            name = cursor.getString(0);
            amount = cursor.getInt(1);
            eId = cursor.getInt(2);
            total_amount += amount;

            uniqueNames.add(name);
            entryIds.add(eId);
            listItemsName.add(name + ":   " + amount);
        }

        cursor.close();

        //Number of unique names in the moneypot
        numOfContacts = uniqueNames.size();

        //Show the sum of all the payments in the moneypot
        showTotalAmount = (TextView) view.findViewById(R.id.showTotalAmount);
        showTotalAmount.setText(String.valueOf(total_amount));

        //Get the list from the layout
        listView = (ListView) view.findViewById(R.id.mp_nonscroll_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lay_mp_row, R.id.rowEntry, listItemsName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {goToEntry(position);

            }
        });

    }

    private void goToEntry(int position)
    {
        ((MainActivity)getActivity()).changeFragmentToChosenEntry(entryIds.get(position), pId);
    }

    //change the fragment to potEntry
    public void addEntry(View v)
    {
        int counter = 0;
        String[] columns = {"name", "_contact_id"};

        // Selects the column name and puts the column in too cursor.
        cursor = db.query("CONTACTS",columns,"_contact_id != 0",null,null,null,"name");

        // Moves through each row of the db and adds
        // the name of each contact to the listItem
        while(cursor.moveToNext()) {
            counter++;
        }

        cursor.close();

        if(counter == 0)
        {
            ((MainActivity) getActivity()).toastIt("You can't add an entry yet. Please populate your contacts list first.");
            return;
        }
        ((MainActivity)getActivity()).changeFragmentToPotEntry(pName, pId);
    }

    private void deletePot(View v) {
        //delete the contact from the database
        db.delete("ALLPOTS", "_pot_id = " + pId, null);
        db.delete("POTS", "_pot_id = " + pId, null);

        getActivity().onBackPressed();
    }

    public static Boolean checkArray(String[][] array) {
        int count=0;
        for (int k=0; k<array.length; k++) {
            //Log.i("While loop check", "Values in array: " + array[k][1]);
            float swag = Float.parseFloat(array[k][1]);
            if (swag != 0) {
                count +=1;
            }
        }
        if (count <=1) {
        return true;
        } else { return false;}
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}


