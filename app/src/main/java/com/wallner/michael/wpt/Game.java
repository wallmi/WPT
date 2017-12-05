package com.wallner.michael.wpt;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;
import android.widget.TextView;

import static com.wallner.michael.wpt.R.id.container;


public class Game extends AppCompatActivity {

    @Override
    //Game erstellen
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(this);
        db.open();

        final int gameID = this.getIntent().getExtras().getInt("GameID",-1);
        int anzplayer = db.getAnzPlayerbyGameID(gameID);
        int rounds = 0;

        //Anzahl der Spielrunden ermitteln
        switch (anzplayer) {
            case 6: rounds = 10; break;
            case 5: rounds = 12; break;
            case 4: rounds = 15; break;
            case 3: rounds = 20; break;
            default:
                System.out.println("Invalid number of players!");
                break;
        }

        String tabs[] = new String[rounds];
        for (int i=1; i <=rounds; i++)
            tabs[i-1] = "Runde "+i;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                tabs));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int round, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(container, RoundFragment.newInstance(round + 1,gameID))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //TextView status = (TextView) findViewById(R.id.status);
        //status.setText("Test");

        //Action Item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Runde beendet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                RoundFragment.finish();

                //TODO: nächste Runde springen

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


    /**
     * A placeholder fragment containing a simple view.
     */
    //public static class RoundFragment extends Fragment implements NumberPicker.OnValueChangeListener {
    public static class RoundFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ROUND_NUMBER = "round_number";
        private static final String GAME_ID = "gameid";

        private TextView p1_name;        private TextView p2_name;
        private TextView p3_name;        private TextView p4_name;
        private TextView p5_name;        private TextView p6_name;

        private SeekBar p1_hip;     private SeekBar p2_hip;
        private SeekBar p3_hip;     private SeekBar p4_hip;
        private SeekBar p5_hip;     private SeekBar p6_hip;

        private SeekBar p1_done;    private SeekBar p2_done;
        private SeekBar p3_done;    private SeekBar p4_done;
        private SeekBar p5_done;    private SeekBar p6_done;

        private TextView p1_points;      private TextView p2_points;
        private TextView p3_points;      private TextView p4_points;
        private TextView p5_points;      private TextView p6_points;

        public RoundFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RoundFragment newInstance(int roundNumber, int gameID) {
            RoundFragment fragment = new RoundFragment();
            Bundle args = new Bundle();
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ROUND_NUMBER, roundNumber);
            args.putInt(GAME_ID, gameID);
            fragment.setArguments(args);
            return fragment;
        }

        public static void finish (){
            help h = new help();

            //p1_points.setText(h.getPoints(p1_hip.getValue(),p1_done.getValue()).toString());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //final EditText p1 = (EditText) rootView.findViewById(R.id.p1);

            p1_name = rootView.findViewById(R.id.p1_name);
            p2_name = rootView.findViewById(R.id.p2_name);
            p3_name = rootView.findViewById(R.id.p3_name);
            p4_name = rootView.findViewById(R.id.p4_name);
            p5_name = rootView.findViewById(R.id.p5_name);
            p6_name = rootView.findViewById(R.id.p6_name);

            p1_hip = rootView.findViewById(R.id.p1_hip);
            p2_hip = rootView.findViewById(R.id.p2_hip);
            p3_hip = rootView.findViewById(R.id.p3_hip);
            p4_hip = rootView.findViewById(R.id.p4_hip);
            p5_hip = rootView.findViewById(R.id.p5_hip);
            p6_hip = rootView.findViewById(R.id.p6_hip);

            p1_done = rootView.findViewById(R.id.p1_done);
            p2_done = rootView.findViewById(R.id.p2_done);
            p3_done = rootView.findViewById(R.id.p3_done);
            p4_done = rootView.findViewById(R.id.p4_done);
            p5_done = rootView.findViewById(R.id.p5_done);
            p6_done = rootView.findViewById(R.id.p6_done);

            p1_points = rootView.findViewById(R.id.p1_points);
            p2_points = rootView.findViewById(R.id.p2_points);
            p3_points = rootView.findViewById(R.id.p3_points);
            p4_points = rootView.findViewById(R.id.p4_points);
            p5_points = rootView.findViewById(R.id.p5_points);
            p6_points = rootView.findViewById(R.id.p6_points);

            int round = getArguments().getInt(ROUND_NUMBER);
            int gameID = getArguments().getInt(GAME_ID);

            //Optionen DB öffnen
            WPTDataSource db = new WPTDataSource(getContext());
            db.open();

            //int anzp = Integer.parseInt(o.getValue("anzplayer"));
            int anzp = db.getAnzPlayerbyGameID(gameID);

            if (anzp < 4)
                rootView.findViewById(R.id.p4).setVisibility(View.INVISIBLE);

            if (anzp < 5)
                rootView.findViewById(R.id.p5).setVisibility(View.INVISIBLE);

            if (anzp < 6)
                rootView.findViewById(R.id.p6).setVisibility(View.INVISIBLE);

            String [] playernames = db.getPlayerNamesbyGameID(gameID);

            p1_name.setText(playernames[0]);
            p2_name.setText(playernames[1]);
            p3_name.setText(playernames[2]);

            if (anzp >= 4)
                p4_name.setText(playernames[3]);
            if (anzp >= 5)
                p5_name.setText(playernames[4]);
            if (anzp >= 6)
                p6_name.setText(playernames[5]);

            p1_hip.setMax(round);        p2_hip.setMax(round);
            p3_hip.setMax(round);        p4_hip.setMax(round);
            p5_hip.setMax(round);        p6_hip.setMax(round);

            p1_done.setMax(round);       p2_done.setMax(round);
            p3_done.setMax(round);       p4_done.setMax(round);
            p5_done.setMax(round);       p6_done.setMax(round);


            //p1_hip.setOnValueChangedListener(this);
            //p2_hip.setOnValueChangedListener(this);
            //p3_hip.setOnValueChangedListener(this);
            //p4_hip.setOnValueChangedListener(this);
            //p5_hip.setOnValueChangedListener(this);
            //p6_hip.setOnValueChangedListener(this);

            //p1_done.setOnValueChangedListener(this);
            //p2_done.setOnValueChangedListener(this);
            //p3_done.setOnValueChangedListener(this);
            //p4_done.setOnValueChangedListener(this);
            //p5_done.setOnValueChangedListener(this);
            // p6_done.setOnValueChangedListener(this);


            return rootView;
        }

        //Change Event an den 2 Piker, Hip and done
        //TODO: Aktuell Endloschleife
        //

        /*
        @Override
        public void onValueChange(NumberPicker p, int oldVal, int newVal) {
            help h = new help();

            if (p.getId() == p1_done.getId() || p.getId() == p1_hip.getId())
               p1_points.setText(h.getPoints(p1_hip.getValue(),p1_done.getValue()).toString());

            if (p.getId() == p2_done.getId() || p.getId() == p2_hip.getId())
                p2_points.setText(h.getPoints(p2_hip.getValue(),p2_done.getValue()).toString());

            if (p.getId() == p3_done.getId() || p.getId() == p3_hip.getId())
                p3_points.setText(h.getPoints(p3_hip.getValue(),p3_done.getValue()).toString());

            if (p.getId() == p4_done.getId() || p.getId() == p4_hip.getId())
                p4_points.setText(h.getPoints(p4_hip.getValue(),p4_done.getValue()).toString());

            if (p.getId() == p5_done.getId() || p.getId() == p5_hip.getId())
                p5_points.setText(h.getPoints(p5_hip.getValue(),p5_done.getValue()).toString());

            if (p.getId() == p6_done.getId() || p.getId() == p6_hip.getId())
                p6_points.setText(h.getPoints(p6_hip.getValue(),p6_done.getValue()).toString());
        }*/
    }



    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
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
