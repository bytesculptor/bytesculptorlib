package com.bytesculptor.applib.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bytesculptor.applib.R;

public class AppRating {

    private static final String LIKE_QUESTION_DONE = "like_question";
    private static final String GAVE_FEEDBACK = "gave_feedback";
    private static final String NEVER_ASK = "never_ask";
    private static final String COUNTER = "launch_counter";
    private static final String FIRST_LAUNCH = "first_launch_timestamp";

    private static String appName;
    private static FragmentManager fragmentManager;
    private static Context context;

    private static SharedPreferences.Editor sharedPrefsEditor;

    public static void appLaunch(Context ctx, FragmentManager fragMgr, int daysUntilPrompt, int startsUntilPrompt, String name) {
        if (daysUntilPrompt < 0 || startsUntilPrompt < 0) throw new IllegalArgumentException();
        fragmentManager = fragMgr;
        context = ctx;
        appName = name;

        final String APP_RATING_PREF = ctx.getPackageName() + ".apprating";
        SharedPreferences prefs = ctx.getSharedPreferences(APP_RATING_PREF, Context.MODE_PRIVATE);
        sharedPrefsEditor = prefs.edit();

        // check if already asked
        if (prefs.getBoolean(NEVER_ASK, false)) {
            return;
        }

        // increment counter
        long launch_count = 1 + prefs.getLong(COUNTER, 0);
        sharedPrefsEditor.putLong(COUNTER, launch_count);

        // update timestamp if 0
        long firstLaunchTimestamp = prefs.getLong(FIRST_LAUNCH, 0);
        if (firstLaunchTimestamp == 0) {
            firstLaunchTimestamp = System.currentTimeMillis();
            sharedPrefsEditor.putLong(FIRST_LAUNCH, firstLaunchTimestamp);
        }
        sharedPrefsEditor.apply();

        // prompt dialog if limit reached
        if ((launch_count >= startsUntilPrompt) &&
                (System.currentTimeMillis() >= (firstLaunchTimestamp + (daysUntilPrompt * 24 * 3600 * 1000)))) {
            if (prefs.getBoolean(LIKE_QUESTION_DONE, false)) {
                DialogQuestionRateApp rate = new DialogQuestionRateApp();
                rate.show(fragmentManager, "likeDialog");
            } else {
                DialogQuestionLike like = new DialogQuestionLike();
                like.show(fragmentManager, "rateDialog");
            }
        }
    }


    public static class DialogQuestionLike extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(getString(R.string.szLikeThisAppTitle));
            builder.setMessage(getString(R.string.szLikeThisAppMessage));

            builder.setPositiveButton(getString(R.string.szYes), (dialog, which) -> {
                setLikeQuestionAsked();
                DialogQuestionRateApp rate = new DialogQuestionRateApp();
                rate.show(fragmentManager, "likeDialog");
            });

            builder.setNegativeButton(getString(R.string.szLikeThisAppNo), (dialog, which) -> {
                setLikeQuestionAsked();
                DialogQuestionGiveFeedback feedback = new DialogQuestionGiveFeedback();
                feedback.show(fragmentManager, "feedbackDialog");
            });

            return builder.create();
        }
    }


    public static class DialogQuestionRateApp extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(getString(R.string.szRateThisApp));
            builder.setMessage(getString(R.string.szRatingMessage));

            builder.setPositiveButton(getString(R.string.szRateNow), (dialog, which) -> {
                setNeverAsk();
                ExternalLinksHelper.goToAppStore(requireContext(), requireContext().getPackageName());
            });

            builder.setNegativeButton(getString(R.string.szNoThanks), (dialog, which) -> {
                setNeverAsk();
            });

            builder.setNeutralButton(getString(R.string.szRemindMe), (dialog, which) -> {
                resetCounterAndTimestamp();
            });

            return builder.create();
        }
    }


    public static class DialogQuestionGiveFeedback extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(getString(R.string.szFeedback));
            builder.setMessage(getString(R.string.szAskFeedbackMessage));

            builder.setPositiveButton(getString(R.string.szYes), (dialog, which) -> {
                setGaveFeedback();
                resetCounterAndTimestamp();
                ExternalLinksHelper.sendFeedbackMail(context, appName);
            });

            builder.setNegativeButton(getString(R.string.szNoThanks), (dialog, which) -> {
                setNeverAsk();
            });

            return builder.create();
        }
    }


    private static void resetCounterAndTimestamp() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor.putLong(FIRST_LAUNCH, 0);
            sharedPrefsEditor.putLong(COUNTER, 0);
            sharedPrefsEditor.apply();
        }
    }


    private static void setNeverAsk() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor.putBoolean(NEVER_ASK, true);
            sharedPrefsEditor.apply();
        }
    }


    private static void setGaveFeedback() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor.putBoolean(GAVE_FEEDBACK, true);
            sharedPrefsEditor.apply();
        }
    }


    private static void setLikeQuestionAsked() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor.putBoolean(LIKE_QUESTION_DONE, true);
            sharedPrefsEditor.apply();
        }
    }
}