package com.irate.data.model;

import androidx.annotation.FloatRange;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

@Entity
public final class Report implements Serializable {
    @PrimaryKey(
            autoGenerate = true
    )
    private final long id;
    @NotNull
    private final String name;
    @NotNull
    private final String type;
    @NotNull
    private final Date reportTime;
    private final float price;
    @Embedded
    @NotNull
    private final Report.Rating rating;
    @NotNull
    private final String note;
    @NotNull
    private final String reporter;

    public final long getId() {
        return this.id;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final String getType() {
        return this.type;
    }

    @NotNull
    public final Date getReportTime() {
        return this.reportTime;
    }

    public final float getPrice() {
        return this.price;
    }

    @NotNull
    public final Report.Rating getRating() {
        return this.rating;
    }

    @NotNull
    public final String getNote() {
        return this.note;
    }

    @NotNull
    public final String getReporter() {
        return this.reporter;
    }

    public Report(long id, @NotNull String name, @NotNull String type, @NotNull Date reportTime, float price, @NotNull Report.Rating rating, @NotNull String note, @NotNull String reporter) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.reportTime = reportTime;
        this.price = price;
        this.rating = rating;
        this.note = note;
        this.reporter = reporter;
    }

    public final long component1() {
        return this.id;
    }

    @NotNull
    public final String component2() {
        return this.name;
    }

    @NotNull
    public final String component3() {
        return this.type;
    }

    @NotNull
    public final Date component4() {
        return this.reportTime;
    }

    public final float component5() {
        return this.price;
    }

    @NotNull
    public final Report.Rating component6() {
        return this.rating;
    }

    @NotNull
    public final String component7() {
        return this.note;
    }

    @NotNull
    public final String component8() {
        return this.reporter;
    }

    @NotNull
    public final Report copy(long id, @NotNull String name, @NotNull String type, @NotNull Date reportTime, float price, @NotNull Report.Rating rating, @NotNull String note, @NotNull String reporter) {
        return new Report(id, name, type, reportTime, price, rating, note, reporter);
    }

    // $FF: synthetic method
    public static Report copy$default(Report var0, long var1, String var3, String var4, Date var5, float var6, Report.Rating var7, String var8, String var9, int var10, Object var11) {
        if ((var10 & 1) != 0) {
            var1 = var0.id;
        }

        if ((var10 & 2) != 0) {
            var3 = var0.name;
        }

        if ((var10 & 4) != 0) {
            var4 = var0.type;
        }

        if ((var10 & 8) != 0) {
            var5 = var0.reportTime;
        }

        if ((var10 & 16) != 0) {
            var6 = var0.price;
        }

        if ((var10 & 32) != 0) {
            var7 = var0.rating;
        }

        if ((var10 & 64) != 0) {
            var8 = var0.note;
        }

        if ((var10 & 128) != 0) {
            var9 = var0.reporter;
        }

        return var0.copy(var1, var3, var4, var5, var6, var7, var8, var9);
    }

    @NotNull
    public String toString() {
        return "Report(id=" + this.id + ", name=" + this.name + ", type=" + this.type + ", reportTime=" + this.reportTime + ", price=" + this.price + ", rating=" + this.rating + ", note=" + this.note + ", reporter=" + this.reporter + ")";
    }

    public int hashCode() {
        long var10000 = this.id;
        int var1 = (int) (var10000 ^ var10000 >>> 32) * 31;
        String var10001 = this.name;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        var10001 = this.type;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        Date var2 = this.reportTime;
        var1 = ((var1 + (var2 != null ? var2.hashCode() : 0)) * 31 + Float.floatToIntBits(this.price)) * 31;
        Report.Rating var3 = this.rating;
        var1 = (var1 + (var3 != null ? var3.hashCode() : 0)) * 31;
        var10001 = this.note;
        var1 = (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31;
        var10001 = this.reporter;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof Report) {
                Report var2 = (Report) var1;
                if (this.id == var2.id && Intrinsics.areEqual(this.name, var2.name) && Intrinsics.areEqual(this.type, var2.type) && Intrinsics.areEqual(this.reportTime, var2.reportTime) && Float.compare(this.price, var2.price) == 0 && Intrinsics.areEqual(this.rating, var2.rating) && Intrinsics.areEqual(this.note, var2.note) && Intrinsics.areEqual(this.reporter, var2.reporter)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static final class Rating implements Serializable {
        private final transient float average;
        private final float service;
        private final float cleanliness;
        private final float food;

        public final float getAverage() {
            return this.average;
        }

        public final float getService() {
            return this.service;
        }

        public final float getCleanliness() {
            return this.cleanliness;
        }

        public final float getFood() {
            return this.food;
        }

        public Rating(@FloatRange(from = 0.5D, to = 5.0D) float service, @FloatRange(from = 0.5D, to = 5.0D) float cleanliness, @FloatRange(from = 0.5D, to = 5.0D) float food) {
            this.service = service;
            this.cleanliness = cleanliness;
            this.food = food;
            this.average = (this.service + this.cleanliness + this.food) / (float) 3;
        }

        public final float component1() {
            return this.service;
        }

        public final float component2() {
            return this.cleanliness;
        }

        public final float component3() {
            return this.food;
        }

        @NotNull
        public final Report.Rating copy(@FloatRange(from = 0.5D, to = 5.0D) float service, @FloatRange(from = 0.5D, to = 5.0D) float cleanliness, @FloatRange(from = 0.5D, to = 5.0D) float food) {
            return new Report.Rating(service, cleanliness, food);
        }

        // $FF: synthetic method
        public static Report.Rating copy$default(Report.Rating var0, float var1, float var2, float var3, int var4, Object var5) {
            if ((var4 & 1) != 0) {
                var1 = var0.service;
            }

            if ((var4 & 2) != 0) {
                var2 = var0.cleanliness;
            }

            if ((var4 & 4) != 0) {
                var3 = var0.food;
            }

            return var0.copy(var1, var2, var3);
        }

        @NotNull
        public String toString() {
            return "Rating(service=" + this.service + ", cleanliness=" + this.cleanliness + ", food=" + this.food + ")";
        }

        public int hashCode() {
            return (Float.floatToIntBits(this.service) * 31 + Float.floatToIntBits(this.cleanliness)) * 31 + Float.floatToIntBits(this.food);
        }

        public boolean equals(@Nullable Object var1) {
            if (this != var1) {
                if (var1 instanceof Report.Rating) {
                    Report.Rating var2 = (Report.Rating) var1;
                    if (Float.compare(this.service, var2.service) == 0 && Float.compare(this.cleanliness, var2.cleanliness) == 0 && Float.compare(this.food, var2.food) == 0) {
                        return true;
                    }
                }

                return false;
            } else {
                return true;
            }
        }
    }
}
