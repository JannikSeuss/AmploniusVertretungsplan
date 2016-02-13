package de.amplonius.Vertretungsplan.values;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Values {
    public static final String SettingsName = "Settings";

    public static String enable = "http://avp2016.bplaced.net/deac.html";

    public static String[] lnkVertretungMethode() {
        String[] lnkVertretung = new String[21];
        lnkVertretung[1] = "http://www.amplonius.de/vertretung/online/subst_001.htm";
        lnkVertretung[2] = "http://www.amplonius.de/vertretung/online/subst_002.htm";
        lnkVertretung[3] = "http://www.amplonius.de/vertretung/online/subst_003.htm";
        lnkVertretung[4] = "http://www.amplonius.de/vertretung/online/subst_004.htm";
        lnkVertretung[5] = "http://www.amplonius.de/vertretung/online/subst_005.htm";
        lnkVertretung[6] = "http://www.amplonius.de/vertretung/online/subst_006.htm";
        lnkVertretung[7] = "http://www.amplonius.de/vertretung/online/subst_007.htm";
        lnkVertretung[8] = "http://www.amplonius.de/vertretung/online/subst_008.htm";
        lnkVertretung[9] = "http://www.amplonius.de/vertretung/online/subst_009.htm";
        lnkVertretung[10] = "http://www.amplonius.de/vertretung/online/subst_010.htm";
        lnkVertretung[11] = "http://www.amplonius.de/vertretung/online/subst_011.htm";
        lnkVertretung[12] = "http://www.amplonius.de/vertretung/online/subst_012.htm";
        lnkVertretung[13] = "http://www.amplonius.de/vertretung/online/subst_013.htm";
        lnkVertretung[14] = "http://www.amplonius.de/vertretung/online/subst_014.htm";
        lnkVertretung[15] = "http://www.amplonius.de/vertretung/online/subst_015.htm";
        lnkVertretung[16] = "http://www.amplonius.de/vertretung/online/subst_016.htm";
        lnkVertretung[17] = "http://www.amplonius.de/vertretung/online/subst_017.htm";
        lnkVertretung[18] = "http://www.amplonius.de/vertretung/online/subst_018.htm";
        lnkVertretung[19] = "http://www.amplonius.de/vertretung/online/subst_019.htm";
        lnkVertretung[20] = "http://www.amplonius.de/vertretung/online/subst_020.htm";

        return lnkVertretung;
    }

    public static String[] counter() {
        String[] counter = new String[5];
        counter[0] = "http://bit.ly/1JxhQ1O";// Install
        counter[1] = "http://bit.ly/1Jxi0Xc";// Reset
        counter[2] = "http://bit.ly/1U6SeyD";// Load Use
        counter[3] = "http://bit.ly/1Ubjfkm";// Active User
        counter[4] = "http://bit.ly/1PJlo7T";// Notification

        return counter;
    }

    public final static Document errorPage = Jsoup.parse("<html><head><title>Error</title></head></html>");

}
