package com.irate.nguyet.common;


import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;


public final class DateExt {

    @NonNull
    public static Date truncateSeconds(@NonNull Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
