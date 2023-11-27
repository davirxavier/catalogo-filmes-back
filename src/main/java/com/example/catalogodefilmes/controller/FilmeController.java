package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Filme;
import com.example.catalogodefilmes.entity.dto.filme.FilmeGetDTO;
import com.example.catalogodefilmes.entity.dto.filme.FilmePostDTO;
import com.example.catalogodefilmes.exception.EntityNotFoundException;
import com.example.catalogodefilmes.service.FilmeService;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Controlador REST para a entidade Filme.
 */
@RestController
@RequestMapping("${app.filmeController.mapping}")
public class FilmeController
{
    private final FilmeService filmeService;
    private final ObjectConverter objectConverter;
    private final ControllerConfig controllerConfig;
    private final MessageSource messageSource;

    public FilmeController(FilmeService filmeService,
                           ObjectConverter objectConverter,
                           ControllerConfig controllerConfig,
                           MessageSource messageSource)
    {
        this.filmeService = filmeService;
        this.objectConverter = objectConverter;
        this.controllerConfig = controllerConfig;
        this.messageSource = messageSource;
    }

    /**
     * Mapeamento para criação de um novo filme.
     *
     * @param filmePostDTO objeto recebido no post à ser processado e salvo.
     * @return HTTP 200 caso o filme seja salvo com sucesso ou um erro HTTP
     * definido nas propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> postFilme(@RequestBody @Valid FilmePostDTO filmePostDTO)
    {
        Filme filme = objectConverter.convert(filmePostDTO, Filme.class);
        try
        {
            filmeService.createFilme(filme);

            return ResponseEntity.status(200).build();
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para retorno de um filme já salvo.
     *
     * @param id id do filme desejado.
     * @return Reposta HTTP 200 com um corpo contendo as informações do filme desejado,
     * HTTP 404 caso o filme desejado não tenha sido encontrado ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<FilmeGetDTO> getFilme(@PathVariable int id)
    {
        try
        {
            Filme filme = filmeService.retrieveFilme(id);

            byte[] img = filme.getImagem();
            filme.setImagem(new byte[0]);

            FilmeGetDTO dto = objectConverter.convert(filme, FilmeGetDTO.class);
            dto.setImagem(img);

            return ResponseEntity.status(200).body(dto);
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Filme.class.getSimpleName(), Integer.toString(id)},
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
     * Mapeamento para retorno de todos os filmes do sistema.
     *
     * @return HTTP 200 com lista de filmes no corpo ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public ResponseEntity<List<FilmeGetDTO>> getAllFilme()
//    {
//        try
//        {
//            List<FilmeGetDTO> dtos = objectConverter.convert(
//                    filmeService.retrieveAllFilme(),
//                    FilmeGetDTO.class);
//
//            return ResponseEntity.ok(dtos);
//        }
//        catch (Exception e)
//        {
//            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
//                    e.getMessage());
//        }
//    }

    /**
     * Mapeamento para retorno de todos os filmes do sistema utilizando paginação.
     *
     * @return HTTP 200 com lista de filmes no corpo ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Slice<FilmeGetDTO>> getAllFilme(@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable,
                                                          @RequestParam(value = "categoria", required = false) Integer categoria,
                                                          @RequestParam(value = "titulo", required = false) String titulo,
                                                          @RequestParam(value = "sinopse", required = false) String sinopse,
                                                          @RequestParam(value = "anoLancamento", required = false) Integer ano,
                                                          @RequestParam(value = "desativados", required = false) Boolean desativados,
                                                          @RequestHeader(value = "accept-language", required = false) String langTag)
    {
        try
        {
            Slice<Filme> filmesPage = filmeService.retrieveAllFilme(pageable, categoria, langTag, titulo, sinopse, ano, desativados);
            Slice<FilmeGetDTO> dtos = filmesPage.map(filme ->
            {
                byte[] img = filme.getImagem();
                filme.setImagem(new byte[0]);

                FilmeGetDTO dto = objectConverter.convert(filme, FilmeGetDTO.class);
                dto.setImagem(img);
                return dto;
            });

            return ResponseEntity.ok(dtos);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para atualizar um filme já existente.
     *
     * @param id             id do filme à ser atualizado.
     * @param filmePostDTO informações à serem salvas.
     * @return HTTP 200 quando a atualização ocorre com sucesso, HTTP 404 caso o filme
     * desejado não tenha sido encontrado ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateFilme(@PathVariable int id,
                                              @RequestBody @Valid FilmePostDTO filmePostDTO)
    {
        try
        {
            Filme filme = objectConverter.convert(filmePostDTO, Filme.class);
            filmeService.updateFilme(id, filme);

            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Filme.class.getSimpleName(), Integer.toString(id)},
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
     * Mapeamento para exclusão de um filme.
     *
     * @param id id do filme desejado.
     * @return HTTP 200 caso o filme seja excluído com sucesso, HTTP 404 caso o
     * filme desejado não tenha sido encontrado ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteFilme(@PathVariable int id)
    {
        try
        {
            filmeService.deleteFilme(id);
            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Filme.class.getSimpleName(), Integer.toString(id)},
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
