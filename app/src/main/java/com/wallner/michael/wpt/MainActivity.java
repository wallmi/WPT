package com.wallner.michael.wpt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView2) TextView txView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //Ressourcen laden
        ButterKnife.bind(this);

        txView2.setText(String.format(Locale.getDefault(),
                "%s%s", txView2.getText(), BuildConfig.VERSION_NAME));
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
        openUrl(getString(R.string.git_link));
    }

    public void reportIssue(View view) {
        openUrl(getString(R.string.report_bug));

    }

    private void openUrl (String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }
}
