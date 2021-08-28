package pl.asku.authservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import pl.asku.authservice.model.Authority;
import pl.asku.authservice.model.User;
import pl.asku.authservice.repository.AuthorityRepository;
import pl.asku.authservice.repository.UserRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@RefreshScope
@EnableEurekaClient
@EnableSwagger2
public class AuthServiceApplication {

    @Value("${test-data.user-username}")
    private String testUserUsername;
    @Value("${test-data.user-password}")
    private String testUserPassword;

    @Value("${test-data.moderator-username}")
    private String testModeratorUsername;
    @Value("${test-data.moderator-password}")
    private String testModeratorPassword;

    @Value("${test-data.admin-username}")
    private String testAdminUsername;
    @Value("${test-data.admin-password}")
    private String testAdminPassword;

    @Value("${test-data.fb-user-username}")
    private String testFacebookUserUsername;
    @Value("${test-data.fb-user-password}")
    private String testFacebookUserPassword;

    @Value("${facebook.username-prefix}")
    private String facebookUsernamePrefix;

    @Value("${facebook.default-password}")
    private String facebookDefaultPassword;

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String facebookUsernamePrefix(){
        return facebookUsernamePrefix;
    }

    @Bean
    public String facebookDefaultPassword(){
        return facebookDefaultPassword;
    }

    @Bean
    public CommandLineRunner loadAuthorities(AuthorityRepository authorityRepository){
        return args -> {
            List<String> authorityNames = List.of("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN");
            List<Authority> presentAuthorities = authorityRepository.findAll();
            for(String authorityName : authorityNames){
                Authority authority = new Authority(authorityName);
                if(!presentAuthorities.contains(authority)){
                    authorityRepository.save(authority);
                }
            }
        };
    }

    @Bean CommandLineRunner loadTestUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {


        return args -> {
            userRepository.save(
                    new User(
                            1L,
                            testUserUsername,
                            passwordEncoder.encode(testUserPassword),
                            true,
                            Set.of(new Authority("ROLE_USER"))
                    )
            );
            userRepository.save(
                    new User(
                            2L,
                            testModeratorUsername,
                            passwordEncoder.encode(testModeratorPassword),
                            true,
                            Set.of(
                                    new Authority("ROLE_USER"),
                                    new Authority("ROLE_MODERATOR")
                            )
                    )
            );
            userRepository.save(
                    new User(
                            3L,
                            testAdminUsername,
                            passwordEncoder.encode(testAdminPassword),
                            true,
                            Set.of(
                                    new Authority("ROLE_USER"),
                                    new Authority("ROLE_MODERATOR"),
                                    new Authority("ROLE_ADMIN")
                            )
                    )
            );
            userRepository.save(
                    new User(
                            4L,
                            testFacebookUserUsername,
                            passwordEncoder.encode(testFacebookUserPassword),
                            true,
                            Set.of(new Authority("ROLE_USER"))
                    )
            );
        };
    }

}
