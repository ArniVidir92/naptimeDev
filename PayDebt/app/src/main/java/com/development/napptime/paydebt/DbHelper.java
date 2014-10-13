package com.development.napptime.paydebt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Napptime on 10/13/14.
 * A helper that extends the default SQLiteOpenHelper
 */


class DbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "napptimedb";
    private static final String TABLE_NAME_C = "CONTACTS";
    private static final String TABLE_NAME_D = "DEBTS";
    /*
    private static final String UID = "_contact_id";
    private static final String DID = "_dept_id" ;
    private static final String NAME = "name";
    private static final String DESC = "description";
    private static final String DATE = "date";
    private static final String DUE = "due";
    private static final String FAV = "favorite";
    */
    private static final int DATABASE_VERSION = 4 ;
    private Context context;


    DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Message.message(context, "Constructor called");
    }



    //Called when database is created for the first time, create tables and initial data
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create table for contacts
        String query = "CREATE TABLE "+TABLE_NAME_C+" (_contact_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), description VARCHAR(255), favorite INTEGER );";
        db.execSQL(query);

        //Create table for debts
        query = "CREATE TABLE "+TABLE_NAME_D+" (_debt_id INTEGER PRIMARY KEY AUTOINCREMENT, _contact_id INTEGER, name VARCHAR(255), description VARCHAR(255), reminder INTEGER, date INTEGER, due INTEGER, amount REAL, object VARCHER(255) );";
        db.execSQL(query);

        //notify user
        Message.message(context, "onCreate called");
    }

    //Called when database to drop,add tables or any upgrades to the new  schema version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Delete table contacts
        String query = "DROP TABLE IF EXISTS CONTACTS";
        db.execSQL(query);
        //Delete table debts
        query = "DROP TABLE IF EXISTS DEBTS";
        db.execSQL(query);

        //create new tables
        onCreate(db);

        //notify user
        Message.message(context,"onUpgrade Called");
    }
}
