package com.gov.phcrevitalization.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.utils.CommonMethods;

public class WebViewActivity extends AppCompatActivity {

    WebView wvCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        wvCommon = (WebView) findViewById(R.id.wv_common);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String type = bundle.getString("type");
            if(type.equals(CommonMethods.Pages.NEWS)){
                wvCommon.getSettings().setJavaScriptEnabled(true);

                wvCommon.setInitialScale(1);
                wvCommon.getSettings().setJavaScriptEnabled(true);
                wvCommon.getSettings().setLoadWithOverviewMode(true);
                wvCommon.getSettings().setUseWideViewPort(true);
                wvCommon.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                wvCommon.setScrollbarFadingEnabled(false);
                wvCommon.loadUrl("http://phcrd.gov.np/");

            }
        }

    }
}
