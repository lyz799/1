package jsu.lyz.teahouse_ks.Model;

import java.time.LocalDateTime;

public class Room {
    private int roomId;
    private String name;
    private int capacity;
    private boolean available;
    private int remainingUsageTime;
    private LocalDateTime updatedTime;

    public Room(int roomId, String name, int capacity, boolean available, int remainingUsageTime, LocalDateTime updatedTime) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.available = available;
        this.remainingUsageTime = remainingUsageTime;
        this.updatedTime = updatedTime;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRemainingUsageTime() {
        return remainingUsageTime;
    }

    public void setRemainingUsageTime(int remainingUsageTime) {
        this.remainingUsageTime = remainingUsageTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
