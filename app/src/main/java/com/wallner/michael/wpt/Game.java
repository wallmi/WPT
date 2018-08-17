package com.wallner.michael.wpt;

import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.wallner.michael.wpt.fragments.RoundFragment;
import com.wallner.michael.wpt.db.WPTDataSource;
import com.wallner.michael.wpt.fragments.ShowScore;

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

        final int gameID = getIntent().getExtras().getInt("GameID",-1);
        setTitle(getIntent().getExtras().getString("GameName"));

        int anzplayer = db.getAnzPlayerbyGameID(gameID);
        int rounds = GameRules.getRounds(anzplayer);

        String tabs[] = new String[rounds + 1];
        tabs[0] = "Score Board";

        for (int i=1; i <= rounds; i++)
            tabs[i] = getString(R.string.game)+ " "+i;


        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                findViewById(R.id.toolbar).getContext(),
                tabs));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int round, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if (round == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(container, ShowScore
                                    .newInstance(gameID))
                            .commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(container, RoundFragment
                                    .newInstance(round, gameID))
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Not implemented", Toast.LENGTH_LONG).show();
            //TODO: Settings
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
