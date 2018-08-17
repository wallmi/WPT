package com.wallner.michael.wpt;

import android.content.Context;
//import android.os.Environment;
import android.widget.Spinner;
import android.widget.Toast;

import com.wallner.michael.wpt.db.WPTDataSource;

import java.util.Locale;

/**
 * Created by Michael on 08.04.2017 for WPT
 *
 */

public class GameRules {

    /* Checks if external storage is available for read and write */
    //public boolean isExternalStorageWritable() {
    //    return Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState());
    //}

    //private method of your class
    public int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    //TODO: Aktuell nur Standardschema abgebildet
    public Integer getPoints (Integer hip, Integer done){
        if (hip > done)     return (done - hip)*10;
        if (hip < done)     return (hip - done)*10;
        return 20 + hip * 10;
    }

    public String int2string (Integer i) {
        if (i == null)
            return "0";

        return  String.format(Locale.getDefault(),"%d",i);
    }

    static public boolean roundOK(Context context, int gameID, int round) {
        WPTDataSource db = new WPTDataSource(context);
        db.open();
        int[] values = db.getRoundPoints(gameID,round);
        db.close();

        String msg = "";
        //Summer der angesagten Stiche muss ungleich der Runde sein
        if (values[0]+values[1]+values[2]+values[3]+values[4]+values[5] == round)
            msg = "Gesamt möglich Stiche entsprechen den angesagten Stichen";

        //Summer der gemachte Stiche muss gleich der Runde sein
        if((values[6]+values[7]+values[8]+values[9]+values[10]+values[11]) != round)
            msg = "Die Summe der Stiche in dieser Runde passt nicht";

        if (!msg.isEmpty()) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
