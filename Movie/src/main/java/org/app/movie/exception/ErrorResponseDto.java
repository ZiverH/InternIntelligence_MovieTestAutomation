package org.app.movie.exception;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ErrorResponseDto {
    private int code;
    private OffsetDateTime timestamp;
    private String message;
    private String detail;
    private String path;
    @Builder.Default
    private Map<String,String> data=new HashMap<>();



}
