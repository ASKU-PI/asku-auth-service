package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.asku.authservice.dto.TokenDto;
import pl.asku.authservice.model.User;
import pl.asku.authservice.util.FacebookClient;

@Service
@AllArgsConstructor
public class FacebookService {

    private FacebookClient facebookClient;
    private AuthService authService;
    private UserService userService;
    private final String facebookUsernamePrefix;

    public TokenDto login(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);

        if(userService.getUser(facebookUsernamePrefix + facebookUser.getId()).isEmpty()) {
            userService.facebookRegister(facebookUser);
        }

        return authService.facebookLogin(facebookUser.getId());
    }

    public User register(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        return userService.facebookRegister(facebookUser);
    }
}
