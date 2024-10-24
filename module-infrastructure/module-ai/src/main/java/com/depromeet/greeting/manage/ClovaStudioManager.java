package com.depromeet.greeting.manage;

import static com.depromeet.constant.ClovaStudioChatCompletionPrompt.SYSTEM_CONTENT;

import com.depromeet.config.ClovaStudioProperties;
import com.depromeet.dto.ai.ChatCompletionsRequest;
import com.depromeet.dto.ai.SummaryRequest;
import com.depromeet.greeting.port.out.AIPort;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaStudioManager implements AIPort {
    private final RestTemplate restTemplate;
    private final ClovaStudioProperties clovaStudioProperties;

    @Override
    public String getSummary(String inputText) {
        final HttpHeaders headers = getHttpHeaders();
        SummaryRequest body = SummaryRequest.of(new String[] {inputText});
        final HttpEntity<SummaryRequest> httpEntity = new HttpEntity<>(body, headers);

        String baseURL = clovaStudioProperties.baseUrl();
        String summaryApi = clovaStudioProperties.summaryApi();

        ResponseEntity<Map> response =
                restTemplate.postForEntity(baseURL + "/" + summaryApi, httpEntity, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
        return result.get("text").toString();
    }

    @Override
    public String getChatCompletions() {
        final HttpHeaders headers = getHttpHeaders();
        ChatCompletionsRequest body = ChatCompletionsRequest.of(SYSTEM_CONTENT.getPrompt());
        final HttpEntity<ChatCompletionsRequest> httpEntity = new HttpEntity<>(body, headers);

        String baseURL = clovaStudioProperties.baseUrl();
        String chatCompletionsApi = clovaStudioProperties.chatCompletionsApi();

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        baseURL + "/" + chatCompletionsApi, httpEntity, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
        Map<String, Object> message = (Map<String, Object>) result.get("message");
        return message.get("content").toString().replaceAll("^\"|\"$", "");
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioProperties.X_NCP_CLOVASTUDIO_API_KEY());
        headers.add("X-NCP-APIGW-API-KEY", clovaStudioProperties.X_NCP_APIGW_API_KEY());
        return headers;
    }
}
