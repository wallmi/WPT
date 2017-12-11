package com.wallner.michael.wpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


class WPTDataSource {

    // Database fields
    private SQLiteDatabase database;
    private final DbHelp dbH;


    WPTDataSource(Context context) {
        dbH = new DbHelp(context);
    }

    void open() throws SQLException {
        database = dbH.getWritableDatabase();
    }

    void close() {
        dbH.close();
    }

    void setOption (String Opt, String value) {
        ContentValues values = new ContentValues();
        values.put(DbHelp.COLUMN_OPT_OPT, Opt);
        values.put(DbHelp.COLUMN_OPT_VAL, value);

        database.replace(DbHelp.TABLE_OPT, null,values);
    }

    String getOpt (String opt) {
        String Selection = DbHelp.COLUMN_OPT_OPT + " = ?";
        String[] selectionArgs = { opt };
        String[] selectionName = { DbHelp.COLUMN_OPT_VAL };

        Cursor cursor = database.query(
                DbHelp.TABLE_OPT,          // The table to query
                selectionName,                         // The columns to return
                Selection,                             // The columns for the WHERE clause
                selectionArgs,                         // The values for the WHERE clause
                null,                         // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        );

       String value = "";
        while(cursor.moveToNext()) {
            value = cursor.getString(
                    cursor.getColumnIndexOrThrow(DbHelp.COLUMN_OPT_VAL));
        }
        cursor.close();
        return value;
    }

    //Aus Optionen
    String[] getPlayers() {
        int anzplayer = Integer.parseInt(getOpt(DbHelp.OPT_ANZPLAYER));
        String players[] = new String[anzplayer];
        for (int i=0; i <anzplayer; i++)
            players[i] = getOpt(DbHelp.OPT_NAMES[i]);

        return players;
    }

    Integer getAnzPlayerbyGameID(Integer gameID) {
        String Selection = DbHelp.COLUMN_GAMES_ID + "= ?";
        String[] selectionArgs = { gameID.toString() };
        String[] selectionName = { DbHelp.COLUMN_GAMES_ANZPLAYER};

        Cursor cursor = database.query(
                DbHelp.TABLE_GAMES,              // The table to query
                selectionName,                             // The columns to return
                Selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String value = "";
        while(cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndexOrThrow(DbHelp.COLUMN_GAMES_ANZPLAYER));
        }
        cursor.close();
        return Integer.parseInt(value);
    }

    String[] getPlayerNamesbyGameID (Integer gameID) {
        String Selection = DbHelp.COLUMN_GAMES_ID + "= ?";
        String[] selectionArgs = { gameID.toString() };
        String[] selectionName = { DbHelp.COLUMN_GAMES_P1 ,
                DbHelp.COLUMN_GAMES_P2,
                DbHelp.COLUMN_GAMES_P3,
                DbHelp.COLUMN_GAMES_P4,
                DbHelp.COLUMN_GAMES_P5,
                DbHelp.COLUMN_GAMES_P6
                };

        Cursor cursor = database.query(
                DbHelp.TABLE_GAMES,              // The table to query
                selectionName,                             // The columns to return
                Selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String[] value = {"","","","","",""};
        while(cursor.moveToNext())
            for (int i=0; i<=5; i++)
                value[i] = cursor.getString(cursor.getColumnIndexOrThrow(DbHelp.OPT_NAMES[0]));

        cursor.close();
        return value;
    }


    int createGame (int anzplayer, String gameName,
                            String player1, String player2, String player3,
                            String player4, String player5, String player6) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        String[] players = {player1,player2,player3,player4,player5,player6};
        values.put(DbHelp.COLUMN_GAMES_ANZPLAYER, anzplayer);
        values.put(DbHelp.COLUMN_GAMES_NAME, gameName);
        for (int i=0; i<=5; i++)
            values.put(DbHelp.OPT_NAMES[i], players[i]);

        return (int) database.insert(DbHelp.TABLE_GAMES, null, values);
    }

    void deleteGame (Integer gameID) {
        String[] selectionArgs = { gameID.toString() };

        //Delete all Rounds
        database.delete(DbHelp.TABLE_ROUNDS, DbHelp.COLUMN_GAMES_ID
                + " = ?", selectionArgs);

        //Delte the game
        database.delete(DbHelp.TABLE_GAMES, DbHelp.COLUMN_GAMES_ID
                + " = ?", selectionArgs);
    }

    String[][] getGames() {
        //TODO: Check noch ungeprüft
        String[] selectionName = {DbHelp.COLUMN_GAMES_ID,
                DbHelp.COLUMN_GAMES_ANZPLAYER,
                DbHelp.COLUMN_GAMES_NAME};

        Cursor cursor = database.query(
                DbHelp.TABLE_GAMES,   // The table to query
                selectionName,                    // The columns to return
                null,                             // The columns for the WHERE clause
                null,                             // The values for the WHERE clause
                null,                             // don't group the rows
                null,                             // don't filter by row groups
                null                              // The sort order
        );

        int i = 0;
        String Games[][] = new String[cursor.getCount()][3];
        while (cursor.moveToNext()) {
            Games[i][0] = cursor.getString(0);
            Games[i][1] = cursor.getString(1);
            Games[i][2] = cursor.getString(2);
            i++;
        }
        cursor.close();
        return Games;
    }

    void setPoints (Integer gameID, Integer roundNr, String field, int value) {

        ContentValues values = new ContentValues();
        values.put(field, value);

        String[] selectionArgs ={gameID.toString(), roundNr.toString()};

        database.update(DbHelp.TABLE_ROUNDS, values,
                DbHelp.COLUMN_GAMES_ID+"=? AND "
                        + DbHelp.COLUMN_ROUNDS_NR+"=?", selectionArgs);
    }

    void addRound (Integer gameID, Integer roundNr){
        ContentValues values = new ContentValues();
        values.put(DbHelp.COLUMN_GAMES_ID, gameID);
        values.put(DbHelp.COLUMN_ROUNDS_NR, roundNr);

        database.insert(DbHelp.TABLE_ROUNDS, null, values);
    }
}
