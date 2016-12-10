package com.gov.phcrevitalization.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SlowhandJr. on 7/30/16.
 */
public class CopyToDevice {
    String fileName;
    private String TAG = "test";
    Context context;

    public CopyToDevice(Context context) {
        this.context = context;
    }

    /**
     *
     * @return the path of the copied file name
     */
    public String copy(String fileName) {
        this.fileName = fileName;
        if (!this.fileExists(fileName)) {
            try {
                String pureName = this.removeExtention(fileName);
                String targetDirectory = context.getExternalFilesDir(null) + "/books";
                File dir = new File(targetDirectory);
                dir.mkdirs();
                String targetPath = targetDirectory + "/" + fileName;

                InputStream localInputStream = context.getAssets().open(fileName);
                FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);

                byte[] arrayOfByte = new byte[1024];
                int offset;
                while ((offset = localInputStream.read(arrayOfByte)) > 0) {
                    localFileOutputStream.write(arrayOfByte, 0, offset);
                }
                localFileOutputStream.close();
                localInputStream.close();
                Log.d(TAG, fileName + " copied to phone");
                return context.getExternalFilesDir(null) + "/books/"+fileName;
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
                Log.d(TAG, "failed to copy");
                return "";
            }

        } else {
            Log.d(TAG, fileName + " already exist");
            return context.getExternalFilesDir(null) + "/books/"+fileName;
        }
    }

    public boolean fileExists(String fileName) {
        boolean res;

        String pureName = removeExtention(fileName);
        String targetDirectory = context.getExternalFilesDir(null) + "/books";
        String targetPath = targetDirectory + "/" + fileName;

        File file = new File(targetPath);
        debug(file.getAbsolutePath());

        if (file.exists()) res = true;
        else res = false;
        return res;
    }

    public static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory()) return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0) {
            // No period after first character - return name as it was passed in
            return filePath;
        } else {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }
    public void debug(String msg) {
        Log.d("EPub", msg);
    }
}
