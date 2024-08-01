package com.depromeet.memory.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.memory.facade.MemoryFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MemoryController.class)
public class MemoryControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean MemoryFacade memoryFacade;

    @Test
    @WithCustomMockMember
    void 수영기록을_생성합니다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("poolId", 1);
        requestBody.put("item", "오리발");
        requestBody.put("heartRate", 115);
        requestBody.put("pace", "05:00:00");
        requestBody.put("kcal", 300);
        requestBody.put("recordAt", "2024-07-24");
        requestBody.put("startTime", "11:00:00");
        requestBody.put("endTime", "11:50:00");
        requestBody.put("lane", 25);
        requestBody.put("diary", "일기를 기록한다");

        List<Map<String, Object>> strokes = new ArrayList<>();
        Map<String, Object> stroke = new HashMap<>();
        stroke.put("name", "자유형");
        stroke.put("laps", 3);
        stroke.put("meter", 150);
        strokes.add(stroke);

        requestBody.put("strokes", strokes);

        List<Long> imageIdList = new ArrayList<>();
        requestBody.put("imageIdList", imageIdList);

        mockMvc.perform(
                        post("/memory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMORY_1"))
                .andExpect(jsonPath("$.message").value("수영 기록 저장에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 소수점_바퀴_수는_5만_가능합니다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("poolId", 1);
        requestBody.put("item", "오리발");
        requestBody.put("heartRate", 115);
        requestBody.put("pace", "05:00:00");
        requestBody.put("kcal", 300);
        requestBody.put("recordAt", "2024-07-24");
        requestBody.put("startTime", "11:00:00");
        requestBody.put("endTime", "11:50:00");
        requestBody.put("lane", 25);
        requestBody.put("diary", "일기를 기록한다");

        List<Map<String, Object>> strokes = new ArrayList<>();
        Map<String, Object> stroke = new HashMap<>();
        stroke.put("name", "자유형");
        stroke.put("laps", 3.3); // Validation Error
        stroke.put("meter", 150);
        strokes.add(stroke);

        requestBody.put("strokes", strokes);

        List<Long> imageIdList = new ArrayList<>();
        requestBody.put("imageIdList", imageIdList);

        mockMvc.perform(
                        post("/memory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON_1"))
                .andExpect(jsonPath("$.message").value("입력 값 검증에 실패하였습니다"))
                .andExpect(
                        jsonPath("$.data.fieldErrors[0].reason").value("바퀴 수는 0.5 단위로만 입력 가능합니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 수영기록을_조회합니다() throws Exception {
        mockMvc.perform(get("/memory/{memoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMORY_2"))
                .andExpect(jsonPath("$.message").value("수영 기록 조회에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 수영기록을_갱신합니다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("poolId", 1);
        requestBody.put("recordAt", "2024-07-24");
        requestBody.put("startTime", "11:00:00");
        requestBody.put("endTime", "11:50:00");
        mockMvc.perform(
                        patch("/memory/{memoryId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMORY_3"))
                .andExpect(jsonPath("$.message").value("수영 기록 수정에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 수영기록_타임라인을_조회합니다() throws Exception {
        mockMvc.perform(
                        get("/memory/timeline")
                                .param("cursorId", "1L")
                                .param("date", "2024-07")
                                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMORY_4"))
                .andExpect(jsonPath("$.message").value("타임라인 조회에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 수영기록_캘린더를_조회합니다() throws Exception {
        mockMvc.perform(get("/memory/calendar").param("year", "2024").param("month", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMORY_5"))
                .andExpect(jsonPath("$.message").value("캘린더 조회에 성공하였습니다"))
                .andDo(print());
    }
}
