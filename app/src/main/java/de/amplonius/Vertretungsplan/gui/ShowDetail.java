package de.amplonius.Vertretungsplan.gui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.gui.settings.Settings;

@SuppressLint("ResourceAsColor")
public class ShowDetail extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_details);
        Log.v(this.getClass().getSimpleName(), "onCreate");

        Bundle extras = getIntent().getExtras();
        String klasse = extras.getString("klasse");
        String stunde = extras.getString("stunde");
        String lehrer = extras.getString("lehrer");
        String fach = extras.getString("fach");
        String vertreter = extras.getString("vertreter");
        String raum = extras.getString("raum");
        String bemerkung = extras.getString("bemerkung");
        String art = extras.getString("art");

        TextView klasseView = (TextView) findViewById(R.id.klasse);
        klasseView.setText(klasse);

        TextView stundeView = (TextView) findViewById(R.id.stunde);
        stundeView.setText(stunde);

        TextView lehrerView = (TextView) findViewById(R.id.lehrer);
        lehrerView.setText(lehrer);

        TextView fachView = (TextView) findViewById(R.id.fach);
        fachView.setText(fach);

        TextView vertreterView = (TextView) findViewById(R.id.vertreter);
        vertreterView.setText(vertreter);

        TextView raumView = (TextView) findViewById(R.id.raum);
        raumView.setText(raum);

        if (bemerkung.length() > 1) {
            TextView bemerkungLabelView = (TextView) findViewById(R.id.bemerkungLabel);
            bemerkungLabelView.setTextColor(Color.RED);
        }

        TextView bemerkungView = (TextView) findViewById(R.id.bemerkung);
        bemerkungView.setText(bemerkung);

        TextView artView = (TextView) findViewById(R.id.art);
        artView.setText(art);

    }

    // Optionsmenue
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_table_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_refresh:
                Intent refresh = new Intent(this, LoadingPage.class);
                startActivity(refresh);
                return true;

            case R.id.menu_settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;

            case R.id.menu_about:
                Intent about = new Intent(this, About.class);
                startActivity(about);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}