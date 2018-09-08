package com.wallner.michael.wpt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.content.res.Resources.Theme;

import com.wallner.michael.wpt.fragments.RoundFragment;
import com.wallner.michael.wpt.db.WPTDataSource;
import com.wallner.michael.wpt.fragments.ShowScore;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wallner.michael.wpt.R.id.container;


public class Game extends AppCompatActivity {

    int gameID = -1;
    int anzplayer;

    @BindView(R.id.fab)    FloatingActionButton fab;
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.jump)    Button jump;

    @Override
    //Game erstellen
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(this);
        db.open();

        if (getIntent().getExtras() != null) {
            gameID = getIntent().getExtras().getInt("GameID");
            setTitle(getIntent().getExtras().getString("GameName"));
        }

        anzplayer = db.getAnzPlayerbyGameID(gameID);
        int rounds = GameRules.getRounds(anzplayer);

        String tabs[] = new String[rounds + 1];
        tabs[0] = "Score Board";

        for (int i=1; i <= rounds; i++)
            tabs[i] = getString(R.string.game)+ " "+i;

        spinner.setAdapter(new MyAdapter(
                findViewById(R.id.toolbar).getContext(),
                tabs));

        //Runde selektiert
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

                WPTDataSource db = new WPTDataSource(getBaseContext());
                db.open();

                if (round > 0) {
                    if (db.isRoundFinished(gameID, round))
                        fab.setVisibility(View.INVISIBLE);
                    else
                        fab.setVisibility(View.VISIBLE);
                } else
                    fab.setVisibility(View.INVISIBLE);


                if (spinner.getSelectedItemPosition() == 0)
                    jump.setText(String.format(Locale.GERMAN,
                            getString(R.string.round_x), db.getCurrentRound(gameID)));
                else
                    jump.setText(R.string.Scoreboard);


                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //FAB nächste Runde
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Long round = spinner.getSelectedItemId();
                //Integer round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
                Integer round = spinner.getSelectedItemPosition();
                WPTDataSource db = new WPTDataSource(getBaseContext());
                db.open();
                final int gameID = getIntent().getExtras().getInt("GameID",-1);
                final boolean isprevroundfinished = db.isRoundFinished(gameID,round -1);

                if (isprevroundfinished || round == 1){
                    if (GameRules.roundOK(getBaseContext(), gameID, round)) {
                        db.finishRound(gameID, (int) (long) round);
                        db.close();

                        if (round == GameRules.getRounds(anzplayer)) {
                            Toast.makeText(getBaseContext(), R.string.end_game,
                                    Toast.LENGTH_LONG).show();
                            spinner.setSelection(0, true);
                        } else {
                            Snackbar.make(view,
                                    String.format(Locale.GERMAN,
                                            getString(R.string.round_ended),round),
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();



                            spinner.setSelection(round + 1, true);
                        }
                    }
                }
                else
                    Toast.makeText(getBaseContext(), R.string.prev_round_not_ended,
                            Toast.LENGTH_LONG).show();

                db.close();
            }
        });
    }

    public void jumpto(View view){
        //final int gameID = getIntent().getExtras().getInt("GameID",-1);
        if (spinner.getSelectedItemPosition() == 0){
            WPTDataSource db = new WPTDataSource(getBaseContext());
            db.open();
            int curRound = db.getCurrentRound(gameID);
            db.close();
            spinner.setSelection(curRound);
        }
        else
            spinner.setSelection(0);
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
