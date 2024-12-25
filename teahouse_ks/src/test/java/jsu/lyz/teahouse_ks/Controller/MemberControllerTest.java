package jsu.lyz.teahouse_ks.Controller;

import jsu.lyz.teahouse_ks.Model.Members;
import jsu.lyz.teahouse_ks.Service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void addMemberTest() throws Exception {
        Members member = new Members();
        member.setId(1000000);
        member.setName("John Doe");
        member.setPhone("123456789");

        doNothing().when(memberService).addMember(any(Members.class));

        MvcResult result = mockMvc.perform(post("/members/add")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"id\":1000000, \"name\":\"John Doe\", \"phone\":\"123456789\", \"age\":25, \"address\":\"Some address\"}"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("Response content: " + result.getResponse().getContentAsString());

    }

    @Test
    void updateMemberTest() throws Exception {
        Members member = new Members();
        member.setId(1);
        member.setName("John Doe");
        member.setPhone("123456789");

        doNothing().when(memberService).updateMember(any(Members.class));

        mockMvc.perform(put("/members/update")
                        .contentType("application/json")
                        .content("{\"id\":1, \"name\":\"John Doe\", \"phone\":\"123456789\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("更新成功!"));

        verify(memberService, times(1)).updateMember(any(Members.class));
    }

    @Test
    void deleteMemberTest() throws Exception {
        doNothing().when(memberService).deleteMember(anyInt());

        MvcResult result = mockMvc.perform(put("/members/update")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"id\":1, \"name\":\"John Doe\", \"phone\":\"123456789\", \"age\":25, \"address\":\"Updated address\"}"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("Response content: " + result.getResponse().getContentAsString());

    }

    @Test
    void getMemberTest() throws Exception {
        Members member = new Members();
        member.setId(1);
        member.setName("John Doe");
        member.setPhone("123456789");

        when(memberService.getMember(1)).thenReturn(member);

        mockMvc.perform(get("/members/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phone").value("123456789"));

        verify(memberService, times(1)).getMember(1);
    }

    @Test
    void getMemberNotFoundTest() throws Exception {
        when(memberService.getMember(1)).thenReturn(null);

        mockMvc.perform(get("/members/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ID不存在"));

        verify(memberService, times(1)).getMember(1);
    }

    @Test
    void getMemberByPhoneTest() throws Exception {
        Members member = new Members();
        member.setId(1);
        member.setName("John Doe");
        member.setPhone("123456789");

        when(memberService.getMemberByPhone("123456789")).thenReturn(member);

        mockMvc.perform(get("/members/phone/{phone}", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phone").value("123456789"));

        verify(memberService, times(1)).getMemberByPhone("123456789");
    }

    @Test
    void getMemberByPhoneNotFoundTest() throws Exception {
        when(memberService.getMemberByPhone("123456789")).thenReturn(null);

        mockMvc.perform(get("/members/phone/{phone}", "123456789"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("手机号不存在"));

        verify(memberService, times(1)).getMemberByPhone("123456789");
    }

    @Test
    void getAllMembersTest() throws Exception {
        Members member1 = new Members();
        member1.setId(1);
        member1.setName("John Doe");
        member1.setPhone("123456789");

        Members member2 = new Members();
        member2.setId(2);
        member2.setName("Jane Doe");
        member2.setPhone("987654321");

        when(memberService.getAllMembers()).thenReturn(List.of(member1, member2));

        mockMvc.perform(get("/members/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(memberService, times(1)).getAllMembers();
    }
}
