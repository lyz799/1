package jsu.lyz.teahouse_ks.Controller;

import jsu.lyz.teahouse_ks.Model.Members;
import jsu.lyz.teahouse_ks.Service.MemberService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 添加成员
    @PostMapping("/add")
    public String addMember(@RequestBody Members member) {
        try {
            memberService.addMember(member);
            return "添加成功!";
        } catch (Exception e) {
            return "错误: " + e.getMessage();
        }
    }

    // 更新成员
    @PutMapping("/update")
    public String updateMember(@RequestBody Members member) {

        try {
            memberService.updateMember(member);
            return "更新成功!";
        } catch (Exception e) {
            return "错误: " + e.getMessage();
        }
    }

    // 删除成员
    @DeleteMapping("/delete/{id}")
    public String deleteMember(@PathVariable int id) {
        try {
            memberService.deleteMember(id);
            return "删除成功!";
        } catch (Exception e) {
            return "错误: " + e.getMessage();
        }
    }

    // 获取成员
    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable int id) {
        try {
            Members member = memberService.getMember(id);
            if (member != null) {
                return ResponseEntity.ok(member);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessage("ID不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("ID不存在"));
        }
    }

    // 用于包装错误消息的类
    @Data
    public static class ErrorMessage {
        private String message;

        public ErrorMessage(String message) {
            this.message = message;
        }
    }


    // 根据手机号获取成员
    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getMemberByPhone(@PathVariable String phone){
        try {
            Members member = memberService.getMemberByPhone(phone);
            if (member != null) {
                return ResponseEntity.ok(member);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessage("手机号不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("手机号不存在"));
        }
    }


    // 获取所有成员
    @GetMapping("/all")
    public List<Members> getAllMembers() {
        return memberService.getAllMembers();
    }
}
