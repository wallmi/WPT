package com.wallner.michael.wpt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoadGame extends AppCompatActivity {

    private ListView lv_games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        WPTDataSource db = new WPTDataSource(this);
        db.open();

        final String[][] games = db.getGames();
        lv_games = (ListView) findViewById(R.id.lv_games);

        final ArrayList<String> list = new ArrayList<>();

        for (String[] game : games) {
            list.add(game[2]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);

        lv_games.setAdapter(adapter);

        // ListView Item Click Listener
        lv_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) lv_games.getItemAtPosition(position);
                Integer gameID = Integer.parseInt(games[position][0]);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                loadGame(gameID);
            }
        });
    }
    //Starten des Dialoges SelectPlayer
    public void loadGame (Integer gameID) {

        Intent intent = new Intent(this, Game.class);
        //GameID übergeben
        intent.putExtra("GameID",gameID);
        //Starte Activity Spiel
        startActivity(intent);
    }
}
