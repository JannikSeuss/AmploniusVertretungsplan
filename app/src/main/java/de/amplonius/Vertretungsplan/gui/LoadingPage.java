package de.amplonius.Vertretungsplan.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.nodes.Document;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.Counter;
import de.amplonius.Vertretungsplan.background.CreateDialog;
import de.amplonius.Vertretungsplan.background.DownloadManager;
import de.amplonius.Vertretungsplan.background.Downloader;
import de.amplonius.Vertretungsplan.values.Values;

public class LoadingPage extends Activity {

    private SharedPreferences settings;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.downloader_init);

        Downloader.DownloadCompleteListener ddcl = new Downloader.DownloadCompleteListener() {

            @Override
            public void onDownloadComplete(Document result) {
                if (!result.text().equals("1")) {
                    settings = getSharedPreferences(Values.SettingsName, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("D");
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String datum = sdf.format(time);
                    String datumSettings = settings.getString("datum", "");
                    if (!datumSettings.equals(datum)) {
                        new Counter(Values.counter()[3]);
                        Log.v(this.getClass().getSimpleName(), datumSettings);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("datum", datum);
                        editor.commit();
                        Log.v(this.getClass().getSimpleName(), datum);
                    }

                    new Counter(Values.counter()[2]);

                    DownloadManager.DownloadCompleteListener dcl = new DownloadManager.DownloadCompleteListener() {

                        @Override
                        public void onDownloadComplete(String[] datumString, int count, Bundle informationen) {
                            gotoShowTable(datumString, count, informationen);

                        }
                    };

                    DownloadManager dlm = new DownloadManager(dcl);
                    dlm.getIntent(settings);
                } else {
                    new CreateDialog().dialogOK("App deaktiviert",
                            "Die App wurde deaktiviert. Vermutlich ist ein neues Update auf Amplonius.de verfübar",
                            context);
                }
            }
        };

        String authentication = "";

        String[] parameter = new String[2];

        parameter[0] = Values.enable;
        parameter[1] = authentication;

        Downloader downloader = new Downloader(ddcl);
        downloader.execute(parameter);

    }

    private void gotoShowTable(String[] datumString, int count, Bundle informationen) {
        Intent intent = new Intent(this, ShowTable.class);
        intent.putExtra("datum", datumString);
        intent.putExtra("anzahl", count);
        intent.putExtra("informationen", informationen);
        startActivity(intent);
    }

}
