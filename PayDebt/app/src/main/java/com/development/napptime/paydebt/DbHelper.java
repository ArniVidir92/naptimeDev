package com.development.napptime.paydebt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Napptime on 10/13/14.
 */
//1. define the schema


//sqlite open helper

//create subclass of openhelper and overwrite onCreate() and onUpdate()

class DbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "napptimedb";
    private static final String TABLE_NAME1 = "CONTACTS";
    private static final String UID = "_contact_id" ;
    private static final String NAME = "name";
    private static final String DESC = "description" ;
    private static final String FAV = "favorite" ;
    private static final int DATABASE_VERSION = 1 ;
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
        String query = "CREATE TABLE "+TABLE_NAME1+" (_contact_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), description VARCHAR(255), favorite INTEGER );";
        //try
        //{
            db.execSQL(query);
            Message.message(context, "onCreate called");
        //}
        //catch(SQLException e)
        //{
        //    Message.message(context, ""+e);
        //}
    }

    //Called when database to drop,add tables or any upgrades to the new  schema version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS CONTACTS";
        //try
        //{
            db.execSQL(query);
            onCreate(db);
            Message.message(context,"onUpgrade Called");
        //}
        //catch(SQLException e)
        //{
        //    Message.message(context, ""+e);
        //}
        db.execSQL(query);

        onCreate(db);
    }
}
