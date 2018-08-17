package com.wallner.michael.wpt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //Ressourcen laden
    }

    //Neues Spiel starten
    public void startNewGame (View view) {
        Intent intent = new Intent(this, NewGame.class);
        startActivity(intent);
    }

    //Ein Spiel zum Laden auswählen
    public void loadGame (View view) {
        Intent intent = new Intent(this, LoadGame.class);
        startActivity(intent);
    }

    public void Github(View view) {
        openUrl("https://github.com/wallmi/WPT");
    }

    public void reportIssue(View view) {
        openUrl("https://github.com/wallmi/WPT/issues/new");
    }

    private void openUrl (String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }
}
