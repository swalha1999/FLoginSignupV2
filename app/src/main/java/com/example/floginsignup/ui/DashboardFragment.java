package com.example.floginsignup.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.floginsignup.R;
import com.example.floginsignup.api.ApiCallback;
import com.example.floginsignup.api.ApiClient;
import com.example.floginsignup.model.ActivityItem;
import com.example.floginsignup.model.DashboardData;
import com.example.floginsignup.ui.util.ActivityIcons;
import com.example.floginsignup.ui.util.TimeAgo;
import com.example.floginsignup.ui.widget.StackedBarView;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private TextView tvGreeting, tvTotal, tvOccupied, tvAvailable, tvRevenue;
    private TextView tvLegendOccupied, tvLegendReserved, tvLegendFree;
    private StackedBarView stackedBar;
    private LinearLayout recentActivityContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvOccupied = view.findViewById(R.id.tvOccupied);
        tvAvailable = view.findViewById(R.id.tvAvailable);
        tvRevenue = view.findViewById(R.id.tvRevenue);
        tvLegendOccupied = view.findViewById(R.id.tvLegendOccupied);
        tvLegendReserved = view.findViewById(R.id.tvLegendReserved);
        tvLegendFree = view.findViewById(R.id.tvLegendFree);
        stackedBar = view.findViewById(R.id.stackedBar);
        recentActivityContainer = view.findViewById(R.id.recentActivityContainer);

        tvGreeting.setText(greetingForTime());
        loadData();
    }

    private void loadData() {
        ApiClient.get().getDashboard(new ApiCallback<DashboardData>() {
            @Override public void onSuccess(DashboardData d) { bind(d); }
            @Override public void onError(Throwable e) { /* show empty */ }
        });
    }

    private void bind(DashboardData d) {
        if (!isAdded()) return;
        tvTotal.setText(String.valueOf(d.totalSpots));
        tvOccupied.setText(String.valueOf(d.occupied));
        tvAvailable.setText(String.valueOf(d.available));
        tvRevenue.setText(formatCurrency(d.revenue));

        tvLegendOccupied.setText(getString(R.string.legend_occupied, d.occupied));
        tvLegendReserved.setText(getString(R.string.legend_reserved, d.reserved));
        tvLegendFree.setText(getString(R.string.legend_free, d.available));

        stackedBar.setData(
                new float[]{d.occupied, d.reserved, d.available},
                new int[]{Color.parseColor("#EF4444"), Color.parseColor("#F59E0B"), Color.parseColor("#22C55E")}
        );

        recentActivityContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (ActivityItem item : d.recentActivity) {
            View row = inflater.inflate(R.layout.item_activity, recentActivityContainer, false);
            bindActivityRow(row, item);
            recentActivityContainer.addView(row);
        }
    }

    private void bindActivityRow(View row, ActivityItem item) {
        ImageView bg = row.findViewById(R.id.imgActivityBg);
        ImageView icon = row.findViewById(R.id.imgActivityIcon);
        TextView title = row.findViewById(R.id.tvActivityTitle);
        TextView subtitle = row.findViewById(R.id.tvActivitySubtitle);

        bg.setBackgroundResource(ActivityIcons.bgFor(item.type));
        icon.setImageResource(ActivityIcons.iconFor(item.type));
        icon.setImageTintList(ContextCompat.getColorStateList(requireContext(), ActivityIcons.tintFor(item.type)));
        title.setText(item.title);
        subtitle.setText(TimeAgo.format(requireContext(), item.timestampMs));
    }

    private String greetingForTime() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h < 12) return getString(R.string.greeting_morning);
        if (h < 18) return "Good afternoon";
        return "Good evening";
    }

    private String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        return nf.format(amount);
    }
}
