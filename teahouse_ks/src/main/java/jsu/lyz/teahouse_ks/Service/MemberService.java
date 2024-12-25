package jsu.lyz.teahouse_ks.Service;

import jsu.lyz.teahouse_ks.Dao.MemberDAO;
import jsu.lyz.teahouse_ks.Model.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service  // 确保这里加了 @Service 注解
public class MemberService {

    @Autowired
    private MemberDAO memberDAO;

    // 添加成员
    public void addMember(Members member) throws Exception {
        memberDAO.addMember(member);
    }

    // 更新成员
    public void updateMember(Members member) throws Exception {
        memberDAO.updateMember(member);
    }

    // 删除成员
    public void deleteMember(int id) throws Exception {
        memberDAO.deleteMember(id);
    }

    // 获取成员
    public Members getMember(int id) throws Exception {
        return memberDAO.getMember(id);
    }

    // 获取所有成员
    public List<Members> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    public Members getMemberByPhone(String phone) throws SQLException {
        return memberDAO.getMemberByPhone(phone);
    }

}
