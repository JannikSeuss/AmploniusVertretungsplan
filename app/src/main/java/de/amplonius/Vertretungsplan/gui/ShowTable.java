package de.amplonius.Vertretungsplan.gui;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.StoreArrays;
import de.amplonius.Vertretungsplan.background.showTableHelp.DatumSort;
import de.amplonius.Vertretungsplan.gui.settings.Settings;
import de.amplonius.Vertretungsplan.values.Values;

@SuppressLint("ResourceAsColor")
public class ShowTable extends Activity {

    private SharedPreferences settings;

    private int rowcounter;
    private String[] klasse;
    private String[] stunde;
    private String[] lehrer;
    private String[] fach;
    private String[] vertreter;
    private String[] raum;
    private String[] bemerkung;
    private String[] art;
    private String[] info;
    private Bundle informationen;
    private int anzahl;
    private TableLayout tl;
    private TableRow trTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(this.getClass().getSimpleName(), "onCreate");
        setContentView(R.layout.show_table);

        settings = getSharedPreferences(Values.SettingsName, 0);

        trTitle = (TableRow) findViewById(R.id.tableTitle);

        Bundle extras = getIntent().getExtras();
        String[] datum = extras.getStringArray("datum");
        anzahl = extras.getInt("anzahl");
        informationen = extras.getBundle("informationen");

        String welcheSeite = "seite1";

