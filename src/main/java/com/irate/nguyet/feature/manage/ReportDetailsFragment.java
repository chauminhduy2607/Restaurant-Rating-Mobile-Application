package com.irate.nguyet.feature.manage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.irate.nguyet.MainActivity;
import com.irate.nguyet.MainApplication;
import com.irate.nguyet.R;
import com.irate.nguyet.data.repo.RestaurantRepo;
import com.irate.nguyet.databinding.FragmentReportDetailsBinding;

public class ReportDetailsFragment extends Fragment {

    private interface Callback {
        void onSuccess();

        void onError(Throwable e);
    }

    private class DeleteReportTask extends AsyncTask<Long, Void, ReportDetailsFragment.DeleteReportTask.Result> {

        private class Result {
            Throwable error;

            public Result() {
            }

            public Result(Throwable error) {
                this.error = error;
            }
        }

        private final RestaurantRepo repo;
        private final Callback callback;

        public DeleteReportTask(RestaurantRepo repo, Callback callback) {
            this.repo = repo;
            this.callback = callback;
        }

        @Override
        protected ReportDetailsFragment.DeleteReportTask.Result doInBackground(Long... ids) {
            try {
                long id = ids[0];

                repo.deleteReport(id);
                return new ReportDetailsFragment.DeleteReportTask.Result();
            } catch (Exception e) {
                return new ReportDetailsFragment.DeleteReportTask.Result(e);
            }
        }

        @Override
        protected void onPostExecute(ReportDetailsFragment.DeleteReportTask.Result result) {
            if (result.error != null) {
                callback.onError(result.error);
            } else {
                callback.onSuccess();
            }
        }
    }

    public interface OnEditListener {
        void startEdit();
    }

    private static final String REPORT_ID = "report_id";
    private final RestaurantRepo repo = MainApplication.getInstance().getRepository();
    private FragmentReportDetailsBinding binding;
    private long reportId = 0;
    ReportDetailsFragment self = this;

    @Nullable
    private OnEditListener onEditListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (!getArguments().containsKey(REPORT_ID)) {
            throw new IllegalArgumentException("Report id is required");
        }
        reportId = getArguments().getLong(REPORT_ID);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnEditListener) {
            onEditListener = (OnEditListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainApplication.getInstance().getRepository().getReport(getArguments().getLong(REPORT_ID)).observe(getViewLifecycleOwner(), report -> {
            if (report != null){
                getActivity().setTitle(report.getName());
                binding.tvName.setText(report.getName());
                binding.tvType.setText(report.getType());
                binding.tvVisitTime.setText(MainApplication.dateTimeFormat.format(report.getReportTime()));
                binding.tvPrice.setText(MainApplication.getInstance().getPriceFormat().format(report.getPrice()));
                binding.tvNote.setText(report.getNote());
                binding.tvReporter.setText(report.getReporter());
                binding.rbAverageRating.setRating(report.getRating().getAverage());
                binding.rbService.setRating(report.getRating().getService());
                binding.rbCleanliness.setRating(report.getRating().getCleanliness());
                binding.rbFood.setRating(report.getRating().getFood());
            }
        });
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(v.getContext());
            }
        });

        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(self.getContext())
                        .setCancelable(true)
                        .setTitle("IRate")
                        .setMessage("Do you want to delete this report?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            new DeleteReportTask(repo, new Callback() {
                                @Override
                                public void onSuccess() {
                                    new AlertDialog.Builder(self.getContext())
                                            .setCancelable(false)
                                            .setTitle("IRate")
                                            .setMessage("Report successfully deleted")
                                            .setPositiveButton("OK", (dialog, which) -> {
                                                dialog.dismiss();
                                                MainActivity.start(self.getContext());
                                            }).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    new AlertDialog.Builder(self.getContext())
                                            .setCancelable(false)
                                            .setTitle("IRate")
                                            .setMessage("Cannot delete report")
                                            .setPositiveButton("OK", (dialog, which) -> {
                                                dialog.dismiss();
                                            }).show();
                                }
                            }).execute(reportId);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        }).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_report_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_edit) {
            showEditScreen();
            return true;
        }
        return false;
    }

    private void showEditScreen() {
        if (onEditListener != null) {
            onEditListener.startEdit();
        }
    }

    public static ReportDetailsFragment newInstance(@IntRange(from = 1) long reportId) {
        Bundle args = new Bundle();
        args.putLong(REPORT_ID, reportId);
        ReportDetailsFragment fragment = new ReportDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
