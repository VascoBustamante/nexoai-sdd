package nexoai.security;

import nexoai.model.PerfilUsuario;
import nexoai.repository.PerfilUsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PerfilUsuarioRepository perfilUsuarioRepository) {

        return username -> {

            PerfilUsuario perfil = perfilUsuarioRepository
                    .findByEmail(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Usuario no encontrado"
                            )
                    );

            return org.springframework.security.core.userdetails.User
                    .withUsername(perfil.getEmail())
                    .password(perfil.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .defaultSuccessUrl("/agenda", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            );

        return http.build();
    }
}