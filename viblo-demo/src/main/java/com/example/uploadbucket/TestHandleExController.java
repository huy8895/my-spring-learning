package com.example.uploadbucket;

import com.example.uploadbucket.modle.RequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestHandleExController {
    @GetMapping("test/{long}")
    public Long test(@PathVariable(value = "long") Long parameter){
        return parameter;
    }

    @PostMapping("test/")
    public ResponseEntity<RequestDTO> testNotValidParameter(@Validated @RequestBody RequestDTO dto){
        return ResponseEntity.ok(dto);
    }
}
