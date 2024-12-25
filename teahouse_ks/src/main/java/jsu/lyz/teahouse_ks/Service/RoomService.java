package jsu.lyz.teahouse_ks.Service;

import jsu.lyz.teahouse_ks.Dao.RoomDAO;
import jsu.lyz.teahouse_ks.Model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomDAO roomDAO;

    // 添加房间
    public void addRoom(Room room) throws SQLException {
        roomDAO.addRoom(room);
    }

    // 更新房间
    public void updateRoom(Room room) throws SQLException {
        roomDAO.updateRoom(room);
    }

    // 删除房间
    public void deleteRoom(int roomId) throws SQLException {
        roomDAO.deleteRoom(roomId);
    }

    // 获取房间信息
    public Room getRoom(int roomId) throws SQLException {
        return roomDAO.getRoom(roomId);
    }

    public List<Room> getRoomsByRemainingUsageTimeLessThan(int remainingUsageTime) {
        return roomDAO.getRoomsByRemainingUsageTimeLessThan(remainingUsageTime);
    }


    // 获取所有房间信息
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }
}
