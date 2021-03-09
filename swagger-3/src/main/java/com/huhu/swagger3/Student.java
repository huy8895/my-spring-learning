package com.huhu.swagger3;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Student {
    @Schema(description = "student ID", example = "01")
    private long id;

    @Schema(description = "student firstName", example = "huy")
    private String firstName;

    @Size(max = 10)
    @Schema(description = "student lastName", example = "trinh van")
    private String lastName;
}
