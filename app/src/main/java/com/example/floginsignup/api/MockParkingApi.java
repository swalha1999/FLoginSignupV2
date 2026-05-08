package com.example.floginsignup.api;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.example.floginsignup.model.ActivityItem;
import com.example.floginsignup.model.DashboardData;
import com.example.floginsignup.model.ParkingRow;
import com.example.floginsignup.model.ParkingSpot;
import com.example.floginsignup.model.ParkingState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockParkingApi implements ParkingApi {

    private static final long FAKE_LATENCY_MS = 250;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void getDashboard(ApiCallback<DashboardData> cb) {
        handler.postDelayed(() -> cb.onSuccess(buildDashboard()), FAKE_LATENCY_MS);
    }

    @Override
    public void getParkingState(ApiCallback<ParkingState> cb) {
        handler.postDelayed(() -> cb.onSuccess(buildParkingState()), FAKE_LATENCY_MS);
    }

    @Override
    public void getActivityLog(ApiCallback<List<ActivityItem>> cb) {
        handler.postDelayed(() -> cb.onSuccess(buildActivityLog()), FAKE_LATENCY_MS);
    }

    @Override
    public void openGate(GateCallback cb) {
        handler.postDelayed(() -> {
            cb.onOpened();
            handler.postDelayed(cb::onClosed, GATE_OPEN_DURATION_MS);
        }, FAKE_LATENCY_MS);
    }

    private DashboardData buildDashboard() {
        return new DashboardData(20, 14, 4, 2, 2840.0, buildActivityLog().subList(0, 3));
    }

    private ParkingState buildParkingState() {
        List<ParkingSpot> rowA = Arrays.asList(
                new ParkingSpot("A1", "A", ParkingSpot.Status.OCCUPIED, Color.parseColor("#3B82F6")),
                new ParkingSpot("A2", "A", ParkingSpot.Status.FREE, 0),
                new ParkingSpot("A3", "A", ParkingSpot.Status.OCCUPIED, Color.parseColor("#EF4444")),
                new ParkingSpot("A4", "A", ParkingSpot.Status.OCCUPIED, Color.parseColor("#F59E0B")),
                new ParkingSpot("A5", "A", ParkingSpot.Status.FREE, 0)
        );
        List<ParkingSpot> rowB = Arrays.asList(
                new ParkingSpot("B1", "B", ParkingSpot.Status.FREE, 0),
                new ParkingSpot("B2", "B", ParkingSpot.Status.OCCUPIED, Color.parseColor("#22C55E")),
                new ParkingSpot("B3", "B", ParkingSpot.Status.FREE, 0),
                new ParkingSpot("B4", "B", ParkingSpot.Status.FREE, 0),
                new ParkingSpot("B5", "B", ParkingSpot.Status.OCCUPIED, Color.parseColor("#A855F7"))
        );
        List<ParkingRow> rows = Arrays.asList(
                new ParkingRow("ROW A", rowA),
                new ParkingRow("ROW B", rowB)
        );
        return new ParkingState(false, 8, 5, rows, 14, 4, 2);
    }

    private List<ActivityItem> buildActivityLog() {
        long now = System.currentTimeMillis();
        List<ActivityItem> list = new ArrayList<>();
        list.add(new ActivityItem("1", ActivityItem.Type.CAR_ENTERED,
                "Car entered · Spot A1", "Toyota Camry", now - minutes(2)));
        list.add(new ActivityItem("2", ActivityItem.Type.CAR_EXITED,
                "Car exited · Spot B5", "Honda Civic", now - minutes(15)));
        list.add(new ActivityItem("3", ActivityItem.Type.SUBSCRIPTION,
                "New subscription · Al Hassan", "Monthly Plan", now - minutes(10)));
        list.add(new ActivityItem("4", ActivityItem.Type.GATE_OPENED,
                "Gate opened manually", "Admin", now - minutes(32)));
        list.add(new ActivityItem("5", ActivityItem.Type.CAR_ENTERED,
                "Car entered · Spot B3", "BMW X5", now - hours(1)));
        list.add(new ActivityItem("6", ActivityItem.Type.CAR_EXITED,
                "Car exited · Spot A4", "Nissan Altima", now - hours(1)));
        return list;
    }

    private long minutes(int m) { return m * 60_000L; }
    private long hours(int h) { return h * 3_600_000L; }
}
