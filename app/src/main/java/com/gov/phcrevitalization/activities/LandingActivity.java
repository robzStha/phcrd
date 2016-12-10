package com.gov.phcrevitalization.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.utils.CommonMethods;
import com.gov.phcrevitalization.utils.Opener;

public class LandingActivity extends AppCompatActivity implements View.OnTouchListener {

    LinearLayout llNews, llAct, llPrograms, llPublications,
            llReports, llForms, llOrganogram, llStaffs, llPhotoGallery,
            llDownloads, llDirectorsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();
    }

    private void init() {
        llNews = (LinearLayout) findViewById(R.id.ll_news);
        llAct = (LinearLayout) findViewById(R.id.ll_act_regulations);
        llPrograms = (LinearLayout) findViewById(R.id.ll_programs);
        llPublications = (LinearLayout) findViewById(R.id.ll_publications);
        llReports = (LinearLayout) findViewById(R.id.ll_reports);
        llForms = (LinearLayout) findViewById(R.id.ll_forms_formats);
        llOrganogram = (LinearLayout) findViewById(R.id.ll_organogram);
        llStaffs = (LinearLayout) findViewById(R.id.ll_staff_info);
        llPhotoGallery = (LinearLayout) findViewById(R.id.ll_photo_gallery);
        llDownloads = (LinearLayout) findViewById(R.id.ll_downloads);
        llDirectorsMsg = (LinearLayout) findViewById(R.id.ll_directors_message);

        llNews.setOnTouchListener(this);
        llAct.setOnTouchListener(this);
        llPrograms.setOnTouchListener(this);
        llPublications.setOnTouchListener(this);
        llReports.setOnTouchListener(this);
        llForms.setOnTouchListener(this);
        llOrganogram.setOnTouchListener(this);
        llStaffs.setOnTouchListener(this);
        llPhotoGallery.setOnTouchListener(this);
        llDownloads.setOnTouchListener(this);
        llDirectorsMsg.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.setAlpha(0.5f);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.setAlpha(1f);
            switch(view.getId()){
                case R.id.ll_news:
                    Opener.WebViewActivity(LandingActivity.this, CommonMethods.Pages.NEWS);
                    break;
                case R.id.ll_publications:
                    Opener.ListingAndDownloadingActivity(LandingActivity.this, CommonMethods.Downloadable.PUBLICATIONS);
                    break;
                case R.id.ll_act_regulations:
                    Opener.ListingAndDownloadingActivity(LandingActivity.this, CommonMethods.Downloadable.ACTS_REGULATIONS);
                    break;
                case R.id.ll_downloads:
                    Opener.ListingAndDownloadingActivity(LandingActivity.this, CommonMethods.Downloadable.DOWNLOADS);
                    break;
                case R.id.ll_forms_formats:
                    Opener.ListingAndDownloadingActivity(LandingActivity.this, CommonMethods.Downloadable.FORM_FORMATS);
                    break;
                default:
                    Opener.ProgramActivity(LandingActivity.this);
                    break;
            }

//            Toast.makeText(LandingActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        } else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
            view.setAlpha(1f);
        }
        return true;
    }
}
