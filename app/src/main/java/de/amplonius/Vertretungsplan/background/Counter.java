package de.amplonius.Vertretungsplan.background;

import org.jsoup.nodes.Document;

public class Counter {
    public Counter(String url) {
        Downloader.DownloadCompleteListener dcl = new Downloader.DownloadCompleteListener() {

            @Override
            public void onDownloadComplete(Document result) {
            }
        };

        String authentication = "";

        String[] parameter = new String[2];

        parameter[0] = url;
        parameter[1] = authentication;

        Downloader downloader = new Downloader(dcl);
        downloader.execute(parameter);

    }
}
