package jsu.lyz.teahouse_ks.Exception;

public class ApiResponse {
    private boolean operate;
    private String message;

    // 构造器
    public ApiResponse(boolean operate, String message) {
        this.operate = operate;
        this.message = message;
    }

    // Getter 和 Setter 方法
    public boolean isSuccess() {
        return operate;
    }

    public void setSuccess(boolean success) {
        this.operate = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

