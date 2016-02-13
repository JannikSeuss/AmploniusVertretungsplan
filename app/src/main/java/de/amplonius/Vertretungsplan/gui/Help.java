package de.amplonius.Vertretungsplan.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.gui.settings.Settings;

public class Help extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(this.getClass().getSimpleName(), "onCreate");
        setContentView(R.layout.help_layout);

        Bundle extras = getIntent().getExtras();
        int which = 0;

        if (extras != null) {
            which = extras.getInt("which");
        }

        WebView webView = (WebView) findViewById(R.id.helpWebView);
        initialisiereWebKit(which, webView, this);
        webView.bringToFront();
    }

    private void initialisiereWebKit(int which, WebView view, Context context) {
        final String mimetype = "text/html";
        final String encoding = "UTF-8";
        String htmldata;
        InputStream is;

        switch (which) {
            case 1:
                is = context.getResources().openRawResource(R.raw.settings_login_help);
                break;

            default:
                is = context.getResources().openRawResource(R.raw.hilfe);
                break;
        }

        try {
            if (is != null && is.available() > 0) {
                final byte[] bytes = new byte[is.available()];
                is.read(bytes);
                htmldata = new String(bytes);
                view.loadDataWithBaseURL(null, htmldata, mimetype, encoding, null);

            }
        } catch (IOException e) {
        }
    }

    // Optionsmenue
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;

            case R.id.menu_about:
                Intent i = new Intent(this, About.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
