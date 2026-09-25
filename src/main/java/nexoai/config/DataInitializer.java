package nexoai.config;

import nexoai.model.PerfilUsuario;
import nexoai.repository.PerfilUsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarDatos(
            PerfilUsuarioRepository perfilUsuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String emailDemo = "demo@nexoai.com";

            if (perfilUsuarioRepository.findByEmail(emailDemo).isEmpty()) {

                PerfilUsuario usuarioDemo = new PerfilUsuario(
                        "Usuario Demo",
                        emailDemo,
                        passwordEncoder.encode("Nexo1234"),
                        "America/Lima"
                );

                perfilUsuarioRepository.save(usuarioDemo);

                System.out.println(
                        "Usuario demo creado: " + emailDemo
                );
            }
        };
    }
}