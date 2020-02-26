package com.wallner.michael.wpt.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wallner.michael.wpt.GameRules;
import com.wallner.michael.wpt.R;
import com.wallner.michael.wpt.ValueSelector;
import com.wallner.michael.wpt.db.WPTDataSource;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RoundFragmentNew extends Fragment
        implements ValueSelector.OnValueChangeListener
        {
    int round = -1; int gameID = -1;

    @BindView(R.id.p1_name) TextView p1_name;
    @BindView(R.id.p2_name) TextView p2_name;
    @BindView(R.id.p3_name) TextView p3_name;
    @BindView(R.id.p4_name) TextView p4_name;
    @BindView(R.id.p5_name) TextView p5_name;
    @BindView(R.id.p6_name) TextView p6_name;

    @BindView(R.id.p1_hip) ValueSelector p1_hip;
    @BindView(R.id.p2_hip) ValueSelector p2_hip;
    @BindView(R.id.p3_hip) ValueSelector p3_hip;
    @BindView(R.id.p4_hip) ValueSelector p4_hip;
    @BindView(R.id.p5_hip) ValueSelector p5_hip;
    @BindView(R.id.p6_hip) ValueSelector p6_hip;

    @BindView(R.id.p1_done) ValueSelector p1_done;
    @BindView(R.id.p2_done) ValueSelector p2_done;
    @BindView(R.id.p3_done) ValueSelector p3_done;
    @BindView(R.id.p4_done) ValueSelector p4_done;
    @BindView(R.id.p5_done) ValueSelector p5_done;
    @BindView(R.id.p6_done) ValueSelector p6_done;

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

    @BindView(R.id.p4)    ConstraintLayout p4;
    @BindView(R.id.p5)    ConstraintLayout p5;
    @BindView(R.id.p6)    ConstraintLayout p6;

    @BindView(R.id.all_done) TextView all_done;
    @BindView(R.id.all_hip) TextView all_hip;

    public RoundFragmentNew() {
    }

    public static RoundFragmentNew newInstance(int roundNumber, int gameID) {
        RoundFragmentNew fragment = new RoundFragmentNew();
        Bundle args = new Bundle();

        args.putInt(WPTDataSource.COLUMN_ROUNDS_NR, roundNumber);
        args.putInt(WPTDataSource.COLUMN_GAMES_ID, gameID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_game_new, container, false);
        ButterKnife.bind(this, view);       //Butterknife

        ValueSelector[] valueSelectors = {
                p1_hip, p2_hip, p3_hip,
                p4_hip, p5_hip, p6_hip,
                p1_done, p2_done, p3_done,
                p4_done, p5_done, p6_done
        };

        Bundle arg = getArguments();

        if (arg != null) {
            round = arg.getInt(WPTDataSource.COLUMN_ROUNDS_NR);
            gameID = arg.getInt(WPTDataSource.COLUMN_GAMES_ID);
        }

        //Optionen DB öffnen
        WPTDataSource db = new WPTDataSource(getContext());
        db.open();

        int anzp = db.getAnzPlayerbyGameID(gameID);

        if (anzp < 4)
            p4.setVisibility(View.INVISIBLE);

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

        for (ValueSelector sb: valueSelectors){
            sb.setMaxValue(round);
            sb.setMinValue(0);
            sb.setOnValueChangeListener(this);
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
        for(int i=0; i<valueSelectors.length; i++)
            valueSelectors[i].setValue(values[i]);

        //Ermittlung des Gebers in der Rund
        //GEBER = ((round -1) % anzp + 1 + (giver - 1)
        switch (((round -1 + db.firstGiver(gameID) - 1) % anzp) + 1) {
            case 1: p1_dealer.setVisibility(View.VISIBLE); break;
            case 2: p2_dealer.setVisibility(View.VISIBLE); break;
            case 3: p3_dealer.setVisibility(View.VISIBLE); break;
            case 4: p4_dealer.setVisibility(View.VISIBLE); break;
            case 5: p5_dealer.setVisibility(View.VISIBLE); break;
            case 6: p6_dealer.setVisibility(View.VISIBLE); break;
        }

        db.close();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Integer round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        WPTDataSource db = new WPTDataSource(getContext());
        //int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);
        db.open();
        ValueSelector[] valueSelectors = {
                p1_hip, p2_hip, p3_hip,
                p4_hip, p5_hip, p6_hip,
                p1_done, p2_done, p3_done,
                p4_done, p5_done, p6_done
        };
        boolean roundfinished = db.isRoundFinished(gameID,round);
        db.close();

        for (ValueSelector vs: valueSelectors) {
            //sb.setProgress(0);
            vs.callOnClick();
            vs.setEnabled(!roundfinished);
        }

        if (roundfinished)
            Toast.makeText(getContext(),String.format(Locale.getDefault(),
                    getString(R.string.round_finished),round),
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressChanged(ValueSelector vs, int progress) {
        //int round = getArguments().getInt(WPTDataSource.COLUMN_ROUNDS_NR);
        //int gameID = getArguments().getInt(WPTDataSource.COLUMN_GAMES_ID);

        WPTDataSource db = new WPTDataSource(getContext());
        db.open();

        if (vs.getId() == p1_done.getId() || vs.getId() == p1_hip.getId())
            p1_points.setText(GameRules.int2string(GameRules.getPoints(p1_hip.getValue(), p1_done.getValue())));

        if (vs.getId() == p2_done.getId() || vs.getId() == p2_hip.getId())
            p2_points.setText(GameRules.int2string(GameRules.getPoints(p2_hip.getValue(),p2_done.getValue())));

        if (vs.getId() == p3_done.getId() || vs.getId() == p3_hip.getId())
            p3_points.setText(GameRules.int2string(GameRules.getPoints(p3_hip.getValue(),p3_done.getValue())));

        if (vs.getId() == p4_done.getId() || vs.getId() == p4_hip.getId())
            p4_points.setText(GameRules.int2string(GameRules.getPoints(p4_hip.getValue(),p4_done.getValue())));

        if (vs.getId() == p5_done.getId() || vs.getId() == p5_hip.getId())
            p5_points.setText(GameRules.int2string(GameRules.getPoints(p5_hip.getValue(),p5_done.getValue())));

        if (vs.getId() == p6_done.getId() || vs.getId() == p6_hip.getId())
            p6_points.setText(GameRules.int2string(GameRules.getPoints(p6_hip.getValue(),p6_done.getValue())));

        all_done.setText(GameRules.int2string(p1_done.getValue()+p2_done.getValue()+p3_done.getValue()
                +p4_done.getValue()+p5_done.getValue()+p6_done.getValue()));

        all_hip.setText(GameRules.int2string(p1_hip.getValue()+p2_hip.getValue()+p3_hip.getValue()
                +p4_hip.getValue()+p5_hip.getValue()+p6_hip.getValue()));

        db.setPoints(gameID, round, vs.getTag().toString(), vs.getValue());
        db.close();
        }
}
