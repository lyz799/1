package jsu.lyz.teahouse_ks.Dao;

import jsu.lyz.teahouse_ks.Model.Members;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberDAO {
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

    // 检查ID是否存在
    public boolean isIdExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM members WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void addMember(Members member) throws Exception {
        if (isPhoneExists(member.getPhone())) {
            throw new Exception("手机号已被注册");
        }
        if (isIdExists(member.getId())) {
            throw new Exception("ID被注册");
        }
        String sql = "INSERT INTO members (id, name, phone, point, level) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, member.getId());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getPhone());
            stmt.setInt(4, member.getPoint());
            stmt.setString(5, member.getLevel());
            stmt.executeUpdate();
        }
    }

    public void updateMember(Members member) throws Exception {
        if (!isIdExists(member.getId())) {
            throw new Exception("ID不存在");
        }
        String sql = "UPDATE members SET name = ?, phone = ?, point = ?, level = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getPhone());
            stmt.setInt(3, member.getPoint());
            stmt.setString(4, member.getLevel());
            stmt.setInt(5, member.getId());
            stmt.executeUpdate();
            int rowsAffected = stmt.executeUpdate();
            System.out.println("更新影响的行数: " + rowsAffected);
        }
    }

    // 检查ID是否存在的方法（已经实现）
    public boolean disIdExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM members WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void deleteMember(int id) throws Exception {
        if (!isIdExists(id)) {
            throw new Exception("ID不存在");
        }
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Members getMember(int id) throws Exception {
        if (!isIdExists(id)) {
            throw new Exception("ID不存在");
        }
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Members(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("point"),
                        rs.getString("level")
                );
            }
        }
        return null;
    }

    // 根据手机号获取成员信息
    public Members getMemberByPhone(String phone) throws SQLException {
        if (!isPhoneExists(phone)) {
            throw new SQLException("手机号已被注册");
        }
        String sql = "SELECT * FROM members WHERE phone = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Members(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("point"),
                        rs.getString("level")
                );
            }
        }
        return null; // 如果没有找到记录，返回 null
    }

    public List<Members> getAllMembers() {
        List<Members> membersList = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Members member = new Members(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("point"),
                        rs.getString("level")
                );
                membersList.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membersList;
    }

    private boolean isPhoneExists(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM members WHERE phone = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
