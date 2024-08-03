package com.depromeet.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestLog {
    private String createdAt;
    private String item;
    private String action;
    private String result;
    private String uri;
    private String method;
}
