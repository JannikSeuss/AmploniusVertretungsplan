package de.amplonius.Vertretungsplan.background;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;

import org.jsoup.nodes.Document;

import de.amplonius.Vertretungsplan.values.Values;

public class DownloadManager {

    private SharedPreferences settings;
    public int count;
    private String[] urls = Values.lnkVertretungMethode();
    private String url;
    public Bundle informationen = new Bundle();
    public String[] datumString = new String[20];

    public void getIntent(SharedPreferences settings) {
        this.settings = settings;

        count = 1;
        downloadLoop();

    }

    private void createIntent() {
        dc.onDownloadComplete(datumString, count, informationen);

    }

    private void downloadLoop() {
        if (count <= 19) {
            url = urls[count];
            startDownload();
        } else {
            createIntent();
        }

    }

    private void startDownload() {

        Downloader.DownloadCompleteListener dcl = new Downloader.DownloadCompleteListener() {

            @Override
            public void onDownloadComplete(Document result) {

                if (!result.equals(Values.errorPage)) {

                    DownloadSortPlan srt = new DownloadSortPlan();
                    Bundle uebergabe = srt.sort(result, count);
                    datumString[count] = srt.titleString;

                    werteSpeichern(uebergabe, count);
                    count++;
                    downloadLoop();

                } else {
                    createIntent();
                }
            }

            private void werteSpeichern(Bundle uebergabe, int count) {
                String seite = ("seite" + String.valueOf(count));
                informationen.putBundle(seite, uebergabe);

            }

        };

        String username = settings.getString("login", "");
        String password = settings.getString("passwort", "");
        String login = username + ":" + password;
        String authentication = new String(Base64.encode(login.getBytes(), 0));

        String[] parameter = new String[2];

        parameter[0] = url;
        parameter[1] = authentication;

        Downloader downloader = new Downloader(dcl);
        downloader.execute(parameter);

    }

    public static interface DownloadCompleteListener {
        void onDownloadComplete(String[] datumString, int count, Bundle informationen);
    }

    private DownloadCompleteListener dc = null;

    public DownloadManager(DownloadCompleteListener dc) {
        this.dc = dc;
    }

}
