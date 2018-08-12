package com.wallner.michael.wpt;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;
import android.widget.TextView;

import com.wallner.michael.wpt.fragments.RoundFragment;
import com.wallner.michael.wpt.db.WPTDataSource;

import static com.wallner.michael.wpt.R.id.container;


public class Game extends AppCompatActivity {


    @Override
    //Game erstellen
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(this);
        db.open();

        //TODO: getInt NullPointerExeption
        final int gameID = this.getIntent().getExtras().getInt("GameID");

        int anzplayer = db.getAnzPlayerbyGameID(gameID);
        int rounds = 0;

        //Anzahl der Spielrunden ermitteln
        switch (anzplayer) {
            case 6: rounds = 10; break;
            case 5: rounds = 12; break;
            case 4: rounds = 15; break;
            case 3: rounds = 20; break;
            //default:
            //    System.out.println("Invalid number of players!");
            //    break;
        }

        String tabs[] = new String[rounds];
        for (int i=1; i <=rounds; i++)
            tabs[i-1] = getString(R.string.game)+ " "+i;

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                findViewById(R.id.toolbar).getContext(),
                tabs));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int round, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(container, RoundFragment
                                .newInstance(round + 1,gameID))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //TextView status = (TextView) findViewById(R.id.status);
        //status.setText("Test");

        //Action Item
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long round = spinner.getSelectedItemId();

                Snackbar.make(view, "Runde " + round.toString() + " beendet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                WPTDataSource db = new WPTDataSource(getBaseContext());
                db.open();
                db.finishRound(gameID, (int)(long) round);
                db.close();

                spinner.setSelection(round.intValue()+1,true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
}
