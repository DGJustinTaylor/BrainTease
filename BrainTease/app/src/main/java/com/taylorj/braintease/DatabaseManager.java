package com.taylorj.braintease;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper
{
    private static final String dbName = "BTDB";
    private static final int dbVer = 1;
    public static final String dbTableF = "Facts";
    public static final String dbColumnF = "factText";
    public static final String dbTableJ = "Jokes";
    public static final String dbColumnJ = "jokeText";


    public DatabaseManager(Context context)
    {
        super(context, dbName, null, dbVer);
    }

    //Build the tables that will be used the populate the database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String queryFC = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);" , dbTableF, dbColumnF);
        db.execSQL(queryFC);
        String queryJC = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);" , dbTableJ, dbColumnJ);
        db.execSQL(queryJC);
    }

    //Used for dropping tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String queryF = String.format("DELETE TABLE IF EXISTS %s", dbTableF);
        db.execSQL(queryF);
        String queryJ = String.format("DELETE TABLE IF EXISTS %s", dbTableJ);
        db.execSQL(queryJ);
        onCreate(db);
    }

    //Both of these functions are used for inserting facts/jokes
    //Could have used one function for both, but this is nicer for readability
    public void insertNewFact(String fact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(dbColumnF, fact);
        db.insertWithOnConflict(dbTableF, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertNewJoke(String joke)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbColumnJ, joke);
        db.insertWithOnConflict(dbTableJ, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    //Both of these functions are used for editing facts/jokes
    public void editFact(String fact, String oldFact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbColumnF, fact);
        db.update(dbTableF, values, dbColumnF + " = ?", new String[]{oldFact});
        db.close();
    }

    public void editJoke(String joke, String oldJoke)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbColumnJ, joke);
        db.update(dbTableJ, values, dbColumnJ + " = ?", new String[]{oldJoke});
        db.close();
    }

    //Both of these functions are used for deleting facts/jokes
    public void deleteFact(String fact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(dbTableF, dbColumnF + " = ?", new String[]{fact});
        db.close();
    }

    public void deleteJoke(String joke)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(dbTableJ, dbColumnJ + " = ?", new String[]{joke});
        db.close();
    }

    //Both of these functions are used for building arrays of all facts/jokes
    public ArrayList<String> getAllFacts()
    {
        ArrayList<String> factList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbTableF, new String[]{dbColumnF}, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(dbColumnF);
            factList.add(cursor.getString(index));
        }

        cursor.close();
        db.close();
        return factList;
    }

    public ArrayList<String> getAllJokes()
    {
        ArrayList<String> jokeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbTableJ, new String[]{dbColumnJ}, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(dbColumnJ);
            jokeList.add(cursor.getString(index));
        }

        cursor.close();
        db.close();
        return jokeList;
    }

    //Both of these functions are used for generating a single fact/joke
    public String singleFact()
    {
        ArrayList<String> factList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbTableF, new String[]{dbColumnF}, null, null, null, null, null);
        String theString;

        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(dbColumnF);
            factList.add(cursor.getString(index));
        }

        if(factList.size() > 0)
        {
            int rand = (int) (Math.random() * factList.size());
            theString = factList.get(rand);
        }

        else
        {
            theString = "No facts to show.";
        }

        cursor.close();
        db.close();

        return theString;
    }

    public String singleJoke()
    {
        ArrayList<String> jokeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbTableJ, new String[]{dbColumnJ}, null, null, null, null, null);
        String theString;

        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(dbColumnJ);
            jokeList.add(cursor.getString(index));
        }

        if(jokeList.size() > 0)
        {
            int rand = (int) (Math.random() * jokeList.size());
            theString = jokeList.get(rand);
        }

        else
        {
            theString = "No jokes to show.";
        }

        cursor.close();
        db.close();

        return theString;
    }

    public Boolean checkDups(String whichDB, String theCheck)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        if(whichDB == dbTableF)
        {
            String[] columns = { dbColumnF };
            String selection = dbColumnF + " =?";
            String[] selectionArgs = { theCheck };
            String limit = "1";

            Cursor cursor = db.query(dbTableF, columns, selection, selectionArgs, null, null, null, limit);

            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        }
        else
        {
            String[] columns = { dbColumnJ };
            String selection = dbColumnJ + " =?";
            String[] selectionArgs = { theCheck };
            String limit = "1";

            Cursor cursor = db.query(dbTableJ, columns, selection, selectionArgs, null, null, null, limit);

            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        }
    }
}
