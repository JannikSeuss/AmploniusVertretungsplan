package de.amplonius.Vertretungsplan.background;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import de.amplonius.Vertretungsplan.values.Values;

public class Downloader extends AsyncTask<String[], Void, Document> {

    @Override
    protected Document doInBackground(String[]... parameter) {
        Document response = null;

        for (String[] param : parameter) {
            response = downloadWebPage(param[0], param[1]);
        }
        return response;
    }

    private Document downloadWebPage(String url, String authentication) {
        try {

            Document doc = Jsoup.connect(url).header("Authorization", "Basic " + authentication).get();
            return doc;
        } catch (IOException io) {
            if (!url.contains("bit.ly")) {
                Log.w("Downloader", "Couldn't download " + url);
            }
            return Values.errorPage;
        }
    }

    public static interface DownloadCompleteListener {
        void onDownloadComplete(Document result);
    }

    private DownloadCompleteListener dc = null;

    public Downloader(DownloadCompleteListener dc) {
        this.dc = dc;
    }

    @Override
    protected void onPostExecute(Document result) {
        dc.onDownloadComplete(result);
    }
}
