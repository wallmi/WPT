package com.wallner.michael.wpt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wallner.michael.wpt.db.WPTDataSource;

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
        lv_games = findViewById(R.id.lv_games);

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
                //Toast.makeText(getApplicationContext(),
                //        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                //      .show();

                loadGame(gameID);
            }
        });

        // ListView Item LongClick Listener
        lv_games.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                Integer gameID = Integer.parseInt(games[position][0]);
                WPTDataSource db = new WPTDataSource(getBaseContext());
                db.open();

                String gameName = db.getGameName(gameID);

                deleteGame(gameID);

                Toast.makeText(getBaseContext(),"Spiel " + gameName + " wurde gelöscht",
                        Toast.LENGTH_LONG).show();

                db.close();
                finish();
                startActivity(getIntent());
                return true;
            }
        });
    }
    //Starten des Dialoges SelectPlayer
    public void loadGame (Integer gameID) {

        WPTDataSource db = new WPTDataSource(getBaseContext());
        Intent intent = new Intent(this, Game.class);
        db.open();

        intent.putExtra("GameID",gameID);
        intent.putExtra("GameName", db.getGameName(gameID));
        db.close();

        startActivity(intent);

    }

    public void deleteGame(Integer gameID){
        WPTDataSource db = new WPTDataSource(this);
        db.open();
        db.deleteGame(gameID);
        db.close();
    }
}
