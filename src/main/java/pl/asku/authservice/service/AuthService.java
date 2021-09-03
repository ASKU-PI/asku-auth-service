package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.asku.authservice.dto.LoginDto;
import pl.asku.authservice.dto.TokenDto;
import pl.asku.authservice.util.JwtTokenProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final String facebookIdentifierPrefix;
    private final String facebookDefaultPassword;

    public TokenDto login(LoginDto loginDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

        if (violations.size() > 0) {
            throw new RuntimeException(violations.toString());
        }

        return login(loginDto.getEmail(), loginDto.getPassword());
    }

    public TokenDto facebookLogin(String facebookUserId) {
        String identifier = facebookIdentifierPrefix + facebookUserId;
        return login(identifier, facebookDefaultPassword);
    }

    private TokenDto login(String identifier, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(identifier, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        return new TokenDto(token);
    }
}
