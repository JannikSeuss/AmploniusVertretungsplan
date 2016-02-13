package de.amplonius.Vertretungsplan.gui.settings;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.StoreArrays;
import de.amplonius.Vertretungsplan.values.Values;

public class KursMarkierungen extends Activity {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private EditText stufeEdit;
    private EditText kursEdit;

    String[] kurseSettings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_kurs_markierungen);

        context = this;

        settings = getSharedPreferences(Values.SettingsName, 0);
        editor = settings.edit();

        String stufe = settings.getString("klasse", "");

        kursEdit = (EditText) findViewById(R.id.kurs_hinzufuegen_edit);
        stufeEdit = (EditText) findViewById(R.id.eigene_stufe);
        stufeEdit.setText(stufe);

        Switch stufeAusb = (Switch) findViewById(R.id.switch_stufe_ausb);
        stufeAusb.setChecked(settings.getBoolean("reiheAusblenden", false));
        stufeAusb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("reiheAusblenden", true);
                    editor.commit();
                } else {
                    editor.putBoolean("reiheAusblenden", false);
                    editor.commit();
                }
            }
        });

        Switch kurseMark = (Switch) findViewById(R.id.switch_kurse_mark);
        kurseMark.setChecked(settings.getBoolean("kursHighlight", false));
        kurseMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("kursHighlight", true);
                    editor.commit();
                } else {
                    editor.putBoolean("kursHighlight", false);
                    editor.commit();
                }
            }

        });

        fillTable();
    }

    public void fillTable() {

        TableLayout tl = (TableLayout) findViewById(R.id.aktive_kurse);
        tl.removeAllViews();
        kurseSettings = new StoreArrays(this).loadArray("kurseSettings");

        for (int i = 0; i <= kurseSettings.length - 1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            TextView kursView = new TextView(this);
            kursView.setTextSize(18);
            kursView.setText(kurseSettings[i]);
            kursView.setTextColor(Color.BLACK);
            kursView.setPadding(10, 5, 10, 5);
            tr.addView(kursView);

            ImageView cancelButton = new ImageView(this);
            cancelButton.setId(100 + i);
            cancelButton.setBackgroundResource(android.R.drawable.ic_delete);
            cancelButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    final int rowId = arg0.getId() - 100;

                    new AlertDialog.Builder(context).setTitle("Löschen")
                            .setMessage("Kurs \"" + kurseSettings[rowId] + "\" wirklich entfernen?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] kurseSettingsNew = new String[kurseSettings.length - 1];

                                    int i;
                                    for (i = 0; i < rowId; i++) {
                                        kurseSettingsNew[i] = kurseSettings[i];
                                    }

                                    for (; i <= kurseSettingsNew.length - 1; i++) {
                                        kurseSettingsNew[i] = kurseSettings[i + 1];
                                    }

                                    new StoreArrays(context).saveArray(kurseSettingsNew, "kurseSettings");
                                    fillTable();
                                }
                            }).setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert).show();

                }
            });
            tr.addView(cancelButton);

            tl.addView(tr);
        }
    }

    public void onClickHinzufuegen(View v) {
        String[] kurseSettings = new StoreArrays(this).loadArray("kurseSettings");
        String[] kurseSettingsNew = new String[kurseSettings.length + 1];
        Log.v(this.getClass().getSimpleName(), "onClickHinzufuegen");
        int i;

        for (i = 0; i <= kurseSettings.length - 1; i++) {
            kurseSettingsNew[i] = kurseSettings[i];
        }

        kurseSettingsNew[i] = kursEdit.getText().toString();

        new StoreArrays(this).saveArray(kurseSettingsNew, "kurseSettings");

        fillTable();

        kursEdit.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        editor.putString("klasse", stufeEdit.getText().toString());
        editor.commit();
        Toast.makeText(this, "Gespeichert", Toast.LENGTH_SHORT).show();
    }

}
