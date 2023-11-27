package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioDetailsDTO;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioGetDTO;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPostDTO;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPutDTO;
import com.example.catalogodefilmes.exception.EntityNotFoundException;
import com.example.catalogodefilmes.service.UsuarioService;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para a entidade Usuario.
 */
@RestController
@RequestMapping("${app.usuarioController.mapping}")
public class UsuarioController
{
    private final UsuarioService usuarioService;
    private final ObjectConverter objectConverter;
    private final ControllerConfig controllerConfig;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService,
                             ObjectConverter objectConverter,
                             ControllerConfig controllerConfig,
                             MessageSource messageSource)
    {
        this.usuarioService = usuarioService;
        this.objectConverter = objectConverter;
        this.controllerConfig = controllerConfig;
        this.messageSource = messageSource;
    }

    /**
     * Mapeamento para criação de um novo usuário.
     *
     * @param usuarioPostDTO objeto recebido no post à ser processado e salvo.
     * @return HTTP 200 caso o usuário seja salvo com sucesso ou um erro HTTP
     * definido nas propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> postUsuario(@RequestBody @Valid UsuarioPostDTO usuarioPostDTO)
    {
        Usuario usuario = objectConverter.convert(usuarioPostDTO, Usuario.class);
        try
        {
            usuarioService.createUsuario(usuario);

            return ResponseEntity.status(200).build();
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para retorno de um usuário já salvo.
     *
     * @param id id do usuário desejado.
     * @return Reposta HTTP 200 com um corpo contendo as informações do usuário desejado,
     * HTTP 404 caso o usuário desejado não tenha sido encontrado ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UsuarioGetDTO> getUsuario(@PathVariable int id)
    {
        try
        {
            Usuario usuario = usuarioService.retrieveUsuario(id);
            UsuarioGetDTO dto = objectConverter.convert(usuario, UsuarioGetDTO.class);

            return ResponseEntity.status(200).body(dto);
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Usuario.class.getSimpleName(), Integer.toString(id)},
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
     * Mapeamento para retorno de todos os usuários do sistema.
     *
     * @return HTTP 200 com lista de usuários no corpo ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioGetDTO>> getAllUsuario()
    {
        try
        {
            List<UsuarioGetDTO> dtos = objectConverter.convert(
                    usuarioService.retrieveAllUsuario(),
                    UsuarioGetDTO.class);

            return ResponseEntity.ok(dtos);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(controllerConfig.getGenericExceptionHttpStatus(),
                    e.getMessage());
        }
    }

    /**
     * Mapeamento para atualizar um usuário já existente.
     *
     * @param id             id do usuário à ser atualizado.
     * @param usuarioPutDTO informações à serem salvas.
     * @return HTTP 200 quando a atualização ocorre com sucesso, HTTP 404 caso o usuário
     * desejado não tenha sido encontrado ou um erro HTTP definido nas propriedades da
     * aplicação caso ocorra algum outro problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUsuario(@PathVariable int id,
                                              @RequestBody @Valid UsuarioPutDTO usuarioPutDTO)
    {
        try
        {
            UsuarioDetailsDTO details = (UsuarioDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (id == 1 && details.getId() != 1)
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            Usuario usuario = objectConverter.convert(usuarioPutDTO, Usuario.class);
            usuarioService.updateUsuario(id, usuario);

            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Usuario.class.getSimpleName(), Integer.toString(id)},
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
     * Mapeamento para exclusão de um usuário.
     *
     * @param id id do usuário desejado.
     * @return HTTP 200 caso o usuário seja excluído com sucesso, HTTP 404 caso o
     * usuário desejado não tenha sido encontrado ou um erro HTTP definido nas
     * propriedades da aplicação caso ocorra algum problema no processo.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id)
    {
        try
        {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.status(200).build();
        }
        catch (NoSuchElementException e)
        {
            String message = messageSource.getMessage("error.resnotfound",
                    new String[]{Usuario.class.getSimpleName(), Integer.toString(id)},
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
