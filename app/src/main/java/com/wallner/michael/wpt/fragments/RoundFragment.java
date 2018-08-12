package com.wallner.michael.wpt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wallner.michael.wpt.db.DbHelp;
import com.wallner.michael.wpt.db.WPTDataSource;
import com.wallner.michael.wpt.help;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.wallner.michael.wpt.R;

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
        args.putInt(DbHelp.COLUMN_ROUNDS_NR, roundNumber);
        args.putInt(DbHelp.COLUMN_GAMES_ID, gameID);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);

        SeekBar[] Seekbars = {
                p1_hip, p2_hip, p3_hip,
                p4_hip, p5_hip, p6_hip,
                p1_done, p2_done, p3_done
        };

        int round = getArguments().getInt(DbHelp.COLUMN_ROUNDS_NR);
        int gameID = getArguments().getInt(DbHelp.COLUMN_GAMES_ID);

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(getContext());
        db.open();
        db.addRound(gameID,round);

        int[] values = db.getRoundPoints(gameID,round);

        for(int i=0; i<Seekbars.length; i++)
            Seekbars[i].setProgress(values[i]);

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

        for(int i=0; i<Seekbars.length; i++) {
            Seekbars[i].setMax(round);
            Seekbars[i].setOnSeekBarChangeListener(this);
        }

        p1_hip.setTag(DbHelp.COLUMN_ROUNDS_P1_HIP);
        p2_hip.setTag(DbHelp.COLUMN_ROUNDS_P2_HIP);
        p3_hip.setTag(DbHelp.COLUMN_ROUNDS_P3_HIP);
        p4_hip.setTag(DbHelp.COLUMN_ROUNDS_P4_HIP);
        p5_hip.setTag(DbHelp.COLUMN_ROUNDS_P5_HIP);
        p6_hip.setTag(DbHelp.COLUMN_ROUNDS_P6_HIP);

        p1_done.setTag(DbHelp.COLUMN_ROUNDS_P1_DONE);
        p2_done.setTag(DbHelp.COLUMN_ROUNDS_P2_DONE);
        p3_done.setTag(DbHelp.COLUMN_ROUNDS_P3_DONE);
        p4_done.setTag(DbHelp.COLUMN_ROUNDS_P4_DONE);
        p5_done.setTag(DbHelp.COLUMN_ROUNDS_P5_DONE);
        p6_done.setTag(DbHelp.COLUMN_ROUNDS_P6_DONE);

        if (db.isRoundFinished(gameID,round))

            db.close();
        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar p, int progress,boolean fromUser) {
        help h = new help();

        int round = getArguments().getInt(DbHelp.COLUMN_ROUNDS_NR);
        int gameID = getArguments().getInt(DbHelp.COLUMN_GAMES_ID);

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

        db.setPoints(gameID, round, p.getTag().toString(), p.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {        }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {        }
}
