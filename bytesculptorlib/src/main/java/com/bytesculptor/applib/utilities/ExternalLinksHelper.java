package com.bytesculptor.applib.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.bytesculptor.applib.utilities.ByteSculptorConstants.FEEDBACK_EMAIL;


public class ExternalLinksHelper {


    /**
     * Intent to start the default email app. Prompts a choice if more than one is installed.
     *
     * @param subject email subject
     */
    public static void sendFeedbackMail(Context context, String subject) {
        String uriText = "mailto:" + FEEDBACK_EMAIL + "?subject=" + subject;
        Uri mailUri = Uri.parse(uriText);
        Intent intent = new Intent(Intent.ACTION_SENDTO, mailUri);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(Intent.createChooser(intent, "sendEmail"));
    }


    /**
     * Opens an URL with the default browser. Fires a toast if it fails and the toast message is not empty.
     *
     * @param context
     * @param url
     * @param toastMessage
     */
    public static void openLinkInBrowser(Context context, String url, String toastMessage) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            if (toastMessage.trim().length() > 0) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Opens the Google Play Store app with the app named in packageName. If it fails or Play Store
     * is not installed it tries to open with the browser.
     *
     * @param context
     * @param packageName
     */
    public static void goToAppStore(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToAppStore = new Intent(Intent.ACTION_VIEW, uri);
        goToAppStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK | FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(goToAppStore);
        try {
            context.startActivity(goToAppStore);
        } catch (
                ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }


    /**
     * Opens the Google Play Store with all Byte Sculptor apps. If it fails or Play Store
     * is not installed it tries to open with the browser.
     *
     * @param context
     */
    public static void showMoreByteSculptorApps(Context context) {
        Uri uri = Uri.parse("market://apps/developer?id=Byte+Sculptor+Software");
        Intent goToAppStore = new Intent(Intent.ACTION_VIEW, uri);
        goToAppStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToAppStore);
        } catch (
                ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/developer?id=Byte+Sculptor+Software")));
        }
    }

}