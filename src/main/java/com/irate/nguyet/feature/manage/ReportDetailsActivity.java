package com.irate.nguyet.feature.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.irate.nguyet.R;

public class ReportDetailsActivity extends AppCompatActivity implements ReportDetailsFragment.OnEditListener, RestaurantRatingFragment.Callback {

    private static final String REPORT_ID = "report_id";

    private static final String REPORT_DETAILS_TAG = "report_details_tag";
    private long reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        reportId = getIntent().getLongExtra(REPORT_ID, -1);
        if (reportId <= 0) {
            throw new IllegalArgumentException("Report id argument is required");
        }
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(REPORT_DETAILS_TAG) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, ReportDetailsFragment.newInstance(reportId), REPORT_DETAILS_TAG);
            transaction.commit();
        }
    }

    public static void start(@NonNull Context context, long reportId) {
        Intent intent = new Intent(context, ReportDetailsActivity.class);
        intent.putExtra(REPORT_ID, reportId);
        context.startActivity(intent);
    }

    @Override
    public void startEdit() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, RestaurantRatingFragment.newInstance(reportId));
        transaction.addToBackStack("report_edit");
        transaction.commit();
    }

    @Override
    public void onSaved() {
        onBackPressed();
    }
}