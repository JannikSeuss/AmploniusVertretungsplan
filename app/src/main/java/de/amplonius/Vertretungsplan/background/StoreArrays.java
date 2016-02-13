package de.amplonius.Vertretungsplan.background;

import android.content.Context;
import android.content.SharedPreferences;

import de.amplonius.Vertretungsplan.values.Values;

public class StoreArrays {

    private Context context;

    public StoreArrays(Context context) {
        this.context = context;
    }

    public void saveArray(String[] array, String arrayName) {
        SharedPreferences prefs = context.getSharedPreferences(Values.SettingsName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++) {
            editor.putString(arrayName + "_" + i, array[i]);
        }
        editor.commit();
    }

    public String[] loadArray(String arrayName) {
        SharedPreferences prefs = context.getSharedPreferences(Values.SettingsName, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = prefs.getString(arrayName + "_" + i, null);
        }
        return array;
    }
}
