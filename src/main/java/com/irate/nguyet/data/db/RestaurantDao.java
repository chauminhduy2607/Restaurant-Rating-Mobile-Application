package com.irate.nguyet.data.db;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.irate.data.model.Report;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Query("delete from report where id = :id")
    void deleteReport(long id);

    @Query("select * from report where name = :name and reporter = :reporter")
    Report getByNameAndReporter(@Nullable String name, @Nullable String reporter);

    @Query("select * from report where name = :name and reporter = :reporter and id != :id limit 1")
    Report getByNameAndReporterButNotInId(@Nullable String name, @Nullable String reporter, long id);

    @Query("select * from report where id = :id")
    LiveData<Report> getReport(long id);

    @Query("select * from report where (:query is null or name like '%' || :query || '%') and (:type is null or type like :type)")
    LiveData<List<Report>> getReports(@Nullable String query, @Nullable String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createReport(@NotNull Report report);
}
