package com.example.uploadbucket.modle;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestDTO {
    @NotBlank
    private String name;

    @Max(value = 10)
    @NotNull
    private Long id;
}
