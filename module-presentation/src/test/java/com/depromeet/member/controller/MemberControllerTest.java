package com.depromeet.member.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.member.api.MemberController;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.facade.MemberFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean MemberFacade memberFacade;

    @Test
    @WithCustomMockMember
    void 회원의_이름을_수정합니다() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "테스트");

        Member member = new Member(1L, 3000, "테스트", "test@gmail.com", MemberRole.USER, "aa");
        when(memberFacade.updateName(anyLong(), anyString())).thenReturn(member);

        mockMvc.perform(
                        patch("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_4"))
                .andExpect(jsonPath("$.message").value("멤버 이름 수정에 성공하였습니다"))
                .andExpect(jsonPath("$.data.name").value("테스트"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 회원의_이름은_Null을_허용하지_않습니다() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", null);

        mockMvc.perform(
                        patch("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON_1"))
                .andExpect(jsonPath("$.message").value("입력 값 검증에 실패하였습니다"))
                .andExpect(jsonPath("$.data.fieldErrors[0].reason").value("must not be blank"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 회원의_이름은_공백을_허용하지_않습니다() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "");

        mockMvc.perform(
                        patch("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON_1"))
                .andExpect(jsonPath("$.message").value("입력 값 검증에 실패하였습니다"))
                .andExpect(jsonPath("$.data.fieldErrors[0].reason").value("must not be blank"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 회원의_이름은_빈문자열을_허용하지_않습니다() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", " ");

        mockMvc.perform(
                        patch("/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON_1"))
                .andExpect(jsonPath("$.message").value("입력 값 검증에 실패하였습니다"))
                .andExpect(jsonPath("$.data.fieldErrors[0].reason").value("must not be blank"))
                .andDo(print());
    }
}
