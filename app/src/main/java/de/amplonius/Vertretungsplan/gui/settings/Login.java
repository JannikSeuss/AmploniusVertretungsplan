package de.amplonius.Vertretungsplan.gui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.jsoup.nodes.Document;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.Downloader;
import de.amplonius.Vertretungsplan.gui.About;
import de.amplonius.Vertretungsplan.gui.Help;
import de.amplonius.Vertretungsplan.gui.LoadingPage;
import de.amplonius.Vertretungsplan.values.Values;

public class Login extends Activity {

    private EditText loginEdit;
    private EditText passwortEdit;

    private String loginTmp;
    private String passwortTmp;

    private SharedPreferences settings;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.settings_login);
        settings = getSharedPreferences(Values.SettingsName, 0);

        loginEdit = (EditText) findViewById(R.id.login_edit);
        passwortEdit = (EditText) findViewById(R.id.passwort_edit);

        String login;
        String passwort;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            login = extras.getString("login");
            passwort = extras.getString("passwort");
        } else {
            login = settings.getString("login", "");
            passwort = settings.getString("passwort", "");
        }

        if (login != "" && passwort != "") {
            loginEdit.setText(login);
            passwortEdit.setText(passwort);
        }
    }

    public void onClickLoginButton(View v) {

        loginTmp = loginEdit.getText().toString();
        passwortTmp = passwortEdit.getText().toString();

        testLogin(loginTmp, passwortTmp);

    }

    private void restart(String login, String passwort) {
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("login", login);
        intent.putExtra("passwort", passwort);
        startActivity(intent);
    }

    public void testLogin(String login, String password) {

        Downloader.DownloadCompleteListener dcl = new Downloader.DownloadCompleteListener() {

            @Override
            public void onDownloadComplete(Document result) {
                if (result == Values.errorPage) {
                    new AlertDialog.Builder(context).setTitle("Fehlerhafter Login")
                            .setMessage("Die Angaben sind nicht korrekt! \n Bitte erneut eingeben")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    restart(loginTmp, passwortTmp);
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();

                } else {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("login", loginTmp);
                    editor.putString("passwort", passwortTmp);
                    editor.commit();

                    gotoDownloader();
                }
            }
        };

        String loginpassword = login + ":" + password;
        String authentication = new String(Base64.encode(loginpassword.getBytes(), 0));

        String[] parameter = new String[2];

        parameter[0] = Values.lnkVertretungMethode()[1];
        parameter[1] = authentication;

        Downloader downloader = new Downloader(dcl);
        downloader.execute(parameter);

    }

    private void gotoDownloader() {
        Intent i = new Intent(this, LoadingPage.class);
        startActivity(i);
    }

    // Optionsmenue
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_login_menue, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.menu_help:
                Intent help = new Intent(this, Help.class);
                help.putExtra("which", 1);
                startActivity(help);
                return true;

            case R.id.menu_about:
                Intent about = new Intent(this, About.class);
                startActivity(about);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
