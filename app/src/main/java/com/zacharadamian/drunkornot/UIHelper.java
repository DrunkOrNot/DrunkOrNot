package com.zacharadamian.drunkornot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

public class UIHelper {
    public static void DisplayAlertWithText(String text, AppCompatActivity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String FormatCalculationResultForAlert(Double bac, Double minTime, Double maxTIme, AppCompatActivity activity) {
        String result = activity.getResources().getString(R.string.str_bacResult) + "\n";
        result += String.format("%.2f", bac) + "‰\n";
        result +=  activity.getResources().getString(R.string.str_soberUpTime) + "\n";
        result += UIHelper.ConvertDoubleToTimeString(minTime)
                + " - " + UIHelper.ConvertDoubleToTimeString(maxTIme);
        return result;

    }

    public static String ConvertDoubleToTimeString(double value) {
        if (value == 0.0)
            return "0 min";

        return  (int) Math.floor(value) + " h " +
                (int) Math.floor(((value - Math.floor(value)) * 60))
                + " min";
    }
}
