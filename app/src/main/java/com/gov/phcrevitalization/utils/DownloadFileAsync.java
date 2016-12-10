package com.gov.phcrevitalization.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.activities.ListingAndDownloadingActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileAsync extends AsyncTask<String, String, String> {

    private static final String TAG = "DOWNLOADFILE";
    public volatile boolean running = true;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private PostDownload callback;
    private Context context;
    private FileDescriptor fd;
    private File file;
    private String downloadLocation;
    ProgressDialog pd;
    View view;
    TextView tvProgress, tvCancel;
    ProgressBar progressBar;
    ImageView ivDownloaded;
    private String downloadingUrl;
    String type;
    int lenghtOfFile;
    long total = 0;

    public DownloadFileAsync(String downloadLocation, Context context, PostDownload callback) {
        this.context = context;
        this.callback = callback;
        this.downloadLocation = downloadLocation;
    }

    public DownloadFileAsync(String downloadLocation, Context context, View view, PostDownload callback) {
        this.context = context;
        this.callback = callback;
        this.downloadLocation = downloadLocation;
        this.view = view;
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        ivDownloaded = (ImageView) view.findViewById(R.id.iv_downloaded);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_downloading);
        progressBar.setIndeterminate(true);
        ((ListingAndDownloadingActivity) context).viewCurrent = view;
        ((ListingAndDownloadingActivity) context).downloadingPageType = type;
        System.out.println("View set");

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        System.out.println("Downloading file: " + aurl[0]);

        try {
            URL url = new URL(aurl[0]);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.connect();

            lenghtOfFile = connection.getContentLength();
            Log.d(TAG, "Length of the file: " + lenghtOfFile + "-----" + CommonMethods.DeviceMemory.getInternalFreeSpace());

            InputStream input = new BufferedInputStream(url.openStream());
            file = new File(downloadLocation);
            FileOutputStream output = new FileOutputStream(file); //context.openFileOutput("content.zip", Context.MODE_PRIVATE);
            Log.d(TAG, "file saved at " + file.getAbsolutePath());
            fd = output.getFD();

            byte data[] = new byte[1024];

            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    ((ListingAndDownloadingActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resetViews();
                        }
                    });
                    break;
                } else {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }
        return null;

    }

    protected void onProgressUpdate(String... progress) {
        Log.d(TAG, progress[0] + "--- Progresssss" + progressBar.getVisibility());
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_downloading);

        if (!((ListingAndDownloadingActivity) context).downloading) {
            cancel(true);
        }
//        System.out.println("Downloading urs: current :: "+((ListingAndDownloadingActivity)context).downloadingPageType+" -- "+type);
//        if(((ListingAndDownloadingActivity)context).downloadingPageType.equals(type)) {

        if (((ListingAndDownloadingActivity) context).viewCurrent != null && ((ListingAndDownloadingActivity) context).pbCurrent != null) {
            ((ListingAndDownloadingActivity) context).showBook = true;
            progressBar = ((ListingAndDownloadingActivity) context).pbCurrent;
            tvProgress = ((ListingAndDownloadingActivity) context).tvProgressCurrent;
            tvCancel = ((ListingAndDownloadingActivity) context).tvCancel;
        }

        progressBar.setProgress(Integer.parseInt(progress[0]));
        tvProgress.setText("" + Integer.parseInt(progress[0]) + "%");
        if (progressBar.isIndeterminate()) {
            progressBar.setIndeterminate(false);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(true);
                resetViews();
            }
        });
//        }else{
//            ((ListingAndDownloadingActivity)context).showBook = false;
//        }
    }

    @Override
    protected void onPostExecute(String unused) {
        System.out.println("Length real: " + lenghtOfFile + " downloaded: " + total);
        if (lenghtOfFile != total)
            file = null;
        if (callback != null) {
            callback.downloadDone(file);
            ivDownloaded.setVisibility(View.VISIBLE);
        }
        resetViews();
    }

    private void resetViews() {
        ((ListingAndDownloadingActivity) context).downloading = false;
        ((ListingAndDownloadingActivity) context).downloadingPageType = "";
        tvProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        ((ListingAndDownloadingActivity) context).viewCurrent = null; // removing the current view
        ((ListingAndDownloadingActivity) context).pbCurrent = null; // removing the current view
        ((ListingAndDownloadingActivity) context).downloadingItemPos = -1; // removing the current view
        ((ListingAndDownloadingActivity) context).tvProgressCurrent = null; // removing the current view
    }

    public interface PostDownload {
        void downloadDone(File fd);
    }
}