package com.development.napptime.paydebt;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

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
        contentValues.put("favorite", 1);
        long id = sqLiteDatabase.insert("CONTACTS",null,contentValues);
        Message.message(this, "Entry: "+id);
        */

        //read from database
        /*
        String[] columns = {"name"};
        Cursor cursor = sqLiteDatabase.query("CONTACTS",columns,null,null,null,null,null);
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
                fragment = new ChosenContact();
                mTitle = getString(R.string.title_section8);
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
    public void changeFragmentToAddDebt()
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        fragment = new AddDebt();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}





