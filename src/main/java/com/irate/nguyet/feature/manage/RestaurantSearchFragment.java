package com.irate.nguyet.feature.manage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.irate.nguyet.MainApplication;
import com.irate.nguyet.R;
import com.irate.data.model.Report;
import com.irate.nguyet.data.repo.RestaurantRepo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class RestaurantSearchFragment extends Fragment {

    private RestaurantRepo repo;

    private final RestaurantSearchAdapter adapter;
    private ArrayAdapter<String> typeAdapter;

    private static class SearchTask extends AsyncTask<String, Void, LiveData<List<Report>>> {

        interface Callback {
            void onSuccess(LiveData<List<Report>> reports);
        }

        private final RestaurantRepo repo;
        private final Callback callback;

        public SearchTask(RestaurantRepo repo, Callback callback) {
            this.repo = repo;
            this.callback = callback;
        }

        @Override
        protected LiveData<List<Report>> doInBackground(String... params) {
            return repo.getRestaurants(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(LiveData<List<Report>> reports) {
            callback.onSuccess(reports);
        }
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = MainApplication.getInstance().getRepository();
        typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, MainApplication.GetSearchResType());
    }

    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_search, container, false);
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialAutoCompleteTextView tv_type = view.findViewById(R.id.tv_restaurant_type);
        TextInputEditText queryEdt = view.findViewById(R.id.tv_query);
        queryEdt.setText(MainApplication.searchObj.getKeyword());
        tv_type.setText(MainApplication.searchObj.getType());
        Button btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(it -> {
            SearchTask task = new SearchTask(repo, reports -> reports.observe(getViewLifecycleOwner(), adapter::setItems));
            String keySearch = queryEdt.getText().toString();
            String restaurantType = tv_type.getText().toString();
            MainApplication.searchObj.setKeyword(keySearch);
            MainApplication.searchObj.setType(restaurantType);
            if ("All".equals(restaurantType)) {
                restaurantType = null;
            }
            task.execute(keySearch, restaurantType);
        });
        ((MaterialAutoCompleteTextView) view.findViewById(R.id.tv_restaurant_type)).setAdapter(typeAdapter);

        RecyclerView reportRcv = getView().findViewById(R.id.rcv_restaurant);
        reportRcv.setAdapter(this.adapter);
        btnSearch.performClick();
    }

    public void onResume() {
        super.onResume();
    }

    public RestaurantSearchFragment() {
        this.adapter = new RestaurantSearchAdapter(report -> ReportDetailsActivity.start(requireContext(), report.getId()));
    }
}
