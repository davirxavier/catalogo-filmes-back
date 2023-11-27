package com.example.catalogodefilmes.validation.usuario;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPutDTO;
import com.example.catalogodefilmes.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UsuarioPutDTOValidationTests
{
    @MockBean
    UsuarioService serviceMock;

    @Autowired
    Validator validator;

    Idioma idiomaPt;

    Usuario usuarioAdmin;
    Usuario usuario;
    List<Usuario> usuarios;

    String cpfValido = "079.965.913-42";

    @BeforeEach
    void init()
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);
        usuarioAdmin = new Usuario(1, idiomaPt, "admin", "admin",
                "000.000.000-00", "000000000",
                "admin@admin", "$2a$10$egMRCmpFvt.BaGWux2od8utalXtO0XmjuCQsWOxOlAgjJNr5yxGJG",
                "", false);
        usuario = new Usuario(2, idiomaPt, "davi", "admin",
                "111.111.111-11", "000000000",
                "davi@admin", "$2a$10$egMRCmpFvt.BaGWux2od8utalXtO0XmjuCQsWOxOlAgjJNr5yxGJG",
                "", false);

        usuarios = new ArrayList<>();
        usuarios.addAll(Arrays.asList(usuarioAdmin, usuario));

        Mockito.when(serviceMock.retrieveByEmail(Mockito.anyString())).thenAnswer(invocation ->
        {
           String email = invocation.getArgument(0);

           for (Usuario usuario : usuarios)
           {
               if (usuario.getEmail().equals(email))
               {
                   System.out.println(usuario.getEmail());
                    return usuario;
               }
           }
           return null;
        });
        Mockito.when(serviceMock.retrieveByCpf(Mockito.anyString())).thenAnswer(invocation ->
        {
            String cpf = invocation.getArgument(0);

            for (Usuario usuario : usuarios)
            {
                if (usuario.getCpf().equals(cpf))
                {
                    return usuario;
                }
            }
            return null;
        });
        Mockito.when(serviceMock.retrieveByUsername(Mockito.anyString())).thenAnswer(invocation ->
        {
            String username = invocation.getArgument(0);

            for (Usuario usuario : usuarios)
            {
                if (usuario.getUsername().equals(username))
                {
                    return usuario;
                }
            }
            return null;
        });
    }

    @Test
    void testValido()
    {
        UsuarioPutDTO dto = new UsuarioPutDTO(2, idiomaPt, "não-davi",
                cpfValido, "000000000",
                "nd@admin", "ndavi","", false);

        Set<ConstraintViolation<UsuarioPutDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEmailExistente()
    {
        UsuarioPutDTO dto = new UsuarioPutDTO(2, idiomaPt, "não-davi",
                cpfValido, "000000000",
                "admin@admin", "ndavi","", false);

        Set<ConstraintViolation<UsuarioPutDTO>> violations = validator.validate(dto);

        ConstraintViolation<UsuarioPutDTO> first = violations.stream().findFirst().orElseThrow();
        assertEquals("email", first.getPropertyPath().toString());
    }

    @Test
    void testCpfInvalido()
    {
        UsuarioPutDTO dto = new UsuarioPutDTO(2, idiomaPt, "não-davi",
                usuarioAdmin.getCpf(), "000000000",
                "nd@admin", "ndavi","", false);

        Set<ConstraintViolation<UsuarioPutDTO>> violations = validator.validate(dto);

        ConstraintViolation<UsuarioPutDTO> first = violations.stream().findFirst().orElseThrow();
        assertEquals("cpf", first.getPropertyPath().toString());
    }
}
