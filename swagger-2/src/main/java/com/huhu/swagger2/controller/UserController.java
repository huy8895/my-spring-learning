package com.huhu.swagger2.controller;

import com.huhu.swagger2.model.User;
import com.huhu.swagger2.repository.UserRepository;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value = "User APIs")
public class UserController {
    private final UserRepository userRepository;
    private Random ran = new Random();

    @ApiOperation(value = "Xem danh sách User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thành công"),
            @ApiResponse(code = 401, message = "Chưa xác thực"),
            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
            @ApiResponse(code = 404, message = "Không tìm thấy")
    })
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        int nxt = ran.nextInt(10);
        if (nxt == 5) {
            throw new RuntimeException("Breaking logic");
        }

        return userRepository.findById(id).orElse(new User());
    }

    @PostMapping("/users")
    public User createUser(
            @ApiParam(value = "Đối tượng User cần tạo mới", required = true) @Valid @RequestBody User user
    ) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }
}
