package com.gov.phcrevitalization.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.activities.ListingAndDownloadingActivity;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Rabin on 9/7/2015.
 */
public class CommonMethods {

    public static final int CAPITALIZE_WORDS = 1;

    public static Intent getPickImageChooserIntent(Activity activity) {

// Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri(activity);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = activity.getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private static Uri getCaptureImageOutputUri(Activity activity) {
        Uri outputFileUri = null;
        File getImage = activity.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #//getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public static Uri getPickImageResultUri(Intent data, Activity activity) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(activity) : data.getData();
    }

    /**
     * Method for removing the keyboard if touched outside the editview.
     *
     * @param view
     * @param baseActivity
     */
    public static void setupUI(View view, final Activity baseActivity) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
//                    baseActivity.closeKeyboard();
                    hideSoftKeyboard(baseActivity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, baseActivity);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    /**
     * @return : file name with jpt extension
     */
    public static String randomFileName(String ext) {
        String fileName;
        fileName = String.format("%s.%s", System.currentTimeMillis(), ext);
        return fileName;
    }

    public static Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void setImageviewRatio(Context ctx, ImageView iv, int padding) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels - 2 * padding;
        int screenHeight = screenWidth * 3 / 4;
        System.out.println("Width=>" + screenWidth);
        System.out.println("Height=>" + screenHeight);
        iv.getLayoutParams().width = screenWidth;
        iv.getLayoutParams().height = screenWidth * 3 / 4;
        iv.requestLayout();
    }

    public static float dpFromPx(final Activity activity, final int px) {
        return px / activity.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Activity activity, final int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context activity, final int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }

    public static void makeCall(Context context, String mobileNumber) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        if ((tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT)) {
            Toast.makeText(context, "Please insert SIM", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((tm.getSimState() != TelephonyManager.SIM_STATE_READY)) {
            Toast.makeText(context, "Sim is not ready.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
        try {
            context.startActivity(callIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
    }

    public static void visitUrl(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }

    public static void sendEmail(Context context, String EmailId) {
        /* Create the Intent */
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

/* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EmailId});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");

/* Send it off to the Activity-Chooser */
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void getDeviceDensity(Context context) {
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                System.out.println("Device density: DENSITY_LOW");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                System.out.println("Device density: DENSITY_MEDIUM");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                System.out.println("Device density: DENSITY_HIGH");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                System.out.println("Device density: DENSITY_XHIGH");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                System.out.println("Device density: DENSITY_XXHIGH");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                System.out.println("Device density: DENSITY_XXXHIGH");
                break;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static class PasswordObj {
        boolean isValid;
        String msg;
    }

    public static PasswordObj isValidPassword(String pwd, EditText etPwd) {
        PasswordObj pwdObj = new PasswordObj();
        return pwdObj;
    }

    public static void setFadeOut(Interpolator interpolator, int duration, View view) {
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(interpolator); // add this
        fadeOut.setDuration(duration);
        view.setAnimation(fadeOut);
    }

    public static void setFadeIn(Interpolator interpolator, int duration, View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(interpolator); // add this
        fadeIn.setDuration(duration);
        view.setAnimation(fadeIn);
    }

    public static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }


    public static String capitalize(String str, int type) {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        switch (type) {
            case CommonMethods.CAPITALIZE_WORDS:
                for (String s : strArray) {
                    str = s.substring(0, 1).toUpperCase() + s.substring(1);
                    builder.append(str + " ");
                }
                break;
        }
        return builder.toString();
    }

    public static String getUrlVideoRTSP(String urlYoutube)
    {
        try
        {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = extractYoutubeId(urlYoutube);
            URL url = new URL(gdy + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Document doc = documentBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");///media:content
            String cursor = urlYoutube;
            for (int i = 0; i < list.getLength(); i++)
            {
                Node node = list.item(i);
                if (node != null)
                {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    for (int j = 0; j < nodeMap.getLength(); j++)
                    {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format"))
                    {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url"))
                        {
                            cursor = maps.get("url");
                        }
                        if (f.equals("1"))
                            return cursor;
                    }
                }
            }
            return cursor;
        }
        catch (Exception ex)
        {
            Log.e("Youtube RTSP Exception", ex.toString());
        }
        return urlYoutube;

    }

    protected static String extractYoutubeId(String url) throws MalformedURLException
    {
        String id = null;
        try
        {
            String query = new URL(url).getQuery();
            if (query != null)
            {
                String[] param = query.split("&");
                for (String row : param)
                {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v"))
                    {
                        id = param1[1];
                    }
                }
            }
            else
            {
                if (url.contains("embed"))
                {
                    id = url.substring(url.lastIndexOf("/") + 1);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Exception", ex.toString());
        }
        return id;
    }

    public static class Pages {
        public static String NEWS="news";
    }
    public static void downloadFile(final Activity activity, String url,final View view) {
//        TextView tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        ((ListingAndDownloadingActivity)activity).showBook = true;
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_downloading);
        final TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        final TextView tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        final DownloadFileAsync download;
        if (!checkIfFileExists(activity, url)) {
            if (ConnectionMngr.hasConnection(activity)) {
                if (!((ListingAndDownloadingActivity) activity).downloading) {
                    String filename = getFileNameFromUrl(url);
                    final String path = activity.getExternalFilesDir(null) + "/" + filename;
                    progressBar.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvProgress.setVisibility(View.VISIBLE);
                    download = new DownloadFileAsync(path, activity, view, new DownloadFileAsync.PostDownload() {
                        @Override
                        public void downloadDone(File file) {
                            progressBar.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                            if(file!=null) {
                                Log.i("TEST", "file download completed");

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                activity.startActivity(intent);
                                Log.i("TEST", "file unzip completed to: " + activity.getExternalFilesDir(null) + "----" + file.getName());
                            }else{
                                Toast.makeText(activity, "Downloading failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                            ((ListingAndDownloadingActivity) activity).downloading = false;
                            ((ListingAndDownloadingActivity) activity).downloadingPageType = "";
                        }
                    });
                    download.execute(url);
                    ((ListingAndDownloadingActivity) activity).downloading = true;
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setProgress(0);
                            tvProgress.setText("0%");
                            progressBar.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.GONE);
                            tvProgress.setVisibility(View.GONE);
                            ((ListingAndDownloadingActivity) activity).downloading = false;
                            ((ListingAndDownloadingActivity) activity).downloadingPageType = "";
                            download.cancel(true);
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(activity, "Please wait. Download in progress...", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } else {
            Toast toast = Toast.makeText(activity, "Issue from here....", Toast.LENGTH_LONG);
            toast.show();
//            activity.startActivity(intentToStart);
        }
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getSecondLastFileNameFromUrl(String url) {
        String[] strings = url.split("/");
        return strings[strings.length - 2];
    }

    public static String getFilePath(String url) {
        return url.substring(0, url.lastIndexOf(File.separator));
    }

    public static boolean checkIfFileExists(Activity activity, String url) {
        String fileName = getFileNameFromUrl(url);
        String secondLastName = getSecondLastFileNameFromUrl(url);
        fileName = stripExtension(fileName);
        File file = new File(activity.getExternalFilesDir(null) + "/" + fileName);
        File fileSecondLast = new File(activity.getExternalFilesDir(null) + "/" + secondLastName);
        System.out.println("file exists: main file:: " + file.exists() + " -- second last file:  - " + secondLastName + " - " + fileSecondLast.exists());
        return (file.exists() || fileSecondLast.exists()) ? true : false;
    }

    public static boolean checkIfPdfExists(Activity activity, String url) {
        String fileName = getFileNameFromUrl(url);
        File file = new File(activity.getExternalFilesDir(null) + "/" + fileName);
        return (file.exists()) ? true : false;
    }

    public static String stripExtension(String s) {
        return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
    }

    public static class DeviceMemory {

        public static float getInternalStorageSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            return free;
        }

        public static float getInternalUsedSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            float busy = total - free;
            return busy;
        }
    }

    public class Downloadable {
        public static final int PUBLICATIONS = 1;
        public static final int ACTS_REGULATIONS = 2;
        public static final int DOWNLOADS = 3;
        public static final int FORM_FORMATS = 4;
    }
}