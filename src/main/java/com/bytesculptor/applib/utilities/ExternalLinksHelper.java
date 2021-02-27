/*
 * MIT License
 *
 * Copyright (c) 2017 - 2021  Byte Sculptor Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        Uri uri = Uri.parse("market://apps/dev?id=4913602895703268011");
        Intent goToAppStore = new Intent(Intent.ACTION_VIEW, uri);
        goToAppStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToAppStore);
        } catch (
                ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/dev?id=4913602895703268011")));
        }
    }


    /**
     * Opens a "share" to send the app store link
     *
     * @param context
     * @param packageName
     * @param appName
     */
    public static void shareApp(Context context, String packageName, String appName) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
            String shareMessage = "https://play.google.com/store/apps/details?id=" + packageName + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, ""));
        } catch (Exception e) {
            //e.toString();
        }
    }

}