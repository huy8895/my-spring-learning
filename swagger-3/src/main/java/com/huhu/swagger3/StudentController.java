package com.huhu.swagger3;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "student controller tag")
public class StudentController {

    @GetMapping("/{id}")
    @Operation(method = "getStudent", description = "lay chi tiet student")
    public ResponseEntity<Student> getStudent(
            @Parameter(required = true, description = "student id", example = "123")
            @PathVariable("id") Long id) {

        if (id == 2) {
            throw new RuntimeException("test exception handler");
        }
        return ResponseEntity.ok(new Student());
    }

    @PostMapping
    @Operation(method = "createNewStudent", description = "tao moi student")
    public ResponseEntity<Student> createNewStudent(
            @Parameter(required = true, description = "student id" )
            @RequestBody Student student
    ){
        return ResponseEntity.ok(new Student());
    }
}
