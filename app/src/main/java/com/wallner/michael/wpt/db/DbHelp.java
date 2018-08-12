package com.wallner.michael.wpt.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


/**
 * Created by Michael on 08.04.2017 for WPT
 *
 */

public class DbHelp extends SQLiteOpenHelper {
    SQLiteDatabase database;
    private Context ct;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    //Database
    private static final String DATABASE_NAME = "WPT.db";

    //Table Options
    static final String TABLE_OPT = "options";
    static final String COLUMN_OPT_OPT = "option";
    static final String COLUMN_OPT_VAL = "value";

    //Optionen
    public static final String OPT_ANZPLAYER = "anzplayer";
    public static final String OPT_NAME_P1 = "playername1";
    public static final String OPT_NAME_P2 = "playername2";
    public static final String OPT_NAME_P3 = "playername3";
    public static final String OPT_NAME_P4 = "playername4";
    public static final String OPT_NAME_P5 = "playername5";
    public static final String OPT_NAME_P6 = "playername6";

    static final String[] OPT_NAMES = {
            OPT_NAME_P1,OPT_NAME_P2,OPT_NAME_P3,
            OPT_NAME_P4,OPT_NAME_P5,OPT_NAME_P6};

    //Table Games
    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_GAMES_ID = "gameID";
    public static final String COLUMN_GAMES_ANZPLAYER = "AnzPlayer";
    public static final String COLUMN_GAMES_NAME = "GameName";
    public static final String COLUMN_GAMES_P1 = OPT_NAME_P1;
    public static final String COLUMN_GAMES_P2 = OPT_NAME_P2;
    public static final String COLUMN_GAMES_P3 = OPT_NAME_P3;
    public static final String COLUMN_GAMES_P4 = OPT_NAME_P4;
    public static final String COLUMN_GAMES_P5 = OPT_NAME_P5;
    public static final String COLUMN_GAMES_P6 = OPT_NAME_P6;

    //Table rounds
    public static final String TABLE_ROUNDS = "rounds";
    public static final String COLUMN_ROUNDS_NR = "round_nr";
    public static final String COLUMN_ROUNDS_P1_HIP = "p1_hip";
    public static final String COLUMN_ROUNDS_P2_HIP = "p2_hip";
    public  static final String COLUMN_ROUNDS_P3_HIP = "p3_hip";
    public static final String COLUMN_ROUNDS_P4_HIP = "p4_hip";
    public static final String COLUMN_ROUNDS_P5_HIP = "p5_hip";
    public static final String COLUMN_ROUNDS_P6_HIP = "p6_hip";
    public static final String COLUMN_ROUNDS_P1_DONE = "p1_done";
    public static final String COLUMN_ROUNDS_P2_DONE = "p2_done";
    public static final String COLUMN_ROUNDS_P3_DONE = "p3_done";
    public static final String COLUMN_ROUNDS_P4_DONE = "p4_done";
    public static final String COLUMN_ROUNDS_P5_DONE = "p5_done";
    public static final String COLUMN_ROUNDS_P6_DONE = "p6_done";
    public static final String COLUMN_ROUNDS_FINISHED = "round_finished";

    DbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ct = context;
    }

    private static final String SQL_OPT_TABLE_CREATE = "CREATE TABLE "
        + TABLE_OPT + "( " + COLUMN_OPT_OPT
        + " text PRIMARY KEY , " + COLUMN_OPT_VAL
        + " text not null);";

    private static final String SQL_GAMES_TABLE_CREATE = "CREATE TABLE "
        + TABLE_GAMES + " ("
        + COLUMN_GAMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_GAMES_ANZPLAYER + " INTEGER NOT NULL, "
        + COLUMN_GAMES_NAME + " TEXT, "
        + COLUMN_GAMES_P1 + " TEXT, "
        + COLUMN_GAMES_P2 + " TEXT, "
        + COLUMN_GAMES_P3 + " TEXT, "
        + COLUMN_GAMES_P4 + " TEXT, "
        + COLUMN_GAMES_P5 + " TEXT, "
        + COLUMN_GAMES_P6 + " TEXT);";

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

    public void open() throws SQLException {
        database = getWritableDatabase();
    }


    public Cursor exSQL (String Table, String Selection, String[] selectionArgs, String [] columsRet){
        try{
            Cursor cursor = database.query(
                    Table,              // The table to query
                    columsRet,                           // The columns to return
                    Selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
            return cursor;
        } catch (Exception ex) {
            err_message(ex);
            return null;
        }
    }

    private void err_message (Exception ex){
        Toast.makeText(ct,"SQL Error: " + ex.getMessage(),Toast.LENGTH_LONG).show();
    }

}


