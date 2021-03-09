package com.huhu.swagger3;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Student {
    private long id;
    private String firstName;

    @Size(max = 10)
    private String lastName;
}
