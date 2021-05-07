package pl.asku.authservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.asku.authservice.dto.UserDto;
import pl.asku.authservice.model.User;
import pl.asku.authservice.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getCurrentUser().get());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username).get());
    }
}