        try {
            werteZuweisen(welcheSeite);
            tabelleErstellen();

            String[] datumSorted = new DatumSort().datumSort(anzahl, datum);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    datumSorted);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = ((Spinner) this.findViewById(R.id.datumAuswahl));
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position <= anzahl) {
                        String welcheSeite = new String("seite" + String.valueOf(position + 1));

                        werteZuweisen(welcheSeite);

                        rowcounter = 0;
                        tabelleErstellen();

                    } else {
                        Log.e(this.getClass().getSimpleName(), "onItemSelected > anzahl");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });

        } catch (NullPointerException e) {
            new AlertDialog.Builder(this).setTitle("Download fehlgeschlagen").setMessage(R.string.ShowTableKeinInternet)
                    .setNegativeButton(R.string.Erneut, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            retry();
                        }
                    }).setPositiveButton(R.string.EinstellungenOeffnen, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotoSettings();

                }

            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }

    private void tabelleErstellen() {
        TextView empty;

        tl = (TableLayout) findViewById(R.id.table_layout);
        tl.removeAllViews();

        tl.addView(trTitle);

        if (klasse[0] == null) {
            int count = 1;
            while (klasse[count] != null) {

                makeNewRow(count, tl);

                count++;
            }
        } else {

            TextView keineVertretungen = new TextView(this);
            keineVertretungen.setText("Keine Vertretungen");
            keineVertretungen.setPadding(20, 20, 0, 0);
            keineVertretungen.setTextColor(Color.RED);
            keineVertretungen.setTextSize(17);
            tl.addView(keineVertretungen);
        }

        LinearLayout infoLayout = (LinearLayout) findViewById(R.id.aktuelle_info_layout);

        infoLayout.removeAllViews();

        if (info[1] != null) {

            TextView infoTitleTV = new TextView(this);
            infoTitleTV.setText("Aktuelle Informationen:");
            infoTitleTV.setTypeface(null, Typeface.BOLD);
            infoTitleTV.setTextColor(Color.BLACK);
            infoTitleTV.setTextSize(17);
            infoLayout.addView(infoTitleTV);

            empty = new TextView(this);
            empty.setText("");
            infoLayout.addView(empty);

            for (int infoCnt = 1; info[infoCnt] != null; infoCnt++) {
                TextView infoTV = new TextView(this);
                infoTV.setTextSize(17);
                infoTV.setWidth(LayoutParams.MATCH_PARENT);
                infoTV.setTextColor(Color.BLACK);
                infoTV.setText(info[infoCnt]);
                infoLayout.addView(infoTV);
            }

        }
    }

    private void werteZuweisen(String welcheSeite) {
        Bundle information = informationen.getBundle(welcheSeite);

        klasse = information.getStringArray("klasse");
        stunde = information.getStringArray("stunde");
        lehrer = information.getStringArray("lehrer");
        fach = information.getStringArray("fach");
        vertreter = information.getStringArray("vertreter");
        raum = information.getStringArray("raum");
        bemerkung = information.getStringArray("bemerkung");
        art = information.getStringArray("art");
        info = information.getStringArray("info");
    }

    public void makeNewRow(int count, TableLayout tl) {

        boolean reiheAusblenden = settings.getBoolean("reiheAusblenden", false);
        boolean kursHighlight = settings.getBoolean("kursHighlight", false);
        String[] kurseSettings = new StoreArrays(this).loadArray("kurseSettings");
        String klasseSettings = settings.getString("klasse", "");

        if (!reiheAusblenden | klasse[count].equals(klasseSettings)) {

            rowcounter++;

            TableRow tr = new TableRow(this);
            tr.setId(100 + rowcounter);

            if (kursHighlight && Arrays.asList(kurseSettings).contains(fach[count])) {
                if (klasseSettings.equals("") | klasse[count].equals(klasseSettings)) {
                    tr.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }

            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            tr.setClickable(true);
            tr.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    int rowId = arg0.getId() - 100;

                    Bundle showDetailsBundle = new Bundle();

                    showDetailsBundle.putString("klasse", klasse[rowId]);
                    showDetailsBundle.putString("stunde", stunde[rowId]);
                    showDetailsBundle.putString("lehrer", lehrer[rowId]);
                    showDetailsBundle.putString("fach", fach[rowId]);
                    showDetailsBundle.putString("vertreter", vertreter[rowId]);
                    showDetailsBundle.putString("raum", raum[rowId]);
                    showDetailsBundle.putString("bemerkung", bemerkung[rowId]);
                    showDetailsBundle.putString("art", art[rowId]);

                    startShowDetail(showDetailsBundle);
                }
            });

            TextView klasseView = new TextView(this);
            klasseView.setId(200 + rowcounter);
            klasseView.setText(klasse[count]);
            klasseView.setTextColor(Color.BLACK);
            klasseView.setPadding(10, 5, 10, 5);
            tr.addView(klasseView);

            TextView stundeView = new TextView(this);
            stundeView.setId(300 + rowcounter);
            stundeView.setText(stunde[count]);
            stundeView.setTextColor(Color.BLACK);
            stundeView.setPadding(10, 5, 10, 5);
            tr.addView(stundeView);

            TextView lehrerView = new TextView(this);
            lehrerView.setId(400 + rowcounter);
            lehrerView.setText(lehrer[count]);
            lehrerView.setTextColor(Color.BLACK);
            lehrerView.setPadding(10, 5, 10, 5);
            tr.addView(lehrerView);

            TextView artView = new TextView(this);
            artView.setId(500 + rowcounter);
            artView.setText(art[count]);
            artView.setTextColor(Color.BLACK);
            artView.setPadding(10, 5, 10, 5);
            tr.addView(artView);

            TextView bemerkungView = new TextView(this);
            bemerkungView.setId(900 + rowcounter);
            bemerkungView.setPadding(10, 5, 10, 5);

            if (bemerkung[count].length() <= 1) {
                bemerkungView.setText("-");
                bemerkungView.setTextColor(Color.BLACK);
            } else {
                bemerkungView.setText("!!!");
                bemerkungView.setTextColor(Color.RED);
            }
            tr.addView(bemerkungView);

            tl.addView(tr);
        }
    }

    private void startShowDetail(Bundle showDetailsBundle) {
        Intent newIntent = new Intent(this, ShowDetail.class);
        newIntent.putExtras(showDetailsBundle);
        startActivity(newIntent);
    }

    private void retry() {
        Intent downloaderInitIntent = new Intent(this, LoadingPage.class);
        startActivity(downloaderInitIntent);
    }

    private void gotoSettings() {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);

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
