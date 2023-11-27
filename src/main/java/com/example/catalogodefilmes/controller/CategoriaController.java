package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.dto.categoria.CategoriaGetDTO;
import com.example.catalogodefilmes.entity.dto.categoria.CategoriaPostDTO;
import com.example.catalogodefilmes.exception.EntityNotFoundException;
import com.example.catalogodefilmes.service.CategoriaService;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para a entidade Categoria.
 */
@RestController
@RequestMapping("${app.categoriaController.mapping}")
public class CategoriaController
{
    private final CategoriaService categoriaService;
    private final ObjectConverter objectConverter;
    private final ControllerConfig controllerConfig;
    private final MessageSource messageSource;

    public CategoriaController(CategoriaService categoriaService,
                               ObjectConverter objectConverter,
                               ControllerConfig controllerConfig,
                               MessageSource messageSource)
    {
        this.categoriaService = categoriaService;
        this.objectConverter = objectConverter;
        this.controllerConfig = controllerConfig;
        this.messageSource = messageSource;
    }

    /**
     * Mapeamento para criação de uma nova categoria.
     *
     * @param categoriaPostDTO objeto recebido no post à ser processado e salvo.
     * @return HTTP 200 caso a categoria seja salva com sucesso ou um erro HTTP
     * definido nas propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> postCategoria(@RequestBody @Valid CategoriaPostDTO categoriaPostDTO)
    {
        Categoria categoria = objectConverter.convert(categoriaPostDTO, Categoria.class);
        try
        {
            categoriaService.createCategoria(categoria);

            return ResponseEntity.status(200).build();
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para retorno de uma categoria já salva.
     *
     * @param id id da categoria desejada.
     * @return Reposta HTTP 200 com um corpo contendo as informações da categoria desejada,
     * HTTP 404 caso a categoria desejada não tenha sido encontrada ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoriaGetDTO> getCategoria(@PathVariable int id)
    {
        try
        {
            Categoria categoria = categoriaService.retrieveCategoria(id);
            CategoriaGetDTO dto = objectConverter.convert(categoria, CategoriaGetDTO.class);

            return ResponseEntity.status(200).body(dto);
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Categoria.class.getSimpleName(), Integer.toString(id)},
                    LocaleContextHolder.getLocale());

            throw new EntityNotFoundException(message);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para retorno de todas as categorias do sistema.
     *
     * @return HTTP 200 com lista de categorias no corpo ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CategoriaGetDTO>> getAllCategoria(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String lang)
    {
        try
        {
            List<CategoriaGetDTO> dtos = objectConverter.convert(
                    categoriaService.retrieveAllCategoria(lang),
                    CategoriaGetDTO.class);

            return ResponseEntity.ok(dtos);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para atualizar uma categoria já existente.
     *
     * @param id             id da categoria à ser atualizada.
     * @param categoriaPostDTO informações à serem salvas.
     * @return HTTP 200 quando a atualização ocorre com sucesso, HTTP 404 caso a categoria
     * desejada não tenha sido encontrada ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategoria(@PathVariable int id,
                                              @RequestBody @Valid CategoriaPostDTO categoriaPostDTO)
    {
        try
        {
            Categoria categoria = objectConverter.convert(categoriaPostDTO, Categoria.class);
            categoriaService.updateCategoria(id, categoria);

            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Categoria.class.getSimpleName(), Integer.toString(id)},
                    LocaleContextHolder.getLocale());

            throw new EntityNotFoundException(message);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para exclusão de uma categoria.
     *
     * @param id id da categoria desejada.
     * @return HTTP 200 caso a categoria seja excluída com sucesso, HTTP 404 caso a
     * categoria desejada não tenha sido encontrado ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCategoria(@PathVariable int id)
    {
        try
        {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Categoria.class.getSimpleName(), Integer.toString(id)},
                    LocaleContextHolder.getLocale());

            throw new EntityNotFoundException(message);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }
}
