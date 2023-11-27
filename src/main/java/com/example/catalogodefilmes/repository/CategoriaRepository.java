package com.example.catalogodefilmes.repository;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>
{
    /**
     * Checa se existe uma categoria com o nome passado.
     * @param nome para realização da busca.
     * @return verdadeiro se existir uma categoria com esse nome e falso se não.
     */
    public boolean existsCategoriaByNome(String nome);

    public List<Categoria> findAllByIdioma_TagAndDesativadoFalse(String lang);

    public Categoria findFirstByNomeAndIdioma(String nome, Idioma idioma);

    public Categoria findFirstByTag(String tag);
}
