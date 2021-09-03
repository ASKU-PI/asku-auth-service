package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.asku.authservice.dto.RegisterDto;
import pl.asku.authservice.dto.facebook.FacebookUserDto;
import pl.asku.authservice.model.Authority;
import pl.asku.authservice.model.User;
import pl.asku.authservice.repository.UserRepository;
import pl.asku.authservice.util.SecurityUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String facebookIdentifierPrefix;
    private final String facebookDefaultPassword;

    @Transactional
    public User register(RegisterDto registerDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);

        if (violations.size() > 0) {
            throw new RuntimeException(violations.toString());
        }

        return register(registerDto.getEmail(), registerDto.getPassword());
    }

    @Transactional
    public User facebookRegister(FacebookUserDto facebookUserDto) {
        String identifier = facebookIdentifierPrefix + facebookUserDto.getId();

        return register(identifier, facebookDefaultPassword);
    }

    private User register(String identifier, String password) {
        if (userRepository.findOneWithAuthoritiesByIdentifier(identifier).orElse(null) != null) {
            throw new RuntimeException("User with this identifier exists");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .identifier(identifier)
                .password(passwordEncoder.encode(password))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(String identifier) {
        return userRepository.findOneWithAuthoritiesByIdentifier(identifier);
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentUser() {
        return SecurityUtil.getCurrentUserIdentifier().flatMap(userRepository::findOneWithAuthoritiesByIdentifier);
    }
}
