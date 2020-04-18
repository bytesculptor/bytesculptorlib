package com.bytesculptor.androidlib.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bytesculptor.androidlib.R;

import java.util.Objects;

public class AppRating {

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;

    private static final String NEVER_ASK = "never_ask";
    private static final String COUNTER = "launch_counter";
    private static final String FIRST_LAUNCH = "first_launch_timestamp";

    private static SharedPreferences.Editor sEditor;

    public static void appLaunch(Context context, FragmentManager fragmentManager) {
        final String APP_RATING_PREF = context.getPackageName() + ".apprating";

        SharedPreferences prefs = context.getSharedPreferences(APP_RATING_PREF, 0);
        if (prefs.getBoolean(NEVER_ASK, false)) {
            return;
        }

        sEditor = prefs.edit();
        long launch_count = 1 + prefs.getLong(COUNTER, 0);
        sEditor.putLong(COUNTER, launch_count);

        long firstLaunchTimestamp = prefs.getLong(FIRST_LAUNCH, 0);
        if (firstLaunchTimestamp == 0) {
            firstLaunchTimestamp = System.currentTimeMillis();
            sEditor.putLong(FIRST_LAUNCH, firstLaunchTimestamp);
        }

        if ((launch_count >= LAUNCHES_UNTIL_PROMPT) &&
                (System.currentTimeMillis() >= (firstLaunchTimestamp + (DAYS_UNTIL_PROMPT * 24 * 3600 * 1000)))) {
            DialogAppRating appRate = new DialogAppRating();
            appRate.show(fragmentManager, "rateDialog");
        }
        sEditor.apply();
    }


    public static class DialogAppRating extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(getString(R.string.szRateDialogTitle));
            builder.setMessage(getString(R.string.szRatingMessage));

            builder.setPositiveButton(getString(R.string.szRateNow), (dialog, which) -> {
                Uri uri = Uri.parse("market://details?id=" + Objects.requireNonNull(getContext()).getPackageName());
                Intent goToAppStore = new Intent(Intent.ACTION_VIEW, uri);
                goToAppStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToAppStore);
                } catch (
                        ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }

                if (sEditor != null) {
                    sEditor.putBoolean(NEVER_ASK, true);
                    sEditor.apply();
                }
            });

            builder.setNegativeButton(getString(R.string.szNoThanks), (dialog, which) -> {
                if (sEditor != null) {
                    sEditor.putBoolean(NEVER_ASK, true);
                    sEditor.apply();
                }
            });

            builder.setNeutralButton(getString(R.string.szRemindLater), (dialog, which) -> {
                if (sEditor != null) {
                    sEditor.putLong(FIRST_LAUNCH, 0);
                    sEditor.putLong(COUNTER, 0);
                    sEditor.apply();
                }
            });

            return builder.create();
        }
    }
}