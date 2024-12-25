package jsu.lyz.teahouse_ks.Controller;

import jsu.lyz.teahouse_ks.Model.Room;
import jsu.lyz.teahouse_ks.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // 添加房间
    @PostMapping("/add")
    public String addRoom(@RequestBody Room room) {
        try {
            roomService.addRoom(room);
            return "添加成功!";
        } catch (SQLException e) {
            System.out.println("添加失败");
            return "错误: " + e.getMessage();
        }
    }

    // 更新房间
    @PutMapping("/update")
    public String updateRoom(@RequestBody Room room) {
        try {
            roomService.updateRoom(room);
            return "更新成功!";
        } catch (SQLException e) {
            System.out.println("更新失败");
            return "错误: " + e.getMessage();
        }
    }

    // 删除房间
    @DeleteMapping("/delete/{roomId}")
    public String deleteRoom(@PathVariable int roomId) {
        System.out.println("接收到的删除请求，roomId: " + roomId);
        try {
            roomService.deleteRoom(roomId);
            return "删除成功!";
        } catch (SQLException e) {
            System.out.println("删除失败");
            return "错误: " + e.getMessage();
        }
    }


    // 获取所有房间信息
    @GetMapping("/all")
    public List<Room> getAllRooms() {
        try {
            return roomService.getAllRooms();
        } catch (SQLException e) {
            e.printStackTrace();
            // 返回空列表或其他合适的错误响应
            return null;
        }
    }

    // 根据房间ID获取房间信息
    @GetMapping("/id/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable int roomId) {
        try {
            Room room = roomService.getRoom(roomId);
            if (room != null) {
                return ResponseEntity.ok(room); // 返回状态码 200 和房间信息
            } else {
                // 打印日志信息
                System.out.println("房间ID不存在");
                // 返回状态码 404 和错误信息
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房间ID不存在");
            }
        } catch (Exception e) {
            // 打印异常信息（用于调试）
            e.printStackTrace();
            // 返回状态码 500 和错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误，请稍后重试");
        }
    }

    // 根据剩余使用时间小于指定值获取房间信息
    @GetMapping("/remainingUsageTimeLessThan/{remainingUsageTime}")
    public List<Room> getRoomsByRemainingUsageTimeLessThan(@PathVariable int remainingUsageTime) {
        try {
            List<Room> rooms = roomService.getRoomsByRemainingUsageTimeLessThan(remainingUsageTime);
            if (rooms != null && !rooms.isEmpty()) {
                return rooms;
            } else {
                throw new Exception("没有找到符合条件的房间");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // 返回空列表避免前端解析错误
        }
    }
}
