package com.irate.nguyet.common;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.Date;

public final class DateConverter {
    @TypeConverter
    @Nullable
    public final Date fromTimestamp(@Nullable Long time) {
        Date date;
        if (time != null) {
            date = new Date(time);
        } else {
            date = null;
        }
        return date;
    }

    @TypeConverter
    @Nullable
    public final Long dateToTimestamp(@Nullable Date date) {
        return date != null ? date.getTime() : null;
    }
}
