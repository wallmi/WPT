package com.wallner.michael.wpt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class WPTDataSource extends DbHelp{

    public WPTDataSource(Context context) {
        super (context);
        ct = context;
    }

    public void setOption (String Opt, String value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_OPT_OPT, Opt);
        values.put(COLUMN_OPT_VAL, value);

        database.replace(TABLE_OPT, null,values);
    }

    public String getOpt (String opt) {
        String Selection = COLUMN_OPT_OPT + " = ?";
        String[] selectionArgs = { opt };
        String[] selectionName = { COLUMN_OPT_VAL };

        Cursor cursor = database.query(
                TABLE_OPT,          // The table to query
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
                    cursor.getColumnIndexOrThrow(COLUMN_OPT_VAL));
        }
        cursor.close();
        return value;
    }

    //Aus Optionen
    public String[] getPlayers() {
        int anzplayer = Integer.parseInt(getOpt(OPT_ANZPLAYER));
        String players[] = new String[anzplayer];
        for (int i=0; i <anzplayer; i++)
            players[i] = getOpt(OPT_NAMES[i]);

        return players;
    }

    public Integer getAnzPlayerbyGameID(Integer gameID) {
        try {
            String Selection = COLUMN_GAMES_ID + "= ?";
            String[] selectionArgs = {gameID.toString()};
            String[] selectionName = {COLUMN_GAMES_ANZPLAYER};

            Cursor cursor = database.query(
                    TABLE_GAMES,              // The table to query
                    selectionName,                             // The columns to return
                    Selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            String value = "";
            while (cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMES_ANZPLAYER));
            }
            cursor.close();
            return Integer.parseInt(value);
        } catch (Exception ex){
            err_message(ex);
            return 0;
        }
    }

    public String[] getPlayerNamesbyGameID (Integer gameID) {
        String Selection = COLUMN_GAMES_ID + "= ?";
        String[] selectionArgs = { gameID.toString() };
        String[] selectionName = { COLUMN_GAMES_P1 ,
                COLUMN_GAMES_P2,
                COLUMN_GAMES_P3,
                COLUMN_GAMES_P4,
                COLUMN_GAMES_P5,
                COLUMN_GAMES_P6
                };

        Cursor cursor = database.query(
                TABLE_GAMES,              // The table to query
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
                value[i] = cursor.getString(cursor.getColumnIndexOrThrow(OPT_NAMES[i]));

        cursor.close();
        return value;
    }


    public int createGame (int anzplayer, String gameName,
                            String player1, String player2, String player3,
                            String player4, String player5, String player6,
                           int giver) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        String[] players = {player1, player2, player3, player4, player5, player6};
        values.put(COLUMN_GAMES_ANZPLAYER, anzplayer);
        values.put(COLUMN_GAMES_NAME, gameName);
        values.put(COLUMN_GAMES_GIVER,giver);
        for (int i=0; i<=5; i++)
            values.put(OPT_NAMES[i], players[i]);

        return (int) database.insert(TABLE_GAMES, null, values);
    }

    public void deleteGame (Integer gameID) {
        String[] selectionArgs = { gameID.toString() };

        //Delete all Rounds
        database.delete(TABLE_ROUNDS, COLUMN_GAMES_ID
                + " = ?", selectionArgs);

        //Delte the game
        database.delete(TABLE_GAMES, COLUMN_GAMES_ID
                + " = ?", selectionArgs);
    }

    public String[][] getGames() {
        //TODO: Check noch ungeprüft
        String[] selectionName = {COLUMN_GAMES_ID,
                COLUMN_GAMES_ANZPLAYER,
                COLUMN_GAMES_NAME};

        Cursor cursor = database.query(
                TABLE_GAMES,   // The table to query
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


    public String getGameName(Integer gameID) {
        String[] selectionName = {
                COLUMN_GAMES_NAME
                };

        String Selection = COLUMN_GAMES_ID + "=?";
        String[]  SelectionArgs = {gameID.toString()};

        Cursor cursor = database.query(
                TABLE_GAMES,   // The table to query
                selectionName,                    // The columns to return
                Selection,                             // The columns for the WHERE clause
                SelectionArgs,                             // The values for the WHERE clause
                null,                             // don't group the rows
                null,                             // don't filter by row groups
                null                              // The sort order
        );
        String ret= "";
        while (cursor.moveToNext()) {
            ret = cursor.getString(0);
        }
        cursor.close();
        return ret;
    }

    public void setPoints (Integer gameID, Integer roundNr, String field, int value) {

        ContentValues values = new ContentValues();
        values.put(field, value);

        String[] selectionArgs ={gameID.toString(), roundNr.toString()};

        database.update(TABLE_ROUNDS, values,
                COLUMN_GAMES_ID+"=? AND "
                        + COLUMN_ROUNDS_NR+"=?", selectionArgs);
    }

    public void addRound (Integer gameID, Integer roundNr){
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_GAMES_ID, gameID);
            values.put(COLUMN_ROUNDS_NR, roundNr);

            database.insert(TABLE_ROUNDS, null, values);
        }
        catch (Exception ex){
            err_message(ex);
        }
    }

    public void finishRound (Integer gameID, Integer roundNr){

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ROUNDS_FINISHED, 1);

            String[] selectionArgs = {gameID.toString(), roundNr.toString()};

            database.update(TABLE_ROUNDS, values,
                    COLUMN_GAMES_ID + "=? AND "
                            + COLUMN_ROUNDS_NR + "=?", selectionArgs);
        } catch (Exception ex){
            err_message(ex);
        }
    }

    public int[] getRoundPoints (Integer gameID, Integer roundNr) {

        try {
            String Selection = COLUMN_GAMES_ID + "=? AND " + COLUMN_ROUNDS_NR + "=?";
            String[] selectionArgs = {gameID.toString(), roundNr.toString()};
            String[] selectionName = {
                    COLUMN_ROUNDS_P1_HIP,
                    COLUMN_ROUNDS_P2_HIP,
                    COLUMN_ROUNDS_P3_HIP,
                    COLUMN_ROUNDS_P4_HIP,
                    COLUMN_ROUNDS_P5_HIP,
                    COLUMN_ROUNDS_P6_HIP,
                    COLUMN_ROUNDS_P1_DONE,
                    COLUMN_ROUNDS_P2_DONE,
                    COLUMN_ROUNDS_P3_DONE,
                    COLUMN_ROUNDS_P4_DONE,
                    COLUMN_ROUNDS_P5_DONE,
                    COLUMN_ROUNDS_P6_DONE,
            };

            Cursor cursor = database.query(
                    TABLE_ROUNDS,              // The table to query
                    selectionName,                             // The columns to return
                    Selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            int[] value = new int[selectionName.length];
            while (cursor.moveToNext())
                for (int i = 0; i <= 11; i++)
                    value[i] = Integer.valueOf(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(selectionName[i])));

            cursor.close();
            return value;
        } catch (Exception ex) {
            err_message(ex);
        }
        return new int[0];
    }

    public Boolean isRoundFinished(Integer gameID, Integer roundNr){

        String[] selectionArgs = {gameID.toString(), roundNr.toString()};
        String[] selectionName = {COLUMN_ROUNDS_FINISHED};
        String Selection = COLUMN_GAMES_ID + "=? AND " + COLUMN_ROUNDS_NR + "=?";

        Cursor c = exSQL(TABLE_ROUNDS, Selection, selectionArgs, selectionName);

        if (c == null)
            return false;

        String value = "";
        while (c.moveToNext()) {
            value = c.getString(c.getColumnIndexOrThrow(COLUMN_ROUNDS_FINISHED));
        }
        c.close();
        return value.equals("1");

    }

}
