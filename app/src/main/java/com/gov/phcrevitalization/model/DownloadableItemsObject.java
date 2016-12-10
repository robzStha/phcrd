package com.gov.phcrevitalization.model;

/**
 * Created by bugatti on 08/12/16.
 */

public class DownloadableItemsObject {

    String title, pdfName, url;

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    boolean downloaded;

    public DownloadableItemsObject(String title, String url) {
        this.title = title;
        this.url = url;
        this.pdfName = getPdfName();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPdfName() {

        String temp = url.substring(url.lastIndexOf("/") + 1, url.length());

        return temp;
    }
}
