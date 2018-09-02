package com.wallner.michael.wpt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView2) TextView txView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //Ressourcen laden
        ButterKnife.bind(this);

        txView2.setText(txView2.getText()+ BuildConfig.VERSION_NAME);

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

        //Intent i = new Intent(Intent.ACTION_SEND);
        //i.setType("message/rfc822");
        //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"wpt@wallner-michael.at"});
        //i.putExtra(Intent.EXTRA_SUBJECT, "Wizard Point Table");
        //i.putExtra(Intent.EXTRA_TEXT   , "Please Descripe your Problem or Feature Request..");
        //try {
            //    startActivity(Intent.createChooser(i, "Send mail..."));
            //} catch (android.content.ActivityNotFoundException ex) {
        //    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        //}
        //openUrl("https://github.com/wallmi/WPT/issues/new");
        openUrl("mailto:wpt@wallner-michael.at?subject=Wizard%20Point%20Table");

    }

    private void openUrl (String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }
}
