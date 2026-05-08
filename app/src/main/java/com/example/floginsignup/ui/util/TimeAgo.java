package com.example.floginsignup.ui.util;

import android.content.Context;

import com.example.floginsignup.R;

public final class TimeAgo {
    private TimeAgo() {}

    public static String format(Context ctx, long timestampMs) {
        long diff = System.currentTimeMillis() - timestampMs;
        if (diff < 60_000) return ctx.getString(R.string.time_just_now);
        long minutes = diff / 60_000;
        if (minutes < 60) {
            return minutes == 1
                    ? ctx.getString(R.string.time_minute_ago, 1)
                    : ctx.getString(R.string.time_minutes_ago, (int) minutes);
        }
        long hours = minutes / 60;
        return hours == 1
                ? ctx.getString(R.string.time_hour_ago, 1)
                : ctx.getString(R.string.time_hours_ago, (int) hours);
    }
}
