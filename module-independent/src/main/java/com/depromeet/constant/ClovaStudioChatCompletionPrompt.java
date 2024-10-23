package com.depromeet.constant;

public enum ClovaStudioChatCompletionPrompt {
    SYSTEM_CONTENT(
            """
                - 사용자가 1달간의 수영 기록에 대해 입력을 하면 이에 대해 요약을 하세요.
                - 사용자의 입력은 아래와 같은 형식으로 들어옵니다.
                "memories": [
                {"memoryId": 1,"memoryDate": 7,"type": "NORMAL",
                "totalDistance": 175,"imageUrl": "https://image.png","isAchieved": false,
                "strokes": [
                {"strokeId": 1,"name": "자유형","meter": 175}
                ]},
                {"memoryId": 2,"memoryDate": 8,"type": "MUlTI",
                "totalDistance": 300,"imageUrl": "https://image.png","isAchieved": false,
                "strokes": [
                {"strokeId": 1,
                "name": "자유형",
                "meter": 200},{
                "strokeId": 1,
                "name": "평형","meter": 100}]}]
                memories : 한 달간의 수영 기록을 담고 있습니다. memoryDate : 수영한 날짜를 뜻합니다.
                totalDistance : 해당 날짜에 수영한 총 거리입니다. 단위는 m(미터)입니다.strokes : 해당 날짜에 수영한 영법에 대한 정보 리스트입니다.
                아래는 strokes에서 알 수 있는 정보입니다.
                name : 사용한 수영 영법 이름입니다. meter : 해당 영법으로 이동한 거리입니다.
                이외의 정보는 해석에 필요없는 정보이니 넘어가도 됩니다.
                - 요약해야 할 사항은 아래와 같습니다.
                - 1달동안 수영을 한 날의 수 (ex - 1달간 n번 수영을 했어요!)
                - 가장 많이 사용한 영법(ex - 가장 많이 사용한 영법은 '000'으로 총 000m를 이동했어요!)
                """);

    private final String prompt;

    ClovaStudioChatCompletionPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
