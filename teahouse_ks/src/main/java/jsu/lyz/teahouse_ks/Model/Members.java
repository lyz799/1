package jsu.lyz.teahouse_ks.Model;

public class Members {
    private int id;
    private String name;
    private String phone;
    private int point;
    private String level;

    public Members(int id, String name, String phone, int point, String level) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.point = point;
        this.level = level;
    }

    // 无参构造函数
    public Members() {
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public int getPoint() { return point; }

    public void setPoint(int point) { this.point = point; }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }
}
