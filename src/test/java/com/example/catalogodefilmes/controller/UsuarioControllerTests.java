package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPostDTO;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPutDTO;
import com.example.catalogodefilmes.service.UsuarioService;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc()
public class UsuarioControllerTests
{
    @Value("${jwt.header}")
    String authHeader;
    @Value("${jwt.prefix}")
    String authPrefix;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectConverter converter;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UsuarioService serviceMock;
    @Autowired
    UsuarioController usuarioController;

    @Value("${app.usuarioController.mapping}")
    String mapping;

    Idioma idiomaPt;

    List<Usuario> usuarios;
    Usuario usuarioAdmin;
    Usuario usuario;
    String cpfValido = "079.965.913-42";

    @BeforeEach
    void init()
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);

        usuarioAdmin = new Usuario(1, idiomaPt, "admin", "admin",
                "000.000.000-00", "000000000",
                "admin@admin", "$2a$10$egMRCmpFvt.BaGWux2od8utalXtO0XmjuCQsWOxOlAgjJNr5yxGJG",
                "", false);
        usuario = converter.convert(usuarioAdmin, Usuario.class);
        usuario.setId(2);
        usuario.setUsername("davi");
        usuario.setCpf("111.111.111-11");

        usuarios = new ArrayList<>();
        usuarios.addAll(Arrays.asList(usuarioAdmin, usuario));

        Mockito.doAnswer(invocation ->
        {
            Usuario add = invocation.getArgument(0);
            usuarios.add(add);

            return null;
        }).when(serviceMock).createUsuario(Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                Usuario usuario = invocation.getArgument(1);
                usuarios.set(id, usuario);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(serviceMock).updateUsuario(Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                usuarios.get(id).setDesativado(true);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(serviceMock).deleteUsuario(Mockito.anyInt());
    }

    // Post
    @Test
    void testPostNaoAutenticado() throws Exception
    {
        UsuarioPostDTO dto = converter.convert(usuario, UsuarioPostDTO.class);
        dto.setUsername("neodavi");
        dto.setCpf(cpfValido);
        dto.setSenha("admin");
        dto.setEmail("neodavi@admin");

        mockMvc.perform(post(mapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().is(400));
        Mockito.verify(serviceMock, Mockito.times(0)).createUsuario(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPost() throws Exception
    {
        int tamanhoEsperado = usuarios.size()+1;
        UsuarioPostDTO dto = converter.convert(usuario, UsuarioPostDTO.class);
        dto.setUsername("neodavi");
        dto.setCpf(cpfValido);
        dto.setSenha("admin");
        dto.setEmail("neodavi@admin");

        mockMvc.perform(post(mapping)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

        assertEquals(tamanhoEsperado, usuarios.size());
        assertEquals(dto.getUsername(), usuarios.get(usuarios.size()-1).getUsername());
        Mockito.verify(serviceMock, Mockito.times(1)).createUsuario(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPostEntradaInvalida() throws Exception
    {
        UsuarioPostDTO dto = new UsuarioPostDTO(idiomaPt, "naoeodavi", "não-davi",
                null, "000000000",
                "admin@admin", "ndavi","", false);

        mockMvc.perform(post(mapping)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
        Mockito.verify(serviceMock, Mockito.times(0)).createUsuario(Mockito.any());
    }

    // Put
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPut() throws Exception
    {
        UsuarioPutDTO dto = converter.convert(usuario, UsuarioPutDTO.class);
        dto.setCpf("000000000");
        dto.setSenha("admin2");
        dto.setEmail("neodavi@admin");
        int id = 1;
        dto.setId(id);

        mockMvc.perform(put(mapping + "/" + id)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        assertEquals(dto.getCpf(), usuarios.get(id-1).getCpf());
        Mockito.verify(serviceMock, Mockito.times(1))
                .updateUsuario(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutEntradaInvalida() throws Exception
    {
        UsuarioPostDTO dto = converter.convert(usuario, UsuarioPostDTO.class);
        dto.setUsername("neodavi");
        dto.setCpf("cpfinvalido");
        dto.setSenha("admin");
        dto.setEmail("neodavi@admin");
        int id = 2;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

        Mockito.verify(serviceMock, Mockito.times(0))
                .updateUsuario(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutIdInvalido() throws Exception
    {
        UsuarioPostDTO dto = converter.convert(usuario, UsuarioPostDTO.class);
        dto.setUsername("neodavi");
        dto.setCpf("cpfinvalido");
        dto.setSenha("admin");
        dto.setEmail("neodavi@admin");
        int id = 667;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

        Mockito.verify(serviceMock, Mockito.times(0))
                .updateUsuario(Mockito.anyInt(), Mockito.any());
    }

    // Delete
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testDelete() throws Exception
    {
        int id = 2;

        mockMvc.perform(delete(mapping + "/" + id)
            .header(authHeader, authPrefix + " valid")).andExpect(status().isOk());
        assertTrue(usuarios.get(id-1).isDesativado());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testDeleteIdInvalido() throws Exception
    {
        int id = 667;

        mockMvc.perform(delete(mapping + "/" + id)
                        .header(authHeader, authPrefix + " valid")).andExpect(status().isNotFound());
    }
}
