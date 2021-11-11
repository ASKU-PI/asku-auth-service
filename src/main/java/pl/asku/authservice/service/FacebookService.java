package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.asku.authservice.dto.TokenDto;
import pl.asku.authservice.dto.facebook.FacebookUserDto;
import pl.asku.authservice.model.User;
import pl.asku.authservice.util.FacebookClient;

@Service
@AllArgsConstructor
public class FacebookService {

    private final String facebookIdentifierPrefix;
    private FacebookClient facebookClient;
    private AuthService authService;
    private UserService userService;

    public TokenDto login(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);

        if (userService.getUser(facebookIdentifierPrefix + facebookUser.getId()).isEmpty()) {
            userService.facebookRegister(facebookUser);
        }

        return authService.facebookLogin(facebookUser.getId());
    }

    public FacebookUserDto register(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        return userService.facebookRegister(facebookUser);
    }
}
