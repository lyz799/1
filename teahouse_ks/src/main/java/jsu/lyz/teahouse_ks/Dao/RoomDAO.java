package jsu.lyz.teahouse_ks.Dao;

import jsu.lyz.teahouse_ks.Model.Room;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/teahouse_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 检查 room_id 是否存在
    public boolean isRoomIdExists(int roomId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // 添加房间
    public void addRoom(Room room) throws SQLException {
        System.out.println("添加会员："+room);
        if (isRoomIdExists(room.getRoomId())) {
            throw new SQLException("Room ID 已存在");
        }

        String sql = "INSERT INTO rooms (room_id, name, capacity, available, remaining_usage_time, updated_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, room.getRoomId());
            stmt.setString(2, room.getName());
            stmt.setInt(3, room.getCapacity());
            stmt.setBoolean(4, room.isAvailable());
            stmt.setInt(5, room.getRemainingUsageTime());
            stmt.setTimestamp(6, Timestamp.valueOf(room.getUpdatedTime()));
            stmt.executeUpdate();
        }
    }

    // 更新房间信息
    public void updateRoom(Room room) throws SQLException {
        System.out.println("更新id"+room.getRoomId());
        if (!isRoomIdExists(room.getRoomId())) {
            throw new SQLException("Room ID 不存在");
        }
        String sql = "UPDATE rooms SET name = ?, capacity = ?, available = ?, remaining_usage_time = ?, updated_time = ? WHERE room_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.setBoolean(3, room.isAvailable());
            stmt.setInt(4, room.getRemainingUsageTime());
            stmt.setTimestamp(5, Timestamp.valueOf(room.getUpdatedTime()));
            stmt.setInt(6, room.getRoomId());
            stmt.executeUpdate();
        }
    }

    // 删除房间
    public void deleteRoom(int roomId) throws SQLException {
        System.out.println("删除信息："+roomId);
        if (!isRoomIdExists(roomId)) {
            throw new SQLException("Room ID 不存在");
        }
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
        }
    }

    // 获取单个房间信息
    public Room getRoom(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("查找id"+roomId);
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getBoolean("available"),
                        rs.getInt("remaining_usage_time"),
                        rs.getTimestamp("updated_time").toLocalDateTime()
                );
            }
        }
        return null;
    }

    public List<Room> getRoomsByRemainingUsageTimeLessThan(int remainingUsageTime) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE remaining_usage_time < ?";
        System.out.println("Executing SQL: " + sql + " with remainingUsageTime = " + remainingUsageTime);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, remainingUsageTime); // 设置查询条件
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getBoolean("available"),
                        rs.getInt("remaining_usage_time"),
                        rs.getTimestamp("updated_time").toLocalDateTime()
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }


    // 获取所有房间信息
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("获取所有成员信息");
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getBoolean("available"),
                        rs.getInt("remaining_usage_time"),
                        rs.getTimestamp("updated_time").toLocalDateTime()
                );
                rooms.add(room);
            }
        }
        return rooms;
    }
}
