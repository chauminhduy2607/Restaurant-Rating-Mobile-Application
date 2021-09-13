package com.irate.nguyet.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.irate.data.model.Report;
import com.irate.nguyet.common.DateConverter;

import org.jetbrains.annotations.NotNull;


@Database(
        entities = {Report.class},
        version = 1
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    @NotNull
    public abstract RestaurantDao restaurantDao();
}
