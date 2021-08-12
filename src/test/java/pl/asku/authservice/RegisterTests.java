package pl.asku.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pl.asku.authservice.dto.UserDto;
import pl.asku.authservice.model.User;
import pl.asku.authservice.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterTests {

    @Value("${test-data.user-username}")
    private String testUserUsername;

    private final UserService userService;

    @Autowired
    public RegisterTests(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void registersUserWithCorrectCredentials() {
        //given
        String username = "test";
        String password = "test";
        UserDto userDto = UserDto.builder().username(username).password(password).build();

        //when
        User user = userService.register(userDto);

        //then
        assertEquals(user.getUsername(), username);
    }

    @Test
    public void failsForExistingUser() {
        //given
        UserDto userDto = UserDto.builder().username(testUserUsername).password("test").build();

        //when then
        //TODO: make this throw custom exception
        assertThrows(RuntimeException.class, () -> userService.register(userDto));
    }

    @Test
    public void failsForEmptyCredentials() {
        //given
        UserDto userDto = UserDto.builder().username("").password("").build();

        //when then
        //TODO: make this throw custom exception
        assertThrows(RuntimeException.class, () -> userService.register(userDto));
    }
}
