package com.irate.nguyet.feature.manage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.irate.nguyet.BuildConfig;
import com.irate.nguyet.MainApplication;
import com.irate.nguyet.R;
import com.irate.data.model.Report;
import com.irate.nguyet.data.repo.RestaurantRepo;
import com.irate.nguyet.databinding.FragmentRestaurantRatingBinding;
import com.irate.nguyet.common.DateExt;
import com.irate.nguyet.common.ViewExt;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class RestaurantRatingFragment extends Fragment {

    private static final String REPORT_ID = "report_id";

    private final NumberFormat priceFormat = NumberFormat.getInstance(Locale.getDefault());

    private FragmentRestaurantRatingBinding binding;

    private long reportId = 0;

    private final RestaurantRepo repo = MainApplication.getInstance().getRepository();
    private Date reportDate;
    private final SwitchDateTimeDialogFragment datetimePicker;

    @Nullable
    private Callback callback;

    public interface Callback {
        void onSaved();
    }

    private static class CreateReportTask extends AsyncTask<Report, Void, CreateReportTask.Result> {

        private static class Result {
            long reportId;
            Throwable error;

            public Result(long reportId) {
                this.reportId = reportId;
            }

            public Result(Throwable error) {
                this.error = error;
            }
        }

        private interface Callback {
            void onSuccess(long id);

            void onError(Throwable e);
        }

        private final RestaurantRepo repo;
        private final Callback callback;

        public CreateReportTask(RestaurantRepo repo, Callback callback) {
            this.repo = repo;
            this.callback = callback;
        }

        @Override
        protected Result doInBackground(Report... reports) {
            try {
                Report report = reports[0];

                boolean checkDup = repo.checkDuplicate(report.getName(), report.getReporter(), report.getId());
                Log.d(RestaurantRatingFragment.class.getName(), "Check dup " + checkDup);
                if (checkDup){
                    return new Result(new Exception(report.getReporter() + " reviewed " + report.getName()));
                }
                return new Result(repo.createReport(report));
            } catch (Exception e) {
                Log.e(RestaurantRatingFragment.class.getName(), "Send message to Slack failed", e);
                return new Result(e);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result.error != null) {
                callback.onError(result.error);
            } else {
                callback.onSuccess(result.reportId);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            reportId = args.getLong(REPORT_ID, 0);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRestaurantRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateData() {
        if (reportId > 0) {
            MainApplication.getInstance().getRepository().getReport(reportId).observe(getViewLifecycleOwner(), report -> {
                binding.edtName.setText(report.getName());
                binding.tvType.setText(report.getType(), false);
                binding.edtDatetime.setText(MainApplication.dateTimeFormat.format(report.getReportTime()));
                binding.edtPrice.setText(priceFormat.format(report.getPrice()));
                binding.edtNote.setText(report.getNote());
                binding.edtReporter.setText(report.getReporter());
                Report.Rating rating = report.getRating();
                binding.ratingBarService.setRating(rating.getService());
                binding.ratingBarCleanliness.setRating(rating.getCleanliness());
                binding.ratingBarFood.setRating(rating.getFood());
            });
        }
    }

    private Float parsePrice(String text) {
        try {
            return priceFormat.parse(text).floatValue();
        } catch (ParseException e) {
            return null;
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
        binding.tvType.setAdapter(new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_list_item_1, MainApplication.getInstance().RESTAURANT_TYPES));
        this.datetimePicker.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            public void onPositiveButtonClick(@Nullable Date date) {
                if (date != null) {
                    RestaurantRatingFragment.this.reportDate = DateExt.truncateSeconds(date);
                    binding.edtDatetime.setText(DateFormat.getDateTimeInstance().format(RestaurantRatingFragment.this.reportDate));
                }

            }

            public void onNegativeButtonClick(@Nullable Date date) {
            }
        });
        binding.edtDatetime.setText(MainApplication.dateTimeFormat.format(this.reportDate));
        binding.edtDatetime.setOnClickListener(it -> RestaurantRatingFragment.this.datetimePicker.show(RestaurantRatingFragment.this.getChildFragmentManager(), "datetimepicker"));
        binding.btnSave.setOnClickListener(it -> {
            String name = ViewExt.requiredText(binding.tilName, "Please enter your restaurant name");
            if (name != null) {
                String type = ViewExt.requiredText(binding.tilType, "Please enter your restaurant type");
                if (type != null) {
                    Date date = RestaurantRatingFragment.this.reportDate;
                    String priceText = ViewExt.requiredText(view.findViewById(R.id.til_price), "Please provide price");
                    if (priceText != null) {
                        Float price = parsePrice(priceText);
                        if (price != null) {
                            RatingBar rbService = (RatingBar) view.findViewById(R.id.rating_bar_service);
                            float serviceRating = rbService.getRating();
                            RatingBar rbCleanliness = view.findViewById(R.id.rating_bar_cleanliness);
                            float cleanlinessRating = rbCleanliness.getRating();
                            RatingBar rbFood = view.findViewById(R.id.rating_bar_food);
                            Report.Rating rating = new Report.Rating(serviceRating, cleanlinessRating, rbFood.getRating());
                            TextInputEditText noteEdt = view.findViewById(R.id.edt_note);
                            String note = String.valueOf(noteEdt.getText());
                            String reporter = ViewExt.requiredText(view.findViewById(R.id.til_reporter), "Please provide report name");
                            if (reporter != null) {
                                final Report report = new Report(reportId, name, type, date, price, rating, note, reporter);
                                getView().findViewById(R.id.btn_save).setEnabled(false);
                                new CreateReportTask(repo, new CreateReportTask.Callback() {
                                    @Override
                                    public void onSuccess(long id) {
                                        if (reportId > 0 && callback != null) {
                                            callback.onSaved();
                                        } else {
                                            reset();
                                            getView().findViewById(R.id.til_name).requestFocus();
                                            getView().findViewById(R.id.btn_save).setEnabled(true);
                                            ReportDetailsActivity.start(requireContext(), id);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("show error log", e.getMessage());
                                        new AlertDialog.Builder(requireContext())
                                                .setCancelable(false)
                                                .setTitle("IRate")
                                                .setMessage("Error: " + e.getMessage())
                                                .setPositiveButton("OK", (dialog, which) -> {
                                                    getView().findViewById(R.id.btn_save).setEnabled(true);
                                                    dialog.dismiss();
                                                }).show();
                                    }
                                }).execute(report);
                            }
                        }
                    }
                }
            }
        });
    }

    private void reset() {
        ViewExt.clearText(getView().findViewById(R.id.til_name));
        ViewExt.clearText(getView().findViewById(R.id.til_type));
        this.reportDate = new Date();
        ((TextInputEditText) getView().findViewById(R.id.edt_datetime)).setText(MainApplication.dateTimeFormat.format(this.reportDate));
        ViewExt.clearText(getView().findViewById(R.id.til_price));
        RatingBar rateBar = getView().findViewById(R.id.rating_bar_service);
        rateBar.setRating(0.0F);
        rateBar = getView().findViewById(R.id.rating_bar_cleanliness);
        rateBar.setRating(0.0F);
        rateBar = getView().findViewById(R.id.rating_bar_food);
        rateBar.setRating(0.0F);
        ViewExt.clearText(getView().findViewById(R.id.til_note));
        ViewExt.clearText(getView().findViewById(R.id.til_reporter));
    }

    public RestaurantRatingFragment() {
        this.reportDate = DateExt.truncateSeconds(new Date());
        this.datetimePicker = SwitchDateTimeDialogFragment.newInstance("Please select date and time", "OK", "Cancel");
        this.datetimePicker.startAtCalendarView();
    }

    public static RestaurantRatingFragment newInstance(@IntRange(from = 1) long reportId) {
        Bundle args = new Bundle();
        args.putLong(REPORT_ID, reportId);
        RestaurantRatingFragment fragment = new RestaurantRatingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
