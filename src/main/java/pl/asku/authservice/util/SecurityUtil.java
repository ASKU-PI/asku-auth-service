package pl.asku.authservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Optional<String> getCurrentUserIdentifier() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        String identifier = null;
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            identifier = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            identifier = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(identifier);
    }
}
