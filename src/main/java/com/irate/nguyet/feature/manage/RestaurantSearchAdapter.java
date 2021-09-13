package com.irate.nguyet.feature.manage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.irate.nguyet.MainApplication;
import com.irate.nguyet.R;
import com.irate.data.model.Report;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public final class RestaurantSearchAdapter extends RecyclerView.Adapter<RestaurantSearchAdapter.RestaurantViewHolder> {

    private final List<Report> items;

    private final Consumer<Report> itemClickListener;

    public final void setItems(@NonNull List<Report> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull RestaurantSearchAdapter.RestaurantViewHolder holder, int position) {
        holder.bind(this.items.get(position));
    }

    public int getItemCount() {
        return this.items.size();
    }

    public RestaurantSearchAdapter(Consumer<Report> itemClickListener) {
        this.items = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantSearchAdapter.RestaurantViewHolder(layout, itemClickListener);
    }

    public static final class RestaurantViewHolder extends ViewHolder {

        private Consumer<Report> itemClickListener;

        public final void bind(@NonNull Report item) {
            View itemView = this.itemView;
            TextView childView = itemView.findViewById(R.id.tv_name);
            childView.setText(item.getName());
            itemView = this.itemView;
            childView = itemView.findViewById(R.id.tv_average_rating);
            childView.setText(item.getType());
            itemView = this.itemView;
            childView = itemView.findViewById(R.id.tv_report_time);
            childView.setText(DateFormat.getDateTimeInstance().format(item.getReportTime()));
            itemView = this.itemView;
            childView = itemView.findViewById(R.id.tv_price);
            childView.setText("Price: " + MainApplication.getInstance().getPriceFormat().format(item.getPrice()));
            itemView = this.itemView;
            childView = itemView.findViewById(R.id.tv_reporter);
            childView.setText(item.getReporter());
            itemView = this.itemView;
            RatingBar var3 = itemView.findViewById(R.id.rating_bar);
            Report.Rating var10001 = item.getRating();
            var3.setRating(var10001.getAverage());
            this.itemView.setOnClickListener(v -> {
                itemClickListener.accept(item);
            });
        }

        public RestaurantViewHolder(@NonNull View view, Consumer<Report> itemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
        }
    }
}
