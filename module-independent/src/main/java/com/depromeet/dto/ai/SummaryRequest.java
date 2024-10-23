package com.depromeet.dto.ai;

public record SummaryRequest(String[] texts) {
    public static SummaryRequest of(String[] texts) {
        return new SummaryRequest(texts);
    }
}
