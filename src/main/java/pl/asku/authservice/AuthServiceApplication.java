package pl.asku.authservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import pl.asku.authservice.model.Authority;
import pl.asku.authservice.repository.AuthorityRepository;

import java.util.List;

@SpringBootApplication
@RefreshScope
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
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

}
