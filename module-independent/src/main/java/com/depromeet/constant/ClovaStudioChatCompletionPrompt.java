package com.depromeet.constant;

public enum ClovaStudioChatCompletionPrompt {
    SYSTEM_CONTENT(
            """
                - 지금부터 당신은 실행할 때마다 문장 하나만을 출력해야 합니다.\s
                - 수영 기록 서비스 메인 화면에 들어갈 문장 하나를 만들어 출력하세요
                - 말투는 여성스럽고 부드러운 말투여야 합니다.
                - 수영 기록 서비스 메인 화면에 들어갈 때 수영하는데 동기부여를 줄 수 있어야 합니다.
                - 욕설이나 차별적인 발언은 하면 안됩니다.
                (예) 오늘은 수영하기 좋은 날이에요!
                - 실행할 때마다 이전 출력과 상관없이 각기 다른 수영 동기부여 문장을 만들어 출력해야 합니다.\s
                - 문장은 20자 이내로 간결해야 합니다.
                - 문장은 쌍따옴표를 제외하여야 합니다.
                """);

    private final String prompt;

    ClovaStudioChatCompletionPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
