// src/main/java/com/clubdelcan/crud/config/SecurityConfig.java
package com.clubdelcan.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder enc) {
        var admin   = User.withUsername("admin@clubdelcan.cl").password(enc.encode("admin123")).roles("ADMIN").build();
        var cliente = User.withUsername("cliente@clubdelcan.cl").password(enc.encode("cliente123")).roles("CLIENTE").build();
        return new InMemoryUserDetailsManager(admin, cliente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // públicas
                        .requestMatchers("/", "/login", "/recuperar", "/recuperar/enviado",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.ico", "/error", "/registro-falso").permitAll()
                        // áreas por rol
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/usuario/**").hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers("/mascotas/**").hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers("/servicios/**", "/reservas/**").hasRole("ADMIN")
                        // cualquier otra requiere auth
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")              // página de login (GET)
                        .loginProcessingUrl("/login")     // endpoint que procesa (POST)
                        .usernameParameter("email")       // el input name="email"
                        .passwordParameter("password")    // el input name="password"
                        .failureUrl("/login?error")
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String redirect = "/"; // fallback
                            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                redirect = "/admin";
                            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                                redirect = "/usuario";
                            }
                            response.sendRedirect(redirect);
                        })
                        .permitAll()
                )
                .logout(lo -> lo
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // déjalo como estaba si aún no estás agregando el token CSRF a los forms
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
