package com.depromeet.reaction.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.reaction.facade.ReactionFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(ReactionController.class)
public class ReactionControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;
    @MockBean ReactionFacade reactionFacade;

    @Test
    @WithCustomMockMember
    public void 응원을_등록합니다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("memoryId", 1);
        requestBody.put("emoji", "🦭");
        requestBody.put("comment", "물개세요?");

        mockMvc.perform(
                        post("/memory/reaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("REACTION_1"))
                .andExpect(jsonPath("$.message").value("응원 등록에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 응원_등록_가능여부를_조회합니다() throws Exception {
        mockMvc.perform(get("/memory/{memoryId}/reaction/eligibility", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("REACTION_4"))
                .andExpect(jsonPath("$.message").value("응원 생성 여부 검증에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 기록의_응원들을_조회합니다() throws Exception {
        mockMvc.perform(get("/memory/{memoryId}/reactions", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("REACTION_2"))
                .andExpect(jsonPath("$.message").value("응원 목록 조회에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 응원을_페이지_단위로_조회합니다() throws Exception {
        mockMvc.perform(get("/memory/{memoryId}/reactions/detail", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("REACTION_3"))
                .andExpect(jsonPath("$.message").value("응원 상세 조회에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 응원을_삭제합니다() throws Exception {
        mockMvc.perform(delete("/memory/reaction/{reactionId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("REACTION_5"))
                .andExpect(jsonPath("$.message").value("응원 삭제에 성공하였습니다"))
                .andDo(print());
    }
}
