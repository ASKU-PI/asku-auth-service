package pl.asku.authservice.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import pl.asku.authservice.util.JwtTokenProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Map<HttpMethod, String[]> ANONYMOUS_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "/swagger-ui/**",
                    "/v2/api-docs"
            }
    );

    private final Map<HttpMethod, String[]> OPEN_ENDPOINTS = Map.of(
            HttpMethod.POST, new String[]{
                    "/auth/login",
                    "/auth/register"
            }
    );

    private final Map<HttpMethod, String[]> USER_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "/auth/user"
            }
    );

    private final Map<HttpMethod, String[]> MODERATOR_ENDPOINTS = Map.of(
            HttpMethod.GET, new String[]{
                    "/auth/user/**"
            }
    );

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsFilter corsFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((req, resp, e) -> resp.sendError(HttpServletResponse.SC_FORBIDDEN))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, OPEN_ENDPOINTS.get(HttpMethod.POST)).permitAll()
                .antMatchers(HttpMethod.GET, ANONYMOUS_ENDPOINTS.get(HttpMethod.GET)).anonymous()
                .antMatchers(HttpMethod.GET, USER_ENDPOINTS.get(HttpMethod.GET)).hasRole("USER")
                .antMatchers(HttpMethod.GET, MODERATOR_ENDPOINTS.get(HttpMethod.GET)).hasRole("MODERATOR")
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}