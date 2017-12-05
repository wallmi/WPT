package com.wallner.michael.wpt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Michael on 08.04.2017 for WPT
 *
 */

class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "WPT.db";

    static final String TABLE_OPT = "options";
    static final String COLUMN_NAME_OPT = "option";
    static final String COLUMN_NAME_VAL = "value";

    static final String TABLE_GAMES = "games";
    static final String TABLE_ROUNDS = "rounds";

    FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_OPT_TABLE_CREATE = "CREATE TABLE "
        + TABLE_OPT + "( " + COLUMN_NAME_OPT
        + " text primary key , " + COLUMN_NAME_VAL
        + " text not null);";

    private static final String SQL_GAMES_TABLE_CREATE = "CREATE TABLE "
        + TABLE_GAMES + " ( "
        + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "AnzPlayer	INTEGER NOT NULL, "
        + "GameName	TEXT, "
        + "p1_name TEXT, "
        + "p2_name TEXT, "
        + "p3_name TEXT, "
        + "p4_name TEXT, "
        + "p5_name TEXT, "
        + "p6_name TEXT);";

    private static final String SQL_ROUNDS_TABLE_CREATE = "CREATE TABLE "
        + TABLE_ROUNDS + " ( "
        + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
	    + "game_ID INTEGER NOT NULL, "              //ID from Table games
        + "round_nr INTEGER NOT NULL, "             //Round Number
        + "p1_hip INTEGER DEFAULT 0, "
        + "p2_hip INTEGER DEFAULT 0, "
        + "p3_hip INTEGER DEFAULT 0, "
        + "p4_hip INTEGER DEFAULT 0, "
        + "p5_hip INTEGER DEFAULT 0, "
        + "p6_hip INTEGER DEFAULT 0, "
        + "p1_done INTEGER DEFAULT 0, "
        + "p2_done INTEGER DEFAULT 0, "
        + "p3_done INTEGER DEFAULT 0, "
        + "p4_done INTEGER DEFAULT 0, "
        + "p5_done INTEGER DEFAULT 0, "
        + "p6_done INTEGER DEFAULT 0, "
        + "round_finished INTEGER DEFAULT 0);";     //1 if round is finished

    /**
    public void delOptTable (SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUNDS);
    }
    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_OPT_TABLE_CREATE);
        db.execSQL(SQL_GAMES_TABLE_CREATE);
        db.execSQL(SQL_ROUNDS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUNDS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}


