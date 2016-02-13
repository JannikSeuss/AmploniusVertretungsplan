package de.amplonius.Vertretungsplan.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.background.Counter;
import de.amplonius.Vertretungsplan.background.DownloadManager;
import de.amplonius.Vertretungsplan.background.StoreArrays;
import de.amplonius.Vertretungsplan.background.showTableHelp.DatumSort;
import de.amplonius.Vertretungsplan.gui.LoadingPage;
import de.amplonius.Vertretungsplan.values.Values;

public class CreateNotification extends Service {

    private SharedPreferences settings;
    private Context context;

    @Override
    public void onCreate() {
        context = this;
        Log.v(this.getClass().getSimpleName(), System.currentTimeMillis() + ": Service erstellt.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(this.getClass().getSimpleName(), System.currentTimeMillis() + ": Service gestartet.");

        settings = getSharedPreferences(Values.SettingsName, 0);

        if (settings.getBoolean("notification", false)) {
            aktuelleVertretungen();
        }

        stopSelf();
        return START_STICKY;
    }

    private void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Vertretungsplan").setContentText("Neue Vertretungen!");

        Intent resultIntent = new Intent(this, LoadingPage.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoadingPage.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1000, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        Log.v(this.getClass().getSimpleName(), System.currentTimeMillis() + ": Service zerstoert.");
        Intent i = new Intent(this, CreateAlarmManager.class);
        startService(i);
    }

    private void aktuelleVertretungen() {
        new Counter(Values.counter()[4]);

        DownloadManager.DownloadCompleteListener dcl = new DownloadManager.DownloadCompleteListener() {

            @Override
            public void onDownloadComplete(String[] datumString, int count, Bundle informationen) {
                try {
                    String[] datumSorted = new DatumSort().datumSort(count, datumString);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Timestamp time = new Timestamp(
                            System.currentTimeMillis() + (settings.getInt("offset", 0) * DateUtils.DAY_IN_MILLIS));
                    String datum = sdf.format(time);
                    String[] datumNowStr = datum.split("\\.");
                    int[] datumNow = new int[3];
                    datumNow[0] = Integer.parseInt(datumNowStr[0]);
                    datumNow[1] = Integer.parseInt(datumNowStr[1]);
                    datumNow[2] = Integer.parseInt(datumNowStr[2]);

                    int i;
                    for (i = 0; i < datumSorted.length; i++) {

                        String[] arr = datumSorted[i].split("\\.");
                        arr[2] = arr[2].substring(0, 4);

                        int arr0 = Integer.parseInt(arr[0]);
                        int arr1 = Integer.parseInt(arr[1]);
                        int arr2 = Integer.parseInt(arr[2]);

                        if (arr0 == datumNow[0] && arr1 == datumNow[1] && arr2 == datumNow[2]) {
                            String welcheSeite = "seite" + String.valueOf(i + 1);

                            Bundle information = informationen.getBundle(welcheSeite);

                            String[] klasse = information.getStringArray("klasse");
                            String[] fach = information.getStringArray("fach");

                            String[] kurseSettings = new StoreArrays(context).loadArray("kurseSettings");
                            String klasseSettings = settings.getString("klasse", "");

                            int g = 1;
                            boolean notif = false;
                            while (klasse[g] != null) {

                                if ((klasse[g].equals(klasseSettings) | klasseSettings == "")
                                        && (Arrays.asList(kurseSettings).contains(fach[g])
                                        | kurseSettings.length == 0)) {
                                    notif = true;
                                }

                                g++;
                            }
                            if (notif) {
                                createNotification();
                            }
                        }
                    }

                } catch (NullPointerException e) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher).setContentTitle("Vertretungsplan")
                            .setContentText("Verbindungsfehler");

                    Intent resultIntent = new Intent(context, LoadingPage.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addParentStack(LoadingPage.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(
                            Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1000, mBuilder.build());
                }

            }
        };

        DownloadManager dlm = new DownloadManager(dcl);
        dlm.getIntent(settings);
    }

}
