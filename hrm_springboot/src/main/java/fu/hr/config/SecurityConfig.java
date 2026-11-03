package fu.hr.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/public/**", "/css/**", "/auths/**", "/").permitAll()
                            .requestMatchers("/departments/**").hasRole("HRM")
                            .anyRequest().authenticated();
                })
                .formLogin((form) -> {
                    form.loginPage("/auths/login")
                            .loginProcessingUrl("/auths/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .successHandler((req, res, auth) -> {
                                UserDetails userDetails = (UserDetails) auth.getPrincipal();

                                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // ADMIN, HRM

                                List<String> roles = authorities.stream().map((GrantedAuthority::getAuthority)).toList();

                                String url = roles.contains("ROLE_HRM") ? "/departments" : "/employees";

                                res.sendRedirect(req.getContextPath() + url);

                            })
                            .failureForwardUrl("/auths/login?error=true")
                    ;
                })
                .logout((logout) -> {
                    logout.logoutUrl("/auths/logout")
                            .logoutSuccessUrl("/auths/login?logout=true")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID")
                    ;
                })
                .exceptionHandling((exception)->{
                    exception.accessDeniedHandler(((request, response, accessDeniedException) -> {
                        log.error("access denied for {}", request.getRequestURI());
                        response.sendRedirect(request.getContextPath() + "/auths/login?accessDenied=true");
                    }))
                            .authenticationEntryPoint((((request, response, authException) -> {
                                log.error("authentication required for {}", request.getRequestURI());
                                response.sendRedirect(request.getContextPath() + "/auths/login?error=true");
                            })));
                });
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);

        return providerManager;
    }
}
