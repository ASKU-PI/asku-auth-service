package pl.asku.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.asku.authservice.model.User;
import pl.asku.authservice.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserTests {

    private final UserService userService;
    @Value("${test-data.user-email}")
    private String testUserEmail;
    @Value("${test-data.moderator-email}")
    private String testModeratorEmail;
    @Value("${test-data.admin-email}")
    private String testAdminEmail;

    @Autowired
    public UserTests(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void returnsDataOfUser() {
        //given
        String email = testUserEmail;

        //when
        Optional<User> user = userService.getUser(email);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getIdentifier(), email);
        assertEquals(user.get().getAuthorities().size(), 1);
    }

    @Test
    public void returnsDataOfModerator() {
        //given
        String email = testModeratorEmail;

        //when
        Optional<User> user = userService.getUser(email);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getIdentifier(), email);
        assertEquals(user.get().getAuthorities().size(), 2);
    }

    @Test
    public void returnsDataOfAdmin() {
        //given
        String email = testAdminEmail;

        //when
        Optional<User> user = userService.getUser(email);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getIdentifier(), email);
        assertEquals(user.get().getAuthorities().size(), 3);
    }

    @Test
    public void emptyForWrongUser() {
        //given
        String email = "wrong@email.com";

        //when
        Optional<User> user = userService.getUser(email);

        //then
        assertTrue(user.isEmpty());
    }
}
