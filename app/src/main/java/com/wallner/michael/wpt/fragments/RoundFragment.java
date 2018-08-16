package com.wallner.michael.wpt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wallner.michael.wpt.db.WPTDataSource;
import com.wallner.michael.wpt.GameRules;
import com.wallner.michael.wpt.R;

import butterknife.BindView;        //Butterknife
import butterknife.ButterKnife;     //Butterknife


public class RoundFragment extends Fragment
        implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.p1_name) TextView p1_name;
    @BindView(R.id.p2_name) TextView p2_name;
    @BindView(R.id.p3_name) TextView p3_name;
    @BindView(R.id.p4_name) TextView p4_name;
    @BindView(R.id.p5_name) TextView p5_name;
    @BindView(R.id.p6_name) TextView p6_name;

    @BindView(R.id.p1_hip) SeekBar p1_hip;
    @BindView(R.id.p2_hip) SeekBar p2_hip;
    @BindView(R.id.p3_hip) SeekBar p3_hip;
    @BindView(R.id.p4_hip) SeekBar p4_hip;
    @BindView(R.id.p5_hip) SeekBar p5_hip;
    @BindView(R.id.p6_hip) SeekBar p6_hip;

    @BindView(R.id.p1_done) SeekBar p1_done;
    @BindView(R.id.p2_done) SeekBar p2_done;
    @BindView(R.id.p3_done) SeekBar p3_done;
    @BindView(R.id.p4_done) SeekBar p4_done;
    @BindView(R.id.p5_done) SeekBar p5_done;
    @BindView(R.id.p6_done) SeekBar p6_done;

    @BindView(R.id.p1_points) TextView p1_points;
    @BindView(R.id.p2_points) TextView p2_points;
    @BindView(R.id.p3_points) TextView p3_points;
    @BindView(R.id.p4_points) TextView p4_points;
    @BindView(R.id.p5_points) TextView p5_points;
    @BindView(R.id.p6_points) TextView p6_points;


    @BindView(R.id.p1_dealer) ImageView p1_dealer;
    @BindView(R.id.p2_dealer) ImageView p2_dealer;
    @BindView(R.id.p3_dealer) ImageView p3_dealer;
    @BindView(R.id.p4_dealer) ImageView p4_dealer;
    @BindView(R.id.p5_dealer) ImageView p5_dealer;
    @BindView(R.id.p6_dealer) ImageView p6_dealer;

    @BindView(R.id.p4)    LinearLayout p4;
    @BindView(R.id.p5)    LinearLayout p5;
    @BindView(R.id.p6)    LinearLayout p6;

    @BindView(R.id.fab) FloatingActionButton fab;

    @BindView(R.id.all_done) TextView all_done;
    @BindView(R.id.all_hip) TextView all_hip;

    @BindView(R.id.main) LinearLayout main;

    public RoundFragment() {
    }

    public static RoundFragment newInstance(int roundNumber, int gameID) {
        RoundFragment fragment = new RoundFragment();
        Bundle args = new Bundle();

        args.putInt(WPTDataSource.COLUMN_ROUNDS_NR, roundNumber);
        args.putInt(WPTDataSource.COLUMN_GAMES_ID, gameID);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);       //Butterknife

        SeekBar[] Seekbars = {
                p1_hip, p2_hip, p3_hip,
                p4_hip, p5_hip, p6_hip,
                p1_done, p2_done, p3_done,
                p4_done, p5_done, p6_done
        };

        int round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(getContext());
        db.open();
        db.addRound(gameID,round);

        //int anzp = Integer.parseInt(o.getValue("anzplayer"));
        int anzp = db.getAnzPlayerbyGameID(gameID);

        if (anzp < 4)
            p4.setVisibility(View.INVISIBLE);
            //rootView.findViewById(R.id.p4).setVisibility(View.INVISIBLE);

        if (anzp < 5)
            p5.setVisibility(View.INVISIBLE);

        if (anzp < 6)
            p6.setVisibility(View.INVISIBLE);

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

        for (SeekBar sb: Seekbars){
            sb.setMax(round);
            sb.setOnSeekBarChangeListener(this);
        }

        p1_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P1_HIP);
        p2_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P2_HIP);
        p3_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P3_HIP);
        p4_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P4_HIP);
        p5_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P5_HIP);
        p6_hip.setTag(WPTDataSource.COLUMN_ROUNDS_P6_HIP);

        p1_done.setTag(WPTDataSource.COLUMN_ROUNDS_P1_DONE);
        p2_done.setTag(WPTDataSource.COLUMN_ROUNDS_P2_DONE);
        p3_done.setTag(WPTDataSource.COLUMN_ROUNDS_P3_DONE);
        p4_done.setTag(WPTDataSource.COLUMN_ROUNDS_P4_DONE);
        p5_done.setTag(WPTDataSource.COLUMN_ROUNDS_P5_DONE);
        p6_done.setTag(WPTDataSource.COLUMN_ROUNDS_P6_DONE);

        int[] values = db.getRoundPoints(gameID,round);
        for(int i=0; i<Seekbars.length; i++)
            Seekbars[i].setProgress(values[i]);

        /*
         *  Button für nächste Runde
         */
        //FloatingActionButton fab = findViewById(R.id.fab);

        db.close();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Long round = spinner.getSelectedItemId();
                Integer round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
                WPTDataSource db = new WPTDataSource(getContext());
                int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

                if (GameRules.roundOK(getContext(), gameID, round)) {
                    db.open();
                    db.finishRound(gameID, (int) (long) round);
                    db.close();

                    //Integer showRound = (int) (long) round + 1;
                    Snackbar.make(view, "Runde " + round.toString() + " beendet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Spinner sp = getActivity().findViewById(R.id.spinner);
                    sp.setSelection(round, true);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Integer round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        WPTDataSource db = new WPTDataSource(getContext());
        int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

        db.open();
        if (db.isRoundFinished(gameID,round))
            fab.setVisibility(View.INVISIBLE);

        db.close();
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Integer round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        WPTDataSource db = new WPTDataSource(getContext());
        int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);
        db.open();
        SeekBar[] Seekbars = {
                p1_hip, p2_hip, p3_hip,
                p4_hip, p5_hip, p6_hip,
                p1_done, p2_done, p3_done,
                p4_done, p5_done, p6_done
        };
        boolean roundfinished = db.isRoundFinished(gameID,round);

        for (SeekBar sb: Seekbars) {
            //sb.setProgress(0);
            sb.callOnClick();
            sb.setEnabled(!roundfinished);
        }

        if (roundfinished)
            Toast.makeText(getContext(),
                    "Runde " +  round.toString()  + " ist schon beendet", Toast.LENGTH_LONG).show();

      db.close();
    }

    @Override
    public void onProgressChanged(SeekBar p, int progress,boolean fromUser) {
        GameRules h = new GameRules();

        int round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

        WPTDataSource db = new WPTDataSource(getContext());
        db.open();

        if (p.getId() == p1_done.getId() || p.getId() == p1_hip.getId())
            p1_points.setText(h.int2string(h.getPoints(p1_hip.getProgress(), p1_done.getProgress())));

        if (p.getId() == p2_done.getId() || p.getId() == p2_hip.getId())
            p2_points.setText(h.int2string(h.getPoints(p2_hip.getProgress(),p2_done.getProgress())));

        if (p.getId() == p3_done.getId() || p.getId() == p3_hip.getId())
            p3_points.setText(h.int2string(h.getPoints(p3_hip.getProgress(),p3_done.getProgress())));

        if (p.getId() == p4_done.getId() || p.getId() == p4_hip.getId())
            p4_points.setText(h.int2string(h.getPoints(p4_hip.getProgress(),p4_done.getProgress())));

        if (p.getId() == p5_done.getId() || p.getId() == p5_hip.getId())
            p5_points.setText(h.int2string(h.getPoints(p5_hip.getProgress(),p5_done.getProgress())));

        if (p.getId() == p6_done.getId() || p.getId() == p6_hip.getId())
            p6_points.setText(h.int2string(h.getPoints(p6_hip.getProgress(),p6_done.getProgress())));

        all_done.setText(h.int2string(p1_done.getProgress()+p2_done.getProgress()+p3_done.getProgress()
                +p4_done.getProgress()+p5_done.getProgress()+p6_done.getProgress()));

        all_hip.setText(h.int2string(p1_hip.getProgress()+p2_hip.getProgress()+p3_hip.getProgress()
                +p4_hip.getProgress()+p5_hip.getProgress()+p6_hip.getProgress()));

        db.setPoints(gameID, round, p.getTag().toString(), p.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {        }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {        }
}
