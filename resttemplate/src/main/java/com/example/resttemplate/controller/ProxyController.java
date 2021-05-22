package com.example.resttemplate.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
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

    @GetMapping("/foos-exchange")
    public ResponseEntity<Object> getFoosExchange(@RequestBody List<String> stringList) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<String>> request = new HttpEntity<>(stringList,null);
        ResponseEntity<Foo[]> response = restTemplate
                .exchange(getListFoo, HttpMethod.POST, request, Foo[].class);
        Foo[] foo = response.getBody();
        return ResponseEntity.ok(foo);
    }
}

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
class Foo implements Serializable {
    private long id;

    private String name;
}

