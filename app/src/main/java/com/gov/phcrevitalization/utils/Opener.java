package com.gov.phcrevitalization.utils;

import android.app.Activity;
import android.content.Intent;

import com.gov.phcrevitalization.activities.LandingActivity;
import com.gov.phcrevitalization.activities.LocalWebViewActivity;
import com.gov.phcrevitalization.activities.ProgramsActivity;
import com.gov.phcrevitalization.activities.ListingAndDownloadingActivity;
import com.gov.phcrevitalization.activities.WebViewActivity;

/**
 * Created by Robz on 6/29/2016.
 */
public class Opener {
    static Intent i;

    public static void LandingActivity(Activity activity) {
        startActivity(activity, LandingActivity.class);
    }

    /**
     * Starts the given activity
     *
     * @param activity      : instance of the current activity
     * @param activityClass : Activity to be opened
     */
    private static void startActivity(Activity activity, Class<?> activityClass) {
        i = new Intent(activity, activityClass);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);
    }

    public static void WebViewActivity(Activity activity, String type) {
        i = new Intent(activity, WebViewActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("type", type);
        activity.startActivity(i);
    }

    public static void ProgramActivity(Activity activity){
        startActivity(activity, ProgramsActivity.class);
    }

    public static void LocalWebView(Activity activity, String pageTitle) {
        i = new Intent(activity, LocalWebViewActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("page_title", pageTitle);
        activity.startActivity(i);
    }

    public static void ListingAndDownloadingActivity(Activity activity, int type) {
        i = new Intent(activity, ListingAndDownloadingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("type", type);
        activity.startActivity(i);
    }
}