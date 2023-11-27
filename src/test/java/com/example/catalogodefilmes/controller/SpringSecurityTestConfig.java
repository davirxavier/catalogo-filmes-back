package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioDetailsDTO;
import com.example.catalogodefilmes.security.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig
{
    @Bean
    @Primary
    public UserDetailsService userDetailsService()
    {
        Idioma idiomaPt = new Idioma(1, "Português", "PT", false);

        UsuarioDetailsDTO admin = new UsuarioDetailsDTO(1, idiomaPt, "admin", "admin",
                "000.000.000-00", "000000000",
                "admin@admin", "admin","", false);
        UsuarioDetailsDTO usuario = new UsuarioDetailsDTO(1, idiomaPt, "naoeodavi", "não-davi",
                "000.000.000-00", "000000000",
                "nd@admin", "ndavi","", false);

        return new InMemoryUserDetailsManager(Arrays.asList(admin, usuario));
    }

    @Bean
    @Primary
    public JwtTokenProvider tokenProvider()
    {
        return new JwtTokenProvider()
        {
            @Override
            public String getUsernameFromJWT(String jwt)
            {
                return "admin";
            }

            @Override
            public String genToken(Authentication authentication)
            {
                return "valid";
            }

            @Override
            public boolean validateToken(String authToken)
            {
                return true;
            }
        };
    }
}
