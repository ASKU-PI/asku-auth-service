package pl.asku.authservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import pl.asku.authservice.dto.LoginDto;
import pl.asku.authservice.dto.TokenDto;
import pl.asku.authservice.service.AuthService;
import pl.asku.authservice.util.JwtTokenProvider;
import pl.asku.authservice.util.SecurityUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LoginTests {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${test-data.user-email}")
    private String testUserEmail;
    @Value("${test-data.user-password}")
    private String testUserPassword;

    @Autowired
    LoginTests(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Test
    public void loginShouldReturnBearerToken() {
        //given
        LoginDto loginDto = LoginDto.builder().email(testUserEmail).password(testUserPassword).build();

        //when
        TokenDto tokenDto = authService.login(loginDto);

        //then
        Assertions.assertAll(
                () -> assertNotNull(tokenDto.getToken()),
                () -> assertTrue(SecurityUtil.getCurrentUserIdentifier().isPresent()),
                () -> assertEquals(SecurityUtil.getCurrentUserIdentifier().get(), testUserEmail),
                () -> assertTrue(jwtTokenProvider.checkToken(tokenDto.getToken()))
        );
    }

    @Test
    public void failsForWrongCredentials() {
        //given
        LoginDto loginDto = LoginDto.builder().email("wrong@email.com").password("wrongPassword").build();

        //when then
        assertThrows(BadCredentialsException.class, () -> authService.login(loginDto));
    }
}
