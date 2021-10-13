package pl.asku.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.asku.authservice.dto.RegisterDto;
import pl.asku.authservice.model.User;
import pl.asku.authservice.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterTests {

    private final UserService userService;
    @Value("${test-data.user-email}")
    private String testUserEmail;

    @Autowired
    public RegisterTests(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void registersUserWithCorrectCredentials() {
        //given
        String firstName = "John";
        String lastName = "Doe";
        String email = "test@test.pl";
        String password = "test";
        RegisterDto registerDto = RegisterDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        //when
        User user = userService.register(registerDto);

        //then
        assertEquals(user.getIdentifier(), email);
        assertEquals(user.getAuthorities().size(), 1);
    }

    @Test
    public void failsForExistingUser() {
        //given
        RegisterDto registerDto = RegisterDto.builder().email(testUserEmail).password("test").build();

        //when then
        //TODO: make this throw custom exception
        assertThrows(RuntimeException.class, () -> userService.register(registerDto));
    }

    @Test
    public void failsForEmptyCredentials() {
        //given
        RegisterDto registerDto = RegisterDto.builder().email("").password("").build();

        //when then
        //TODO: make this throw custom exception
        assertThrows(RuntimeException.class, () -> userService.register(registerDto));
    }

    @Test
    public void failsWithNotValidCredentials() {
        //given
        RegisterDto registerDto = RegisterDto.builder().email("abcde@@fghijgmail.com").password("secretPassword").build();

        //when then
        //TODO: make this throw custom exception
        assertThrows(RuntimeException.class, () -> userService.register(registerDto));
    }
}
