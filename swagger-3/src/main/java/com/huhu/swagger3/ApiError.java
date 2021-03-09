package com.huhu.swagger3;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    @Schema(description = "logRef", example = "abcd")
    private String logRef;

    @Schema(description = "message", example = "bad request")
    private String message;
}
