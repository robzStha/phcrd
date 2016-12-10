package com.gov.phcrevitalization.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.gov.phcrevitalization.R;

public class LocalWebViewActivity extends AppCompatActivity {

    private String pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_wev_view_page);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            pageTitle = bundle.getString("page_title");
        }


        WebView wv;
        wv = (WebView) findViewById(R.id.wv_local);
        wv.loadUrl("file:///android_asset/webpages/"+pageTitle+".html");
    }
}
