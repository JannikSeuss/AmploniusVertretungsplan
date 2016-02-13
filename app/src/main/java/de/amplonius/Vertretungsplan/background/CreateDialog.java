package de.amplonius.Vertretungsplan.background;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CreateDialog {


    public void dialogOK(String title, String message, Context context) {

        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
