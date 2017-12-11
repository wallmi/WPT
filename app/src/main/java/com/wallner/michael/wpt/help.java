package com.wallner.michael.wpt;

import android.os.Environment;
import android.widget.Spinner;

/**
 * Created by Michael on 08.04.2017 for WPT
 *
 */

class help {

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState());
    }

    //private method of your class
    int getIndex(Spinner spinner, String myString)
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
    Integer getPoints (Integer hip, Integer done){
        if (hip > done)     return (done - hip)*10;
        if (hip < done)     return (hip - done)*10;
        return 20 + hip * 10;
    }

    String int2string (Integer i) {
        if (i == null)
            return "0";

        return String.format( "%d", i);
        //TODO: Localisation
    }
}
