package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.repository.UsuarioRepository;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioServiceTests
{
    @MockBean
    UsuarioRepository repository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ObjectConverter converter;
    @Autowired
    PasswordEncoder passwordEncoder;
    Validator validator;

    List<Usuario> usuarioList;
    Idioma idiomaPt;

    Usuario usuarioAdmin;
    Usuario usuarioDef;

    @BeforeEach
    void init()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        idiomaPt = new Idioma(1, "Português", "PT", false);
        usuarioAdmin = new Usuario(1, idiomaPt, "admin", "admin", "000.000.000-00", "000000000",
                "admin@admin", "$2a$10$egMRCmpFvt.BaGWux2od8utalXtO0XmjuCQsWOxOlAgjJNr5yxGJG",
                "", false);
        usuarioDef = converter.convert(usuarioAdmin, Usuario.class);
        usuarioDef.setUsername("davi");

        usuarioList = new ArrayList<Usuario>(2);
        Collections.addAll(usuarioList, usuarioAdmin, usuarioDef);

        // Save
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenAnswer(invocation ->
        {
            Usuario adicionado = invocation.getArgument(0);
            int pos = adicionado.getId()-1;
            if (pos > 0)
            {
                usuarioList.set(pos, adicionado);
            }
            else
            {
                usuarioList.add(adicionado);
            }

            return adicionado;
        });
        // Find by id
        Mockito.when(repository.findById(Mockito.anyInt())).thenAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            try
            {
                Usuario buscado = usuarioList.get(id-1);
                return Optional.ofNullable(buscado);
            }
            catch (Exception e)
            {
                return Optional.empty();
            }
        });
        // Find all
        Mockito.when(repository.findAll()).thenReturn(usuarioList);
    }

    //Create
    @Test
    void testCreateUsuario()
    {
        int tamanhoEsperado = usuarioList.size()+1;

        Usuario usuario = new Usuario(0, idiomaPt, "naoeodavi", "não-davi", "000.000.000-00", "000000000",
                "nd@admin", "ndavi",
                "", false);
        usuarioService.createUsuario(usuario);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Usuario.class));
        assertEquals(tamanhoEsperado, usuarioList.size());
        assertEquals(usuarioList.get(usuarioList.size()-1), usuario);
    }

    @Test
    void testCreateUsuarioSemIdioma()
    {
        Usuario usuario = new Usuario(0, null, "naoeodavi", "não-davi", "000.000.000-00", "000000000",
                "nd@admin", "ndavi",
                "", false);

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateUsuarioCamposNulos()
    {
        Usuario usuario = new Usuario(0, idiomaPt, null, "não-davi",
                null, "000000000",
                null, null,
                "", false);

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertThrows(Exception.class, () -> usuarioService.createUsuario(usuario));
        assertFalse(violations.isEmpty());
    }

    // Update
    @Test
    void testUpdateUsuario()
    {
        Usuario usuario = new Usuario(1, idiomaPt, "davi", "não-davi", "000.000.000-00", "000000000",
                "nd@admin", "admin",
                "", true);

        String nomeEsperado = usuario.getNome();
        usuarioService.updateUsuario(2, usuario);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertEquals(nomeEsperado, usuarioList.get(1).getNome());
    }

    @Test
    void testUpdateUsuarioIdInvalido()
    {
        Usuario usuario = new Usuario(1, idiomaPt, "naoeodavi", "não-davi", "000.000.000-00", "000000000",
                "nd@admin", "admin",
                "", true);

        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
        assertThrows(NoSuchElementException.class, () -> usuarioService.updateUsuario(667, usuario));
    }

    @Test
    void testUpdateUsuarioMudarSenha()
    {
        Usuario usuario = new Usuario(1, idiomaPt, "naoeodavi", "não-davi", "000.000.000-00", "000000000",
                "nd@admin", "senhamudada",
                "", true);

        usuarioService.updateUsuario(2, usuario);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertTrue(passwordEncoder.matches("senhamudada", usuario.getSenha()));
    }

    // Delete
    @Test
    void testDeleteUsuario()
    {
        usuarioService.deleteUsuario(2);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertTrue(usuarioDef.isDesativado());
    }

    @Test
    void testDeleteUsuarioIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> usuarioService.deleteUsuario(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // Get
    @Test
    void testGetUsuario()
    {
        int id = 2;
        assertEquals(usuarioDef, usuarioService.retrieveUsuario(id));
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(id));
    }

    @Test
    void testGetUsuarioIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> usuarioService.retrieveUsuario(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // GetAll
    @Test
    void testGetAllUsuario()
    {
        List<Usuario> retrieved = usuarioService.retrieveAllUsuario();

        assertEquals(usuarioList.size(), retrieved.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
