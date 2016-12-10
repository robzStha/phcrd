package com.gov.phcrevitalization.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.model.DownloadableItemsObject;
import com.gov.phcrevitalization.utils.CommonMethods;
import com.gov.phcrevitalization.utils.DownloadableItemsFeeder;

import java.io.File;
import java.util.List;

public class ListingAndDownloadingActivity extends AppCompatActivity {

    public static boolean downloading;
    public static View viewCurrent;
    public static String downloadingPageType = "";
    public static ProgressBar pbCurrent;
    public static TextView tvProgressCurrent;
    public static TextView tvCancel;
    //    public static boolean openBook;
    public static int downloadingItemPos = -1;
    public boolean showBook;

    RecyclerView rvDownloadableItems;

    List<DownloadableItemsObject> downloadableItemsObjects;
    int type;
    private String pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);

        rvDownloadableItems = (RecyclerView) findViewById(R.id.rv_downloadable_items);
        rvDownloadableItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");
        }

        initDownloadables();
        checkDownloadStatus();

        getSupportActionBar().setTitle(pageTitle);
        rvDownloadableItems.setAdapter(new DownloadableItemsAdapter());

    }

    private void checkDownloadStatus() {
        for (DownloadableItemsObject dio : downloadableItemsObjects) {
            dio.setDownloaded(CommonMethods.checkIfPdfExists(ListingAndDownloadingActivity.this, dio.getPdfName()));
            downloadableItemsObjects.set(downloadableItemsObjects.indexOf(dio), dio);
        }
    }

    private void initDownloadables() {
        switch (type) {
            case CommonMethods.Downloadable.PUBLICATIONS:
                pageTitle = getString(R.string.publications);
                downloadableItemsObjects = DownloadableItemsFeeder.PublicationFeeder.getItems();
                break;
            case CommonMethods.Downloadable.ACTS_REGULATIONS:
                pageTitle = getString(R.string.act_regulation);
                downloadableItemsObjects = DownloadableItemsFeeder.ActsRegulationsFeeder.getItems();
                break;
            case CommonMethods.Downloadable.DOWNLOADS:
                pageTitle = getString(R.string.downloads);
                downloadableItemsObjects = DownloadableItemsFeeder.DownloadsFeeder.getItems();
                break;
            case CommonMethods.Downloadable.FORM_FORMATS:
                pageTitle = getString(R.string.forms_formats);
                downloadableItemsObjects = DownloadableItemsFeeder.FormsAndFormat.getItems();
                break;
        }
    }

    public class DownloadableItemsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvProgress, tvCancel, tvSn;
        ProgressBar pbDownloading;
        LinearLayout llItemHolder;
        ImageView ivDownloaded;


        public DownloadableItemsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSn = (TextView) itemView.findViewById(R.id.tv_sn);
            tvProgress = (TextView) itemView.findViewById(R.id.tv_progress);
            tvCancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            pbDownloading = (ProgressBar) itemView.findViewById(R.id.pb_downloading);
            llItemHolder = (LinearLayout) itemView.findViewById(R.id.ll_itemHolder);
            ivDownloaded = (ImageView) itemView.findViewById(R.id.iv_downloaded);
        }
    }

    public class DownloadableItemsAdapter extends RecyclerView.Adapter<DownloadableItemsViewHolder> {

        private LayoutInflater inflater;

        @Override
        public DownloadableItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            inflater = (LayoutInflater) getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_downloadable_list, parent, false);
            return new DownloadableItemsViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? 1 : 2;
        }

        @Override
        public void onBindViewHolder(final DownloadableItemsViewHolder holder, final int position) {
            if (getItemViewType(position) == 1)
                holder.llItemHolder.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            else
                holder.llItemHolder.setBackgroundColor(getResources().getColor(R.color.light_grey));

            holder.tvTitle.setText(downloadableItemsObjects.get(position).getTitle());
            holder.tvSn.setText((position + 1) + "");

            if(downloadableItemsObjects.get(position).isDownloaded()){
                holder.ivDownloaded.setVisibility(View.VISIBLE);
            }else {
                holder.ivDownloaded.setVisibility(View.GONE);
            }

            holder.llItemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!downloadableItemsObjects.get(position).isDownloaded()) {
                        CommonMethods.downloadFile(ListingAndDownloadingActivity.this, downloadableItemsObjects.get(position).getUrl(), holder.llItemHolder);
                    }else {
                        String fileName = downloadableItemsObjects.get(position).getPdfName();
                        File file = new File(getExternalFilesDir(null) + "/" + fileName);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return downloadableItemsObjects.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDownloadStatus();
    }
}
