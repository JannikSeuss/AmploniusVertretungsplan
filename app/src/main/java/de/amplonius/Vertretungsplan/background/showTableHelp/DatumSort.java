package de.amplonius.Vertretungsplan.background.showTableHelp;

import android.util.Log;

public class DatumSort {

    public String[] datumSort(int anzahl, String[] datum) {
        String[] datumTemp = new String[anzahl - 1];

        int yearB = 0;
        int monthB = 0;
        int dayB = 0;
        int b = 0;

        for (int q = 0; q < anzahl - 1; q++) {
            datumTemp[q] = datum[q + 1];
        }
        for (int w = 0; w < anzahl - 1; w++) {
            if (datumTemp[w].contains(".")) {
                String[] arr = datumTemp[w].split("\\.");
                arr[2] = arr[2].substring(0, 4);

                int arr0 = Integer.parseInt(arr[0]);
                int arr1 = Integer.parseInt(arr[1]);
                int arr2 = Integer.parseInt(arr[2]);

                if (arr2 >= yearB) {
                    yearB = arr2;
                    b++;
                } else if (arr2 == yearB && arr1 >= monthB) {
                    monthB = arr1;
                    b++;
                } else if (arr2 == yearB && arr1 == monthB && arr0 >= dayB) {
                    dayB = arr0;
                    b++;
                }

            } else {
                Log.e(this.getClass().getSimpleName(), "Datumformat Error");
            }
        }

        String[] datumSorted = new String[b];
        for (int t = 0; t < b; t++) {
            datumSorted[t] = datumTemp[t];
        }

        return datumSorted;
    }
}
