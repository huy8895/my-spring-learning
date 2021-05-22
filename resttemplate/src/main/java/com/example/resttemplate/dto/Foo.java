package com.example.resttemplate.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Foo implements Serializable {
    private long id;

    private String name;
}
