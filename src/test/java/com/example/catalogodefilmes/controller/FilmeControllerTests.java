package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Filme;
import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.entity.dto.filme.FilmePostDTO;
import com.example.catalogodefilmes.service.FilmeService;
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

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class FilmeControllerTests
{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectConverter converter;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    FilmeService filmeService;
    @Autowired
    FilmeController filmeController;

    @Value("${app.filmeController.mapping}")
    String mapping;

    Idioma idiomaPt;

    List<Filme> filmeList;
    Filme filme1;
    Filme filme2;
    Categoria categoriaAc;

    private static final long DATE = 365;

    @BeforeEach
    void init() throws ServletException, IOException
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);
        categoriaAc = new Categoria(1, "Ação", "ac", idiomaPt, false);

        filme1 = new Filme(1, "A viagem submarina de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                Duration.ofHours(10), false);
        filme2 = new Filme(2, "Alienigenas Terrestres 3: A Volta",
                "Alienígenas voltam a aterrorizar a terra.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                Duration.ofHours(10), false);

        filmeList = new ArrayList<>();
        filmeList.addAll(Arrays.asList(filme1, filme2));

        Mockito.doAnswer(invocation ->
        {
            Filme add = invocation.getArgument(0);
            filmeList.add(add);

            return null;
        }).when(filmeService).createFilme(Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                Filme filme = invocation.getArgument(1);
                filmeList.set(id, filme);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(filmeService).updateFilme(Mockito.anyInt(), Mockito.any());
        Mockito.doAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            id -= 1;
            try
            {
                filmeList.get(id).setDesativado(true);
            }
            catch (Exception e)
            {
                throw new NoSuchElementException();
            }

            return null;
        }).when(filmeService).deleteFilme(Mockito.anyInt());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPost() throws Exception
    {
        int tamanhoEsperado = filmeList.size()+1;
        FilmePostDTO dto = new FilmePostDTO("A viagem diferente de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                Duration.ofHours(10), false);

        mockMvc.perform(post(mapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

        assertEquals(tamanhoEsperado, filmeList.size());
        assertEquals(dto.getTitulo(), filmeList.get(filmeList.size()-1).getTitulo());
        Mockito.verify(filmeService, Mockito.times(1)).createFilme(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPostEntradaInvalida() throws Exception
    {
        FilmePostDTO dto = new FilmePostDTO(null,
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                null, false);

        mockMvc.perform(post(mapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
        Mockito.verify(filmeService, Mockito.times(0)).createFilme(Mockito.any());
    }

    // Put
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPut() throws Exception
    {
        FilmePostDTO dto = new FilmePostDTO("A viagem diferente de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                Duration.ofHours(10), false);
        int id = 2;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

        assertEquals(dto.getTitulo(), filmeList.get(id-1).getTitulo());
        Mockito.verify(filmeService, Mockito.times(1))
                .updateFilme(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutEntradaInvalida() throws Exception
    {
        FilmePostDTO dto = new FilmePostDTO("A viagem diferente de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                null, false);
        int id = 2;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

        Mockito.verify(filmeService, Mockito.times(0))
                .updateFilme(Mockito.anyInt(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testPutIdInvalido() throws Exception
    {
        FilmePostDTO dto = new FilmePostDTO("A viagem diferente de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE),
                Duration.ofHours(10), false);
        int id = 667;

        mockMvc.perform(put(mapping + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // Delete
    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testDelete() throws Exception
    {
        int id = 2;

        mockMvc.perform(delete(mapping + "/" + id)).andExpect(status().isOk());
        assertTrue(filmeList.get(id-1).isDesativado());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    void testDeleteIdInvalido() throws Exception
    {
        int id = 667;

        mockMvc.perform(delete(mapping + "/" + id)).andExpect(status().isNotFound());
    }
}
