package de.amplonius.Vertretungsplan.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.Calendar;

import de.amplonius.Vertretungsplan.values.Values;

public class CreateAlarmManager extends Service {

    @Override
    public void onCreate() {

        SharedPreferences settings = getSharedPreferences(Values.SettingsName, 0);

        Intent serviceIntent = new Intent(this, CreateNotification.class);
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);


        Calendar c = Calendar.getInstance();
        int hourNow = c.get(Calendar.HOUR_OF_DAY);
        int minuteNow = c.get(Calendar.MINUTE);

        int hourSet = settings.getInt("notification_hour", 6);
        int minuteSet = settings.getInt("notification_minute", 0);

        long delayFirstStart;

        if (hourNow <= hourSet) {
            delayFirstStart = (hourSet - hourNow) * DateUtils.HOUR_IN_MILLIS;
        } else {
            delayFirstStart = DateUtils.DAY_IN_MILLIS + ((hourSet - hourNow) * DateUtils.HOUR_IN_MILLIS);
        }

        delayFirstStart = delayFirstStart + ((minuteSet - minuteNow) * DateUtils.MINUTE_IN_MILLIS);

        long firstStart = System.currentTimeMillis() + delayFirstStart;

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, firstStart, servicePendingIntent);

        Log.v(this.getClass().getSimpleName(), "AlarmManager gesetzt");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
