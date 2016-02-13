package de.amplonius.Vertretungsplan.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.Counter;
import de.amplonius.Vertretungsplan.gui.settings.Login;
import de.amplonius.Vertretungsplan.values.Values;

public class FirstPage extends Activity {

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(Values.SettingsName, 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if (firstStart) {
            Log.v(this.getClass().getSimpleName(), "first start");
            setContentView(R.layout.first_page_layout);

        } else {
            Intent i = new Intent(this, LoadingPage.class);
            startActivity(i);
        }
    }

    public void onClickAcceptButton(View v) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstStart", false);
        editor.commit();

        new Counter(Values.counter()[0]);

        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

}