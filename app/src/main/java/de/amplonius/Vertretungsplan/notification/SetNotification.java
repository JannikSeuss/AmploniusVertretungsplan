package de.amplonius.Vertretungsplan.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import de.amplonius.Vertretungsplan.values.Values;

public class SetNotification {

    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public SetNotification(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(Values.SettingsName, 0);
        editor = settings.edit();

    }

    public void enableNotification() {
        // enable Alarm
        editor.putBoolean("notification", true);
        editor.commit();
        ComponentName receiver = new ComponentName(context, NotificationReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent i = new Intent(context, CreateAlarmManager.class);
        context.startService(i);
    }

    public void disableNotification() {
        // disable alarm
        editor.putBoolean("notification", false);
        editor.commit();
        ComponentName receiver = new ComponentName(context, NotificationReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent i = new Intent(context, CreateAlarmManager.class);
        context.startService(i);
    }
}
