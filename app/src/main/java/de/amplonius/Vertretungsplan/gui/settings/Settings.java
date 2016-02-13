package de.amplonius.Vertretungsplan.gui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.Counter;
import de.amplonius.Vertretungsplan.background.CreateDialog;
import de.amplonius.Vertretungsplan.values.Values;

public class Settings extends Activity {

    private SharedPreferences settings;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        context = this;
        settings = getSharedPreferences(Values.SettingsName, 0);

    }

    public void onClickLogin(View v) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void onClickKurse(View v) {
        Intent i = new Intent(this, KursMarkierungen.class);
        startActivity(i);
    }

    public void onClickNotification(View v) {
        Intent i = new Intent(this, Notification.class);
        startActivity(i);
    }

    public void onClickReset(View v) {

        new AlertDialog.Builder(this).setTitle("Reset").setMessage("Wirklich alle Einstellungen zurücksetzten ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterf, int which) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.commit();
                        new Counter(Values.counter()[1]);
                        CreateDialog dialog = new CreateDialog();
                        dialog.dialogOK("RESET", "Die Einstellungen wurden zurückgesetzt", context);
                        Log.w(this.getClass().getSimpleName(), "Settings Reset");
                    }
                }).setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_alert).show();


    }

}
