package com.example.floginsignup.api;

import com.example.floginsignup.model.ActivityItem;
import com.example.floginsignup.model.DashboardData;
import com.example.floginsignup.model.ParkingState;

import java.util.List;

/**
 * Data source contract for the parking app.
 * Swap MockParkingApi for a real implementation (Retrofit, Firebase, gRPC...) without
 * touching UI code by replacing the singleton inside ApiClient.
 */
public interface ParkingApi {
    void getDashboard(ApiCallback<DashboardData> cb);
    void getParkingState(ApiCallback<ParkingState> cb);
    void getActivityLog(ApiCallback<List<ActivityItem>> cb);
}
