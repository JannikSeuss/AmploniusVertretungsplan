package de.amplonius.Vertretungsplan.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.gui.settings.Settings;

public class About extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

    }

    // Optionsmenue
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
