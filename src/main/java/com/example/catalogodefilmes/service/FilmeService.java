package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Filme;
import com.example.catalogodefilmes.repository.FilmeRepository;
import com.sun.istack.Nullable;
import org.modelmapper.internal.util.Lists;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Objects.isNull;

/**
 * Serviço provedor de filmes.
 */
@Service
public class FilmeService
{
    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository)
    {
        this.filmeRepository = filmeRepository;
    }

    /**
     * Cria um novo filme no DataSource.
     *
     * @param filme filme à ser criado.
     */
    public void createFilme(Filme filme)
    {
        filmeRepository.save(filme);
    }

    /**
     * Atualiza o filme já existente pelo id. Lança um exceção se o mesmo não existir.
     *
     * @param id      id do filme à ser atualizado.
     * @param filme informações do filme à serem salvas.
     * @throws NoSuchElementException se o filme com id passado não existir.
     */
    public void updateFilme(int id, Filme filme)
    {
        if (filmeRepository.existsById(id))
        {
            filme.setId(id);
            filmeRepository.save(filme);
        } else
        {
            throw new NoSuchElementException();
        }
    }

    /**
     * Exclui um filme. Lança uma exceção caso o filme com id passado não exista.
     *
     * @param id id do filme à ser excluido.
     * @throws NoSuchElementException se o filme com id passado não existir.
     */
    public void deleteFilme(int id)
    {
        Filme filme = filmeRepository.findById(id).orElseThrow();
        filme.setDesativado(true);

        filmeRepository.save(filme);
    }

    /**
     * Retorna o filme por meio de seu id. Lança uma exceção caso o filme com id passado não exista.
     *
     * @param id id do filme desejado.
     * @return instância de Filme com id desejado.
     * @throws NoSuchElementException se o filme com id passado não existir.
     */
    public Filme retrieveFilme(int id)
    {
        Filme filme = filmeRepository.findById(id).orElseThrow();
        return filme;
    }

    /**
     * Retorna todos os filmes disponíveis no repositório.
     *
     * @return List de Filme contendo todos os filmes.
     */
    public List<Filme> retrieveAllFilme()
    {
        return Lists.from(filmeRepository.findAll().iterator());
    }

    /**
     * Retorna todos os filmes disponíveis no repositório, filtrando pelo nome da
     * categoria e por linguagem, se passados, e utilizando paginação.
     *
     * @return List de Filme contendo todos os filmes.
     */
    public Slice<Filme> retrieveAllFilme(@Nullable Pageable pageable,
                                         @Nullable Integer categoria,
                                         @Nullable String lang,
                                         @Nullable String titulo,
                                         @Nullable String sinopse,
                                         @Nullable Integer anoLancamento,
                                         @Nullable Boolean desativados)
    {
        String tituloCp = (isNull(titulo)) ? "" : titulo;
        String sinopseCp = (isNull(sinopse)) ? "" : sinopse;
        desativados = (isNull(desativados)) ? false : desativados;

        boolean validLang = StringUtils.hasText(lang);
        boolean validCategoria = !isNull(categoria);
        if (validCategoria && validLang)
        {
            return filmeRepository.findAllByCategoriaAndIdioma(pageable, categoria, lang, desativados);
        }
        if (validCategoria)
        {
            return filmeRepository.findAllByCategoria_Id(pageable, categoria, desativados);
        }
        if (validLang)
        {
            LocalDate start = null;
            LocalDate end = null;
            if (!isNull(anoLancamento) && anoLancamento > 0)
            {
                start = LocalDate.of(anoLancamento, Month.JANUARY, 1);
                end = LocalDate.of(anoLancamento, Month.DECEMBER, 31);
            }

            return filmeRepository.findAllForSearch(pageable, tituloCp, sinopseCp, start, end, lang, desativados);
        }

        return filmeRepository.findAll(pageable);
    }

}
