package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.asku.authservice.dto.UserDto;
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
    private final String facebookUsernamePrefix;
    private final String facebookDefaultPassword;

    @Transactional
    public User register(UserDto userDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        if (violations.size() > 0) {
            throw new RuntimeException(violations.toString());
        }

        return register(userDto.getUsername(), userDto.getPassword());
    }

    @Transactional
    public User facebookRegister(FacebookUserDto facebookUserDto) {
        String username = facebookUsernamePrefix + facebookUserDto.getId();
        String password = facebookDefaultPassword;

        return register(username, password);
    }

    private User register(String username, String password) {
        if (userRepository.findOneWithAuthoritiesByUsername(username).orElse(null) != null) {
            throw new RuntimeException("User with this username exists");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentUser() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
