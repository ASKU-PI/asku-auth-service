package pl.asku.authservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.asku.authservice.dto.UserDto;
import pl.asku.authservice.dto.facebook.FacebookLoginRequest;
import pl.asku.authservice.model.User;
import pl.asku.authservice.security.policy.UserPolicy;
import pl.asku.authservice.service.FacebookService;
import pl.asku.authservice.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final FacebookService facebookService;
    private final UserPolicy userPolicy;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
        User user = null;
        try {
            user = userService.register(userDto);
        } catch(Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/facebook/register")
    public ResponseEntity<User> register(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        User user = null;
        try {
            user = facebookService.register(facebookLoginRequest.getAccessToken());
        } catch(Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getMyUserInfo(Authentication authentication) {
        if(!userPolicy.myUserInfo(authentication)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<User> currentUser = userService.getCurrentUser();

        return currentUser.map(user -> ResponseEntity.status(HttpStatus.OK).body(user)).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username, Authentication authentication) {
        if(!userPolicy.userInfo(authentication, username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<User> requestedUser = userService.getUser(username);

        return requestedUser.map(user -> ResponseEntity.status(HttpStatus.OK).body(user)).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }
}
