package com.example.resttemplate.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/rest-template")
public class ProxyController {
    private final String fooResourceUrl
            = "http://localhost:8082/api/rung-api/test";
    private final String getListFoo
            = "http://localhost:8082/api/rung-api/test-stringList";

    @GetMapping()
    public ResponseEntity<String> get(@RequestParam String name) {
        RestTemplate restTemplate = new RestTemplate();
        String url = fooResourceUrl + "/hello" + name;
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        return forEntity;
    }

    @GetMapping("/foo")
    public ResponseEntity<Foo> getFoo(@RequestBody Foo foodto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = fooResourceUrl + "?id=" + foodto.getId() + "&name=" + foodto.getName();
        Foo foo = restTemplate
                .getForObject(url, Foo.class);
        return ResponseEntity.ok(foo);
    }

    @GetMapping("/foos")
    public ResponseEntity<Object> getFoos(@RequestBody List<String> stringList) {
        RestTemplate restTemplate = new RestTemplate();
        Foo[] foos = restTemplate.postForObject(
                getListFoo,
                stringList,
                Foo[].class);
        return ResponseEntity.ok(foos);
    }
}

@Data
@NoArgsConstructor
class Foo implements Serializable {
    private long id;

    private String name;
}

