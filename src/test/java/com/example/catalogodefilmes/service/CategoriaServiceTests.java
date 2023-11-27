package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.repository.CategoriaRepository;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoriaServiceTests
{
    @MockBean
    CategoriaRepository repository;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ObjectConverter converter;

    List<Categoria> categoriaList;
    Idioma idiomaPt;

    Categoria categoriaAc;
    Categoria categoriaAv;

    @BeforeEach
    void init()
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);

        categoriaAc = new Categoria(1, "ação", "ac", idiomaPt, false);
        categoriaAv = new Categoria(2, "aventura", "av", idiomaPt, false);

        categoriaList = new ArrayList<Categoria>(2);
        Collections.addAll(categoriaList, categoriaAc, categoriaAv);

        // Save
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation ->
        {
            Categoria adicionado = invocation.getArgument(0);

            if (adicionado.getIdioma() == null ||
                adicionado.getNome() == null ||
                adicionado.getTag() == null)
                throw new Exception("Constraint violations");

            int pos = adicionado.getId()-1;
            if (pos > 0)
            {
                categoriaList.set(pos, adicionado);
            }
            else
            {
                categoriaList.add(adicionado);
            }

            return adicionado;
        });
        // Find by id
        Mockito.when(repository.findById(Mockito.anyInt())).thenAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            try
            {
                Categoria buscado = categoriaList.get(id-1);
                return Optional.ofNullable(buscado);
            }
            catch (Exception e)
            {
                return Optional.empty();
            }
        });
        // Find all
        Mockito.when(repository.findAll()).thenReturn(categoriaList);
        Mockito.when(repository.existsById(Mockito.anyInt())).thenAnswer(invocation ->
        {
           int id = invocation.getArgument(0);
           return id > 0 && id <= categoriaList.size();
        });
    }

    //Create
    @Test
    void testCreateCategoria()
    {
        int tamanhoEsperado = categoriaList.size()+1;

        Categoria categoria = new Categoria(0, "ficção", "fc", idiomaPt, false);
        categoriaService.createCategoria(categoria);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Categoria.class));
        assertEquals(tamanhoEsperado, categoriaList.size());
        assertEquals(categoriaList.get(categoriaList.size()-1), categoria);
    }

    // TODO Apagar
    @Test
    void testCreateCategoriaSemIdioma()
    {
        Categoria categoria = new Categoria(0, "ficção", "fc", null, false);

        assertThrows(Exception.class, () -> categoriaService.createCategoria(categoria));
    }

    @Test
    void testCreateCategoriaCamposNulos()
    {
        Categoria categoria = new Categoria(0, null, null, idiomaPt, false);

        assertThrows(Exception.class, () -> categoriaService.createCategoria(categoria));
    }

    // Update
    @Test
    void testUpdateCategoria()
    {
        Categoria categoria = new Categoria(0, "ficção", "fc", idiomaPt, false);

        String nomeEsperado = categoria.getNome();
        categoriaService.updateCategoria(2, categoria);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertEquals(nomeEsperado, categoriaList.get(1).getNome());
    }

    @Test
    void testUpdateCategoriaIdInvalido()
    {
        Categoria categoria = new Categoria(0, "ficção", "fc", idiomaPt, false);

        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
        assertThrows(NoSuchElementException.class, () -> categoriaService.updateCategoria(667, categoria));
    }

    // Delete
    @Test
    void testDeleteCategoria()
    {
        categoriaService.deleteCategoria(2);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertTrue(categoriaAv.isDesativado());
    }

    @Test
    void testDeleteCategoriaIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> categoriaService.deleteCategoria(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // Get
    @Test
    void testGetCategoria()
    {
        int id = 2;
        assertEquals(categoriaAv, categoriaService.retrieveCategoria(id));
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(id));
    }

    @Test
    void testGetCategoriaIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> categoriaService.retrieveCategoria(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // GetAll
    @Test
    void testGetAllCategoria()
    {
        List<Categoria> retrieved = categoriaService
                .retrieveAllCategoria(null);

        assertEquals(categoriaList.size(), retrieved.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
