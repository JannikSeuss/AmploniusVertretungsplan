package de.amplonius.Vertretungsplan.gui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import de.amplonius.Vertretungsplan.R;
import de.amplonius.Vertretungsplan.notification.CreateAlarmManager;
import de.amplonius.Vertretungsplan.notification.SetNotification;
import de.amplonius.Vertretungsplan.values.Values;

public class Notification extends Activity {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    TimePicker picker;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_notification);

        context = this;
        settings = getSharedPreferences(Values.SettingsName, 0);
        editor = settings.edit();
        editor.commit();

        Switch notificationSwitch = (Switch) findViewById(R.id.enable_notification_switch);
        notificationSwitch.setChecked(settings.getBoolean("notification", false));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new SetNotification(context).enableNotification();
                    createService();
                } else {
                    new SetNotification(context).disableNotification();
                    createService();
                }
            }
        });

        Switch offsetSwitch = (Switch) findViewById(R.id.offset);
        offsetSwitch.setChecked(settings.getInt("offset", 0) == 1);
        offsetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putInt("offset", 1);
                    editor.commit();
                } else {
                    editor.putInt("offset", 0);
                    editor.commit();
                }
            }
        });

        picker = (TimePicker) findViewById(R.id.settings_notifiction_time_picker);
        picker.setIs24HourView(true);
        picker.setCurrentHour(settings.getInt("notification_hour", 6));
        picker.setCurrentMinute(settings.getInt("notification_minute", 0));

    }

    public void onClickSetTime(View v) {
        editor.putInt("notification_hour", picker.getCurrentHour());
        editor.putInt("notification_minute", picker.getCurrentMinute());
        editor.commit();
        Toast.makeText(this, "Uhrzeit gesetzt", Toast.LENGTH_SHORT).show();
        createService();
    }

    private void createService() {
        Intent i = new Intent(this, CreateAlarmManager.class);
        startService(i);
    }

}