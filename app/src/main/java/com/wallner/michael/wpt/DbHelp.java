package com.wallner.michael.wpt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Michael on 08.04.2017 for WPT
 *
 */

class DbHelp extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    //Database
    private static final String DATABASE_NAME = "WPT.db";

    //Table Options
    static final String TABLE_OPT = "options";
    static final String COLUMN_OPT_OPT = "option";
    static final String COLUMN_OPT_VAL = "value";

    //Optionen
    static final String OPT_ANZPLAYER = "anzplayer";
    static final String OPT_NAME_P1 = "playername1";
    static final String OPT_NAME_P2 = "playername2";
    static final String OPT_NAME_P3 = "playername3";
    static final String OPT_NAME_P4 = "playername4";
    static final String OPT_NAME_P5 = "playername5";
    static final String OPT_NAME_P6 = "playername6";

    static final String[] OPT_NAMES = {
            OPT_NAME_P1,OPT_NAME_P2,OPT_NAME_P3,
            OPT_NAME_P4,OPT_NAME_P5,OPT_NAME_P6};


    //Table Games
    static final String TABLE_GAMES = "games";
    static final String COLUMN_GAMES_ID = "gameID";
    static final String COLUMN_GAMES_ANZPLAYER = "AnzPlayer";
    static final String COLUMN_GAMES_NAME = "GameName";
    static final String COLUMN_GAMES_P1 = "p1_name";
    static final String COLUMN_GAMES_P2 = "p2_name";
    static final String COLUMN_GAMES_P3 = "p3_name";
    static final String COLUMN_GAMES_P4 = "p4_name";
    static final String COLUMN_GAMES_P5 = "p5_name";
    static final String COLUMN_GAMES_P6 = "p6_name";

    //Table rounds
    static final String TABLE_ROUNDS = "rounds";
    static final String COLUMN_ROUNDS_NR = "round_nr";
    static final String COLUMN_ROUNDS_P1_HIP = "p1_hip";
    static final String COLUMN_ROUNDS_P2_HIP = "p2_hip";
    static final String COLUMN_ROUNDS_P3_HIP = "p3_hip";
    static final String COLUMN_ROUNDS_P4_HIP = "p4_hip";
    static final String COLUMN_ROUNDS_P5_HIP = "p5_hip";
    static final String COLUMN_ROUNDS_P6_HIP = "p6_hip";
    static final String COLUMN_ROUNDS_P1_DONE = "p1_done";
    static final String COLUMN_ROUNDS_P2_DONE = "p2_done";
    static final String COLUMN_ROUNDS_P3_DONE = "p3_done";
    static final String COLUMN_ROUNDS_P4_DONE = "p4_done";
    static final String COLUMN_ROUNDS_P5_DONE = "p5_done";
    static final String COLUMN_ROUNDS_P6_DONE = "p6_done";
    static final String COLUMN_ROUNDS_FINISHED = "round_finished";

    DbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_OPT_TABLE_CREATE = "CREATE TABLE "
        + TABLE_OPT + "( " + COLUMN_OPT_OPT
        + " text PRIMARY KEY , " + COLUMN_OPT_VAL
        + " text not null);";

    private static final String SQL_GAMES_TABLE_CREATE = "CREATE TABLE "
        + TABLE_GAMES + " ("
        + COLUMN_GAMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_GAMES_ANZPLAYER + " AnzPlayer INTEGER NOT NULL, "
        + COLUMN_GAMES_NAME + " GameName TEXT, "
        + COLUMN_GAMES_P1 + " p1_name TEXT, "
        + COLUMN_GAMES_P2 + " p2_name TEXT, "
        + COLUMN_GAMES_P3 + " p3_name TEXT, "
        + COLUMN_GAMES_P4 + " p4_name TEXT, "
        + COLUMN_GAMES_P5 + " p5_name TEXT, "
        + COLUMN_GAMES_P6 + " p6_name TEXT);";

    private static final String SQL_ROUNDS_TABLE_CREATE = "CREATE TABLE "
        + TABLE_ROUNDS + " ("
	    + COLUMN_GAMES_ID + " INTEGER NOT NULL, "              //ID from Table games
        + COLUMN_ROUNDS_NR + " INTEGER NOT NULL, "             //Round Number
        + COLUMN_ROUNDS_P1_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P2_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P3_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P4_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P5_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P6_HIP + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P1_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P2_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P3_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P4_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P5_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_P6_DONE + " INTEGER DEFAULT 0, "
        + COLUMN_ROUNDS_FINISHED + " INTEGER DEFAULT 0, " //1 if round is finished
        + "PRIMARY KEY(" + COLUMN_GAMES_ID + "," + COLUMN_ROUNDS_NR + ") "
        +   ");";

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


