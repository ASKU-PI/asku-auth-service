package pl.asku.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.asku.authservice.model.User;
import pl.asku.authservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String identifier) {
      return userRepository.findOneWithAuthoritiesByIdentifier(identifier)
         .map(user -> createUserObject(identifier, user))
         .orElseThrow(() -> new UsernameNotFoundException(identifier + " not found"));
   }

   private org.springframework.security.core.userdetails.User createUserObject(String identifier, User user) {
      if (!user.isActivated()) {
         throw new RuntimeException(identifier + " not active");
      }
      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(user.getIdentifier(),
              user.getPassword(),
              grantedAuthorities);
   }
}
