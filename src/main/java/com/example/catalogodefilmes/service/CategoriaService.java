package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.repository.CategoriaRepository;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Serviço provedor de categorias.
 */
@Service
public class CategoriaService
{
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository)
    {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Cria uma nova categoria no DataSource.
     *
     * @param categoria categoria à ser criada.
     */
    public void createCategoria(Categoria categoria)
    {
        categoriaRepository.save(categoria);
    }

    /**
     * Atualiza a categoria já existente pelo id. Lança um exceção se a mesma não existir.
     *
     * @param id      id da categoria à ser atualizada.
     * @param categoria informações da categoria à serem salvas.
     * @throws NoSuchElementException se a categoria com id passado não existir.
     */
    public void updateCategoria(int id, Categoria categoria)
    {
        if (categoriaRepository.existsById(id))
        {
            categoria.setId(id);
            categoriaRepository.save(categoria);
        } else
        {
            throw new NoSuchElementException();
        }
    }

    /**
     * Exclui uma categoria. Lança uma exceção caso a categoria com id passado não exista.
     *
     * @param id id da categoria à ser excluida.
     * @throws NoSuchElementException se a categoria com id passado não existir.
     */
    public void deleteCategoria(int id)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow();
        categoria.setDesativado(true);

        categoriaRepository.save(categoria);
    }

    /**
     * Retorna a categoria por meio de seu id. Lança uma exceção caso a categoria com id passado não exista.
     *
     * @param id id da categoria desejada.
     * @return instância de Categoria com id desejado.
     * @throws NoSuchElementException se a categoria com id passado não existir.
     */
    public Categoria retrieveCategoria(int id)
    {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow();
        return categoria;
    }

    /**
     * Retorna todos as categoria disponíveis no repositório.
     *
     * @param lang se passado retorna todas as categorias que não estão
     *             desativadas com um idioma que contém essa tag de língua. Ex.: "pt-BR".
     * @return List de Categoria contendo todas as categorias.
     */
    public List<Categoria> retrieveAllCategoria(@Nullable String lang)
    {
        if (StringUtils.hasText(lang))
        {
            return categoriaRepository.findAllByIdioma_TagAndDesativadoFalse(lang);
        }

        return categoriaRepository.findAll();
    }

    /**
     * Checa se existe uma categoria com o nome passado.
     * @param nome para realização da busca.
     * @return verdadeiro se existir uma categoria com esse nome e falso se não.
     */
    public Categoria retrieveByNomeAndIdioma(String nome, Idioma idioma)
    {
        return categoriaRepository.findFirstByNomeAndIdioma(nome, idioma);
    }

    public Categoria retrieveByTag(String tag)
    {
        return categoriaRepository.findFirstByTag(tag);
    }
}
