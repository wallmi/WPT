package com.wallner.michael.wpt.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wallner.michael.wpt.GameRules;
import com.wallner.michael.wpt.R;
import com.wallner.michael.wpt.db.WPTDataSource;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShowScore extends Fragment {

    int gameID = -1;

    @BindView(R.id.p1_name) TextView p1_name;
    @BindView(R.id.p2_name) TextView p2_name;
    @BindView(R.id.p3_name) TextView p3_name;
    @BindView(R.id.p4_name) TextView p4_name;
    @BindView(R.id.p5_name) TextView p5_name;
    @BindView(R.id.p6_name) TextView p6_name;


    @BindView(R.id.p1_points) TextView p1_points;
    @BindView(R.id.p2_points) TextView p2_points;
    @BindView(R.id.p3_points) TextView p3_points;
    @BindView(R.id.p4_points) TextView p4_points;
    @BindView(R.id.p5_points) TextView p5_points;
    @BindView(R.id.p6_points) TextView p6_points;

    @BindView(R.id.p4)    LinearLayout p4;
    @BindView(R.id.p5)    LinearLayout p5;
    @BindView(R.id.p6)    LinearLayout p6;

    public ShowScore() {

    }

    public static ShowScore newInstance(int gameID) {
        ShowScore fragment = new ShowScore();
        Bundle args = new Bundle();
        args.putInt(WPTDataSource.COLUMN_GAMES_ID, gameID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null)
            gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

        View view = inflater.inflate(R.layout.fragment_show_score, container, false);
        ButterKnife.bind(this, view);       //Butterknife

        WPTDataSource db = new WPTDataSource(getContext());
        db.open();
        int anzp = db.getAnzPlayerbyGameID(gameID);
        String [] playernames = db.getPlayerNamesbyGameID(gameID);
        db.close();

        if (anzp < 4)   p4.setVisibility(View.INVISIBLE);
        if (anzp < 5)   p5.setVisibility(View.INVISIBLE);
        if (anzp < 6)   p6.setVisibility(View.INVISIBLE);

        p1_name.setText(playernames[0]);
        p2_name.setText(playernames[1]);
        p3_name.setText(playernames[2]);

        if (anzp >= 4)  p4_name.setText(playernames[3]);
        if (anzp >= 5)  p5_name.setText(playernames[4]);
        if (anzp >= 6)  p6_name.setText(playernames[5]);

        return view;
    }

    @Override
    public void onResume() {
        WPTDataSource db = new WPTDataSource(getContext());
        //int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);
        db.open();

        Integer p1_p = 0, p2_p = 0, p3_p = 0, p4_p = 0, p5_p = 0, p6_p = 0;

        int anzplayer = db.getAnzPlayerbyGameID(gameID);
        int rounds = GameRules.getRounds(anzplayer);

        for (int i=1;i<=rounds;i++){
            if (db.isRoundFinished(gameID,i)){
                int[] points = db.getRoundPoints(gameID,i);
                p1_p += GameRules.getPoints(points[0],points[6]);
                p2_p += GameRules.getPoints(points[1],points[7]);
                p3_p += GameRules.getPoints(points[2],points[8]);
                p4_p += GameRules.getPoints(points[3],points[9]);
                p5_p += GameRules.getPoints(points[4],points[10]);
                p6_p += GameRules.getPoints(points[5],points[11]);
            }
        }

        p1_points.setText(String.format(Locale.getDefault(),"%d",p1_p));
        p2_points.setText(String.format(Locale.getDefault(),"%d",p2_p));
        p3_points.setText(String.format(Locale.getDefault(),"%d",p3_p));
        p4_points.setText(String.format(Locale.getDefault(),"%d",p4_p));
        p5_points.setText(String.format(Locale.getDefault(),"%d",p5_p));
        p6_points.setText(String.format(Locale.getDefault(),"%d",p6_p));

        db.close();
        super.onResume();
    }
}
