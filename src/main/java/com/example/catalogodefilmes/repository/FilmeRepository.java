package com.example.catalogodefilmes.repository;

import com.example.catalogodefilmes.entity.Filme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static java.util.Objects.isNull;

/**
 * Repositório para a entidade Filme.
 */
@Repository
public interface FilmeRepository extends PagingAndSortingRepository<Filme, Integer>
{
    @Query(value = "SELECT f FROM Filme f WHERE f.categoria.id = :categoria " +
                   "AND (f.desativado = false OR f.desativado = :desativado)")
    public Page<Filme> findAllByCategoria_Id(Pageable pageable, int categoria, boolean desativado);

    @Query(value = "SELECT f FROM Filme f WHERE f.idioma.tag = :lang " +
            "AND (f.desativado = false OR f.desativado = :desativado)")
    public Page<Filme> findAllByIdioma_Tag(Pageable pageable, String lang, boolean desativado);

    @Query(value = "SELECT f FROM Filme f WHERE f.idioma.tag = :lang AND f.categoria.id = :categoria " +
            "AND (f.desativado = false OR f.desativado = :desativado)")
    public Page<Filme> findAllByCategoriaAndIdioma(Pageable pageable, int categoria, String lang, boolean desativado);

    @Query(value = "SELECT f FROM Filme f WHERE UPPER(f.titulo) LIKE CONCAT('%',UPPER(:titulo),'%') " +
                   "AND UPPER(f.sinopse) LIKE CONCAT('%',UPPER(:sinopse),'%') " +
                   "AND f.idioma.tag = :lang AND (f.desativado = false OR f.desativado = :desativado)")
    public Page<Filme> findAllForSearch(Pageable pageable,
                                        String titulo,
                                        String sinopse, String lang, boolean desativado);

    @Query(value = "SELECT f FROM Filme f WHERE UPPER(f.titulo) LIKE CONCAT('%',UPPER(:titulo),'%') " +
                   "AND UPPER(f.sinopse) LIKE CONCAT('%',UPPER(:sinopse),'%') " +
                   "AND f.dataLancamento >= :start AND f.dataLancamento <= :end " +
                   "AND f.idioma.tag = :lang AND (f.desativado = false OR f.desativado = :desativado)")
    public Page<Filme> findAllForSearchBetweenDates(Pageable pageable,
                                                    String titulo,
                                                    String sinopse,
                                                    LocalDate start,
                                                    LocalDate end,
                                                    String lang,
                                                    boolean desativado);

    /**
     * Faz uma busca no banco de dados com diversas propriedades de um filme.
     * @param pageable usado para paginação.
     * @param titulo título do filme.
     * @param sinopse sinopse do filme.
     * @param start data de início para busca de filmes.
     * @param end data de fim para busca de filmes.
     * @param lang linguagem dos filmes desejada.
     * @param desativado se retorna filmes marcados como desativados.
     * @return página contendo os filmes.
     */
    default Page<Filme> findAllForSearch(Pageable pageable,
                                         String titulo,
                                         String sinopse,
                                         LocalDate start,
                                         LocalDate end,
                                         String lang,
                                         boolean desativado)
    {
        if (isNull(start) || isNull(end))
        {
            return findAllForSearch(pageable, titulo, sinopse, lang, desativado);
        }
        else
        {
            return findAllForSearchBetweenDates(pageable,
                    titulo,
                    sinopse,
                    start,
                    end,
                    lang,
                    desativado);
        }
    }
}
