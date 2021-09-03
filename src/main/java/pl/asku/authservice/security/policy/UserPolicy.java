package pl.asku.authservice.security.policy;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class UserPolicy {

    public boolean myUserInfo(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public boolean userInfo(Authentication authentication, String identifier) {
        return authentication != null &&
                (authentication.getName().equals(identifier)
                        || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR")));
    }
}
