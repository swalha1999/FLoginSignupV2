package com.example.floginsignup.model;

import java.util.List;

public class ParkingState {
    public final boolean gateOpen;
    public final int entryCount;
    public final int exitCount;
    public final List<ParkingRow> rows;
    public final int occupied;
    public final int available;
    public final int reserved;

    public ParkingState(boolean gateOpen, int entryCount, int exitCount,
                        List<ParkingRow> rows, int occupied, int available, int reserved) {
        this.gateOpen = gateOpen;
        this.entryCount = entryCount;
        this.exitCount = exitCount;
        this.rows = rows;
        this.occupied = occupied;
        this.available = available;
        this.reserved = reserved;
    }

    public int total() { return occupied + available + reserved; }
    public int occupancyPercent() {
        int t = total();
        return t == 0 ? 0 : Math.round((occupied * 100f) / t);
    }
}
