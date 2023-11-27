package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Filme;
import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.repository.FilmeRepository;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmeServiceTests
{
    @MockBean
    FilmeRepository repository;
    @Autowired
    FilmeService filmeService;
    @Autowired
    ObjectConverter converter;

    List<Filme> filmeList;
    Idioma idiomaPt;
    Categoria categoriaAc;

    Filme filme1;
    Filme filme2;

    private static final long DATE_MILIS = 1610980771L;

    @BeforeEach
    void init()
    {
        idiomaPt = new Idioma(1, "Português", "PT", false);
        categoriaAc = new Categoria(1, "Ação", "ac", idiomaPt, false);

        filme1 = new Filme(1, "A viagem submarina de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem nadar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);
        filme2 = new Filme(2, "Alienigenas Terrestres 3: A Volta",
                "Alienígenas voltam a aterrorizar a terra.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);

        filmeList = new ArrayList<Filme>(2);
        Collections.addAll(filmeList, filme1, filme2);

        // Save
        Mockito.when(repository.save(Mockito.any(Filme.class))).thenAnswer(invocation ->
        {
            Filme adicionado = invocation.getArgument(0);

            if (adicionado.getIdioma() == null ||
                    adicionado.getCategoria() == null ||
                    adicionado.getTitulo() == null ||
                    adicionado.getDataLancamento() == null ||
                    adicionado.getDuracao() == null)
                throw new Exception("Constraint violations");

            int pos = adicionado.getId()-1;
            if (pos > 0)
            {
                filmeList.set(pos, adicionado);
            }
            else
            {
                filmeList.add(adicionado);
            }

            return adicionado;
        });
        // Find by id
        Mockito.when(repository.findById(Mockito.anyInt())).thenAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            try
            {
                Filme buscado = filmeList.get(id-1);
                return Optional.ofNullable(buscado);
            }
            catch (Exception e)
            {
                return Optional.empty();
            }
        });
        // Find all
        Mockito.when(repository.findAll()).thenReturn(filmeList);
        Mockito.when(repository.existsById(Mockito.anyInt())).thenAnswer(invocation ->
        {
            int id = invocation.getArgument(0);
            return id > 0 && id <= filmeList.size();
        });
    }

    //Create
    @Test
    void testCreateFilme()
    {
        int tamanhoEsperado = filmeList.size()+1;

        Filme filme = new Filme(1, "A viagem terrestre de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem andar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);
        filmeService.createFilme(filme);

        Mockito.verify(repository, Mockito.times(1))
                .save(Mockito.any(Filme.class));
        assertEquals(tamanhoEsperado, filmeList.size());
        assertEquals(filmeList.get(filmeList.size()-1), filme);
    }

    // TODO Apagar
    @Test
    void testCreateFilmeSemIdioma()
    {
        Filme filme = new Filme(1, "A viagem terrestre de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem andar.", null,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);

        assertThrows(Exception.class, () -> filmeService.createFilme(filme));
    }

    @Test
    void testCreateFilmeCamposNulos()
    {
        Filme filme = new Filme(1, null,
                null, idiomaPt,
                categoriaAc, new byte[0], null, null,
                Duration.ofHours(10), false);

        assertThrows(Exception.class, () -> filmeService.createFilme(filme));
    }

    // Update
    @Test
    void testUpdateFilme()
    {
        Filme filme = new Filme(1, "A viagem terrestre de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem andar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);

        String tituloEsperado = filme.getTitulo();
        filmeService.updateFilme(2, filme);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertEquals(tituloEsperado, filmeList.get(1).getTitulo());
    }

    @Test
    void testUpdateFilmeIdInvalido()
    {
        Filme filme = new Filme(1, "A viagem terrestre de vinte mil alpacas voadoras",
                "Vinte mil alpacas voadoras não sabem andar.", idiomaPt,
                categoriaAc, new byte[0], null, LocalDate.ofEpochDay(DATE_MILIS),
                Duration.ofHours(10), false);

        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
        assertThrows(NoSuchElementException.class, () -> filmeService.updateFilme(667, filme));
    }

    // Delete
    @Test
    void testDeleteFilme()
    {
        filmeService.deleteFilme(2);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        assertTrue(filme2.isDesativado());
    }

    @Test
    void testDeleteFilmeIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> filmeService.deleteFilme(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // Get
    @Test
    void testGetFilme()
    {
        int id = 2;
        assertEquals(filme2, filmeService.retrieveFilme(id));
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(id));
    }

    @Test
    void testGetFilmeIdInvalido()
    {
        assertThrows(NoSuchElementException.class, () -> filmeService.retrieveFilme(667));
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

    // GetAll
    @Test
    void testGetAllFilme()
    {
        List<Filme> retrieved = filmeService.retrieveAllFilme();

        assertEquals(filmeList.size(), retrieved.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
