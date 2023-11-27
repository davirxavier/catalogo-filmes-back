package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.dto.categoria.CategoriaPostDTO;
import com.example.catalogodefilmes.service.CategoriaService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
public class CategoriaControllerTests
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
    CategoriaService categoriaService;
    @Autowired
    CategoriaController categoriaController;

    @Value("${app.categoriaController.mapping}")
    String mapping;

    Idioma idiomaPt;

    List<Categoria> categorias;
    Categoria categoriaAc;
    Categoria categoriaAv;

    @BeforeEach
    void init()
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);

        categoriaAc = new Categoria(1, "ação", "ac", idiomaPt, false);
        categoriaAv = new Categoria(2, "aventura", "av", idiomaPt, false);

        categorias = new ArrayList<>();
        categorias.addAll(Arrays.asList(categoriaAc, categoriaAv));

        Mockito.doAnswer(invocation ->
        {
            Categoria add = invocation.getArgument(0);
            categorias.add(add);

            return null;
        }).when(categoriaService).createCategoria(Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                Categoria categoria = invocation.getArgument(1);
                categorias.set(id, categoria);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(categoriaService).updateCategoria(Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                categorias.get(id).setDesativado(true);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(categoriaService).deleteCategoria(Mockito.anyInt());
    }

    // Post
    @Test
    void testPostNaoAutenticado() throws Exception
    {
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome("ficção");
        dto.setTag("fc");

        mockMvc.perform(post(mapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().is(400));
        Mockito.verify(categoriaService, Mockito.times(0)).createCategoria(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPost() throws Exception
    {
        int tamanhoEsperado = categorias.size()+1;
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome("ficção");
        dto.setTag("fc");

        mockMvc.perform(post(mapping)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

        assertEquals(tamanhoEsperado, categorias.size());
        assertEquals(dto.getNome(), categorias.get(categorias.size()-1).getNome());
        Mockito.verify(categoriaService, Mockito.times(1)).createCategoria(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPostEntradaInvalida() throws Exception
    {
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome(null);
        dto.setTag(null);

        mockMvc.perform(post(mapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
        Mockito.verify(categoriaService, Mockito.times(0)).createCategoria(Mockito.any());
    }

    // Put
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPut() throws Exception
    {
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome("ficção");
        dto.setTag("fc");
        int id = 2;

        mockMvc.perform(put(mapping + "/" + id)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

        assertEquals(dto.getNome(), categorias.get(id-1).getNome());
        Mockito.verify(categoriaService, Mockito.times(1))
                .updateCategoria(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutEntradaInvalida() throws Exception
    {
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome(null);
        dto.setTag(null);
        int id = 2;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

        Mockito.verify(categoriaService, Mockito.times(0))
                .updateCategoria(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutIdInvalido() throws Exception
    {
        CategoriaPostDTO dto = converter.convert(categoriaAc, CategoriaPostDTO.class);
        dto.setNome("ficção");
        dto.setTag("fc");
        int id = 667;

        mockMvc.perform(put(mapping + "/" + id)
                .header(authHeader, authPrefix + " valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isNotFound());
    }

    // Delete
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testDelete() throws Exception
    {
        int id = 2;

        mockMvc.perform(delete(mapping + "/" + id)
                .header(authHeader, authPrefix + " valid")).andExpect(status().isOk());
        assertTrue(categorias.get(id-1).isDesativado());
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
