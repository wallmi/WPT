package com.wallner.michael.wpt;
import android.support.v7.app.AppCompatActivity;    //Version V7 API
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;        //Butterknife
import butterknife.ButterKnife;     //Butterknife

import com.wallner.michael.wpt.db.WPTDataSource;
import com.wallner.michael.wpt.fragments.SelectPlayer;

import static com.wallner.michael.wpt.db.DbHelp.*;

public class NewGame extends AppCompatActivity
        implements SelectPlayer.NoticeDialogListener {

    private final GameRules h = new GameRules();
    private final WPTDataSource db =new WPTDataSource(this);

    @BindView(R.id.player1) EditText p1;
    @BindView(R.id.player2) EditText p2;
    @BindView(R.id.player3) EditText p3;
    @BindView(R.id.player4) EditText p4;
    @BindView(R.id.player5) EditText p5;
    @BindView(R.id.player6) EditText p6;
    @BindView(R.id.gameName) EditText gn;

    @BindView(R.id.spinner) Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game); //Setze Layout
        ButterKnife.bind(this);             //Butterknife
        spinner = findViewById(R.id.spinner);
        db.open();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.anz_players, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);    //Adapter setzen

        //Setze Listener nach auswahl des Spinners
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = findViewById(R.id.spinner);

                int anzplayer = Integer.parseInt(spinner.getSelectedItem().toString());
                //l1 = sichtbar l2=versteckt
                final LinearLayout.LayoutParams l1 =  new LinearLayout.LayoutParams (
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                final LinearLayout.LayoutParams l2 =  new LinearLayout.LayoutParams (
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);

                    if (anzplayer >= 4) {
                        p4.setVisibility(View.VISIBLE);
                        p4.setLayoutParams(l1);
                    }
                    else {
                        p4.setVisibility(View.INVISIBLE);
                        p4.setLayoutParams(l2);
                    }
                    if (anzplayer >= 5) {
                        p5.setVisibility(View.VISIBLE);
                        p5.setLayoutParams(l1);
                    }
                    else{
                        p5.setVisibility(View.INVISIBLE);
                        p5.setLayoutParams(l2);
                    }
                    if (anzplayer >= 6){
                        p6.setVisibility(View.VISIBLE);
                        p6.setLayoutParams(l1);
                    }
                    else {
                        p6.setVisibility(View.INVISIBLE);
                        p6.setLayoutParams(l2);
                    }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Wenn nichts ausgewählt wurde

            }
        });

        //Kalender
        //TODO: Lokalisierung
        Calendar c = Calendar.getInstance(Locale.GERMAN);
        String gamename = "Spiel vom " +
                c.get(Calendar.DAY_OF_MONTH) +"." +
                c.get(Calendar.MONTH)+ "." +
                c.get(Calendar.YEAR) +" um " +
                c.get(Calendar.HOUR_OF_DAY) + ":" +
                c.get(Calendar.MINUTE);

        gn.setText(gamename);

        spinner.setSelection(h.getIndex(spinner,  db.getOpt(OPT_ANZPLAYER)));
        p1.setText(db.getOpt(OPT_NAME_P1)); p2.setText(db.getOpt(OPT_NAME_P2));
        p3.setText(db.getOpt(OPT_NAME_P3)); p4.setText(db.getOpt(OPT_NAME_P4));
        p5.setText(db.getOpt(OPT_NAME_P5)); p6.setText(db.getOpt(OPT_NAME_P6));

        db.close();

        //Wenn leer dann Standardnamen aus strings.xml ziehen
        if (p1.getText().toString().isEmpty()) p1.setText(getString(R.string.player_1));
        if (p2.getText().toString().isEmpty()) p2.setText(getString(R.string.player_2));
        if (p3.getText().toString().isEmpty()) p3.setText(getString(R.string.player_3));
        if (p4.getText().toString().isEmpty()) p4.setText(getString(R.string.player_4));
        if (p5.getText().toString().isEmpty()) p5.setText(getString(R.string.player_5));
        if (p6.getText().toString().isEmpty()) p6.setText(getString(R.string.player_6));
    }

    //Starten des Dialoges SelectPlayer
    public void startGame (View view) {
        DialogFragment d = new SelectPlayer();
        saveSettings();

        Bundle b = new Bundle();
        db.open();
        b.putStringArray("playerlist",db.getPlayers());
        db.close();

        d.setArguments(b);
        d.show(getFragmentManager(), "Test");
    }

    //Wenn ein Spieler vom Dialog ausgewählt wurde über den Dialog SelectPlayer
    @Override
    public void onClickPlayer(DialogFragment dialog,String selectedplayer ) {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                selectedplayer + " ist der erste Geber!",
                Toast.LENGTH_LONG);
        toast.show();

        int anzplayer = GameRules.getRounds(Integer.parseInt(
                spinner.getSelectedItem().toString()));

        String playernames[] = {
                p1.getText().toString(),p2.getText().toString(),p3.getText().toString(),
                p4.getText().toString(),p5.getText().toString(),p6.getText().toString()
        };

        int giver = 0;

        for (int i = 0;i < playernames.length;i++){
            if (playernames[i].equals(selectedplayer))
                giver = i +1;
        }

        db.open();
        //Erstelle neues Spiel in der Datenbank
        int GameID = db.createGame(anzplayer,
                gn.getText().toString(),
                p1.getText().toString(),p2.getText().toString(),
                p3.getText().toString(),p4.getText().toString(),
                p5.getText().toString(),p6.getText().toString(),
                giver);

        int rounds = GameRules.getRounds(anzplayer);
        for (int i = 1; i <= rounds; i ++)
            db.addRound(GameID,i);

        Intent intent = new Intent(this, Game.class);
        //GameID übergeben
        intent.putExtra("GameID",GameID);
        intent.putExtra("GameName", db.getGameName(GameID));
        //Starte Activity Spiel
        db.close();
        startActivity(intent);
}

    private void saveSettings (){
        db.open();
        db.setOption(OPT_ANZPLAYER,spinner.getSelectedItem().toString());
        db.setOption(OPT_NAME_P1,p1.getText().toString());
        db.setOption(OPT_NAME_P2,p2.getText().toString());
        db.setOption(OPT_NAME_P3,p3.getText().toString());
        db.setOption(OPT_NAME_P4,p4.getText().toString());
        db.setOption(OPT_NAME_P5,p5.getText().toString());
        db.setOption(OPT_NAME_P6,p6.getText().toString());
        db.close();
    }
}