package com.development.napptime.paydebt;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by Napptime on 10/13/14.
 * The main Activity of the application. This is the homescreen of the application and acts as a
 * fragment manager for the rest of the application.
 *
 */

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /*
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize helper
        dbHelper = new DbHelper(this);
        //get database object
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        //add a demo entry
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","yo");
        contentValues.put("description","fokk mikill peningur");
        //contentValues.put("favorite", 1);
        long id = sqLiteDatabase.insert("POTS",null,contentValues);
        Message.message(this, "Entry: "+id);

        //read from database

        String[] columns = {"name"};
        Cursor cursor = sqLiteDatabase.query("POTS",
        columns,null,null,null,null,null);
        while(cursor.moveToNext())
            Message.message(this,""+cursor.getString(0));
        */

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // used by us to see the changes on the backstack
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Log.d("console","Jeeeeeeeeeeeeeeeeeeeeeeeeeee");
                    }
                });
        getFragmentManager().removeOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Log.d("console","ooooooooooooooooooooooooooohh");
                    }
                });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        switch(position){
            case 0:
                // Contacts
                fragment = new Contacts();
                mTitle = getString(R.string.title_section1);

                break;
            case 1:
                // Favorites
                fragment = new Favorites();
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                // MyDebts
                fragment = new MyDebts();
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                // Calculator
                fragment = new Calculator();
                mTitle = getString(R.string.title_section4);
                break;
            case 4:
                // About
                fragment = new About();
                mTitle = getString(R.string.title_section5);
                break;
            case 5:
                // MoneyPot
                fragment = new MoneyPot();
                mTitle = getString(R.string.title_section6);
                break;
            case 6:
                // AddDebt
                fragment = new AddDebt();
                mTitle = getString(R.string.title_section7);
                break;
            case 7:
                // About
                fragment = new ChosenDebt();
                mTitle = getString(R.string.title_section8);
                break;
            case 8:
                // About
                fragment = new PotEntry();
                mTitle = getString(R.string.title_section9);
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    // Restores the ActionBar
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    // Changes the fragment so we can add a debt to a specific contact
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToAddDebt(int contactId)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new AddDebt();
        Bundle args = new Bundle();
        args.putInt("cId", contactId);
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Changes the fragment so we can look at a specific contact a debt to a specific contact
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToChosenContact( String name, int cId )
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new ChosenContact();
        Bundle args = new Bundle();
        args.putInt("cId", cId);
        args.putString("cName", name);
        fragment.setArguments(args);
        mTitle = name;
        setTitle(mTitle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //change the fragment so we can access the money pot feature
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToPotEntry()
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new PotEntry();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //change the fragment so we can add another contact to the database
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToAddContact()
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new AddContact();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //change the fragment to see the list of favorite contacts in the app
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToFavorites()
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new Favorites();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //change the fragment to the chosendebt to see more about it
    //   and add it on the backstack so we can use the back button for navigation
    public void changeFragmentToChosenDebt(String debtName, int debtId, int contactId){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new ChosenDebt();
        Bundle args = new Bundle();
        args.putInt("cId", contactId);
        args.putInt("dId", debtId);
        fragment.setArguments(args);
        mTitle = debtName;
        setTitle(mTitle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //override the onBackPressed to make it call the same function because of weird implementation
    // of onBackPressed in older devices
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}





