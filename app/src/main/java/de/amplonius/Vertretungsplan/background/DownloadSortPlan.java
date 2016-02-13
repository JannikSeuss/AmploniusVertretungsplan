package de.amplonius.Vertretungsplan.background;

import android.os.Bundle;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadSortPlan {
    public String titleString;

    public Bundle sort(Document result, int count) {
        Element title = result.select("div.mon_title").first();
        Element list = result.select("table.mon_list").first();

        Elements rows = list.select("tr");

        String titleString = title.html();

        int i = 0;
        int field = 0;
        String[] klasse = new String[100];
        String[] stunde = new String[100];
        String[] lehrer = new String[100];
        String[] fach = new String[100];
        String[] vertreter = new String[100];
        String[] raum = new String[100];
        String[] bemerkung = new String[100];
        String[] art = new String[100];

        Bundle uebergabe = new Bundle();

        this.titleString = titleString;

        for (Element row : rows) {
            Elements fields = row.select("td");

            field = 0;

            for (Element el : fields) {

                String kurz = el.text();

                switch (field) {

                    case 0:
                        klasse[i] = kurz;
                        break;

                    case 1:
                        stunde[i] = kurz;
                        break;

                    case 2:
                        vertreter[i] = kurz;
                        break;

                    case 3:
                        fach[i] = kurz;
                        break;

                    case 4:
                        raum[i] = kurz;
                        break;

                    case 5:
                        lehrer[i] = kurz;
                        break;

                    case 8:
                        art[i] = kurz;
                        break;

                    case 9:
                        bemerkung[i] = kurz;
                        break;

                }

                field++;

            }
            i++;
        }
        String[] info = new String[100];
        int infoCnt = 0;
        try {
            Elements infosRows = result.select("table.info").first().select("tr");

            for (Element infosRow : infosRows) {
                Elements fields = infosRow.select("td");

                for (Element el : fields) {
                    if (info[infoCnt] != null) {
                        info[infoCnt] = info[infoCnt] + "   " + el.text();
                    } else {
                        info[infoCnt] = el.text();
                    }
                }
                infoCnt++;
            }
        } catch (NullPointerException e) {
        }

        uebergabe.putStringArray("klasse", klasse);
        uebergabe.putStringArray("stunde", stunde);
        uebergabe.putStringArray("lehrer", lehrer);
        uebergabe.putStringArray("fach", fach);
        uebergabe.putStringArray("vertreter", vertreter);
        uebergabe.putStringArray("raum", raum);
        uebergabe.putStringArray("bemerkung", bemerkung);
        uebergabe.putStringArray("art", art);
        uebergabe.putStringArray("info", info);

        return uebergabe;
    }
}
