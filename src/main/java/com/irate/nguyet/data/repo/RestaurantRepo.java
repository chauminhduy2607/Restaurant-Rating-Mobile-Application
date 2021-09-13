package com.irate.nguyet.data.repo;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.irate.nguyet.data.db.AppDatabase;
import com.irate.data.model.Report;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class RestaurantRepo {

    private final AppDatabase db;

    public  final  void deleteReport(long reportId){
        this.db.restaurantDao().deleteReport(reportId);
    }

    public  final  boolean checkDuplicate(@Nullable String name, @Nullable String reporter, long reportId){
        Report res;
        if (reportId <= 0){
            res =  this.db.restaurantDao().getByNameAndReporter(name, reporter);
        }else{
            res =  this.db.restaurantDao().getByNameAndReporterButNotInId(name, reporter, reportId);
        }
        return res != null;
    }

    public final LiveData<List<Report>> getRestaurants(@Nullable String query, @Nullable String type) {
        return this.db.restaurantDao().getReports(query, type);
    }

    public LiveData<Report> getReport(long id) {
        return this.db.restaurantDao().getReport(id);
    }

    public final long createReport(@NotNull Report report) {
        return this.db.restaurantDao().createReport(report);
    }

    public RestaurantRepo(@NotNull AppDatabase db) {
        this.db = db;
    }
}
