package com.wallner.michael.wpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


class WPTDataSource {

    // Database fields
    private SQLiteDatabase database;
    private FeedReaderDbHelper dbHelper;


    WPTDataSource(Context context) {
        dbHelper = new FeedReaderDbHelper(context);
    }

    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    void close() {
        dbHelper.close();
    }

    //TODO: Setoption erstellen
    long createOption(String Opt, String value) {
        deleteOption(Opt);
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.COLUMN_NAME_OPT, Opt);
        values.put(FeedReaderDbHelper.COLUMN_NAME_VAL, value);

        // Insert the new row, returning the primary key value of the new row
        return database.insert(FeedReaderDbHelper.TABLE_OPT, null, values);
    }

    private void deleteOption(String opt) {
        //System.out.println("Comment deleted with id: " + opt);
        String[] selectionArgs = { opt };
        database.delete(FeedReaderDbHelper.TABLE_OPT, FeedReaderDbHelper.COLUMN_NAME_OPT
                + " = ?", selectionArgs);
    }

    String getOpt (String opt) {
        String Selection = FeedReaderDbHelper.COLUMN_NAME_OPT + " = ?";
        String[] selectionArgs = { opt };
        String[] selectionName = { FeedReaderDbHelper.COLUMN_NAME_VAL };

        Cursor cursor = database.query(
                FeedReaderDbHelper.TABLE_OPT,              // The table to query
                selectionName,                             // The columns to return
                Selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

       String value = "";
        while(cursor.moveToNext()) {
            value = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderDbHelper.COLUMN_NAME_VAL));
        }
        cursor.close();
        return value;
    }

    //Aus Optionen
    String[] getPlayers() {
        int anzplayer = Integer.parseInt(getOpt("anzplayer"));
        String players[] = new String[anzplayer];
        for (int i=1; i <=anzplayer; i++)
            players[i-1] = getOpt("playername"+i);

        return players;
    }

    Integer getAnzPlayerbyGameID(Integer gameID) {
        String Selection = "ID = ?";
        String[] selectionArgs = { gameID.toString() };
        String[] selectionName = { "AnzPlayer"};

        Cursor cursor = database.query(
                FeedReaderDbHelper.TABLE_GAMES,              // The table to query
                selectionName,                             // The columns to return
                Selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String value = "";
        while(cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndexOrThrow("AnzPlayer"));
        }
        cursor.close();
        return Integer.parseInt(value);
    }

    String[] getPlayerNamesbyGameID (Integer gameID) {
        String Selection = "ID = ?";
        String[] selectionArgs = { gameID.toString() };
        String[] selectionName = { "p1_name" , "p2_name", "p3_name", "p4_name",
                "p5_name", "p6_name"};

        Cursor cursor = database.query(
                FeedReaderDbHelper.TABLE_GAMES,              // The table to query
                selectionName,                             // The columns to return
                Selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String[] value = {"","","","","",""};
        while(cursor.moveToNext()) {
            value[0] = cursor.getString(cursor.getColumnIndexOrThrow("p1_name"));
            value[1] = cursor.getString(cursor.getColumnIndexOrThrow("p2_name"));
            value[2] = cursor.getString(cursor.getColumnIndexOrThrow("p3_name"));
            value[3] = cursor.getString(cursor.getColumnIndexOrThrow("p4_name"));
            value[4] = cursor.getString(cursor.getColumnIndexOrThrow("p5_name"));
            value[5] = cursor.getString(cursor.getColumnIndexOrThrow("p6_name"));
        }
        cursor.close();
        return value;
    }

    Integer createGame (int anzplayer, String gameName,
                            String player1, String player2, String player3,
                            String player4, String player5, String player6) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("AnzPlayer", anzplayer);
        values.put("GameName", gameName);
        values.put("p1_name", player1);
        values.put("p2_name", player2);
        values.put("p3_name", player3);
        values.put("p4_name", player4);
        values.put("p5_name", player5);
        values.put("p6_name", player6);

        // Insert the new row, returning the primary key value of the new row
        return (int) database.insert(FeedReaderDbHelper.TABLE_GAMES, null, values);
    }

    void deleteGame (Integer gameID) {
        String[] selectionArgs = { gameID.toString() };

        //Delete all Rounds
        database.delete(FeedReaderDbHelper.TABLE_ROUNDS, "game_ID"
                + " = ?", selectionArgs);

        //Delte the game
        database.delete(FeedReaderDbHelper.TABLE_GAMES, "ID"
                + " = ?", selectionArgs);

    }

    String[][] getGames() {
        //TODO: Check noch ungeprüft
        String[] selectionName = {"ID", "AnzPlayer", "GameName"};

        Cursor cursor = database.query(
                FeedReaderDbHelper.TABLE_GAMES,   // The table to query
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

        database.update("rounds", values,
                "game_ID=? AND round_nr=?", selectionArgs);
    }

    void addRound (Integer gameID, Integer roundNr){
        ContentValues values = new ContentValues();
        values.put("game_ID", gameID);
        values.put("round_nr", roundNr);

        database.insert("rounds", null, values);
    }
}
