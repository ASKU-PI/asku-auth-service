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

    @Value("${test-data.user-username}")
    private String testUserUsername;

    @Value("${test-data.moderator-username}")
    private String testModeratorUsername;

    @Value("${test-data.admin-username}")
    private String testAdminUsername;

    private final UserService userService;

    @Autowired
    public UserTests(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void returnsDataOfUser() {
        //given
        String username = testUserUsername;

        //when
        Optional<User> user = userService.getUser(username);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), username);
        assertEquals(user.get().getAuthorities().size(), 1);
    }

    @Test
    public void returnsDataOfModerator() {
        //given
        String username = testModeratorUsername;

        //when
        Optional<User> user = userService.getUser(username);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), username);
        assertEquals(user.get().getAuthorities().size(), 2);
    }

    @Test
    public void returnsDataOfAdmin() {
        //given
        String username = testAdminUsername;

        //when
        Optional<User> user = userService.getUser(username);

        //then
        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), username);
        assertEquals(user.get().getAuthorities().size(), 3);
    }

    @Test
    public void emptyForWrongUser() {
        //given
        String username = "wrongUsername";

        //when
        Optional<User> user = userService.getUser(username);

        //then
        assertTrue(user.isEmpty());
    }
}