package com.example.catalogodefilmes.validation.usuario;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPostDTO;
import com.example.catalogodefilmes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

/**
 *  Classe validadora para UsuarioPostDTO.
 */
public class UsuarioPostDTOValidator implements ConstraintValidator<ValidUsuarioPostDTO, UsuarioPostDTO>
{
    private final UsuarioService usuarioService;
    @Autowired
    private MessageSource messageSource;

    public UsuarioPostDTOValidator(UsuarioService usuarioService)
    {
        this.usuarioService = usuarioService;
    }

    @Override
    public boolean isValid(UsuarioPostDTO dto, ConstraintValidatorContext constraintValidatorContext)
    {
        boolean ret = true;
        Usuario usuarioEmail = usuarioService.retrieveByEmail(dto.getEmail());
        Usuario usuarioCpf = usuarioService.retrieveByCpf(dto.getCpf());

        if (!checkUsuario(usuarioEmail, dto))
        {
            String message = messageSource.getMessage("error.emailexistente", null,
                    LocaleContextHolder.getLocale());
            ret = false;
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    message).addPropertyNode("email")
                    .addConstraintViolation();
        }
        if (!checkUsuario(usuarioCpf, dto))
        {
            String message = messageSource.getMessage("error.cpfexistente", null,
                    LocaleContextHolder.getLocale());
            ret = false;
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    message).addPropertyNode("cpf")
                    .addConstraintViolation();
        }

        return ret;
    }

    /**
     * Retorna verdadeiro caso o usuário passado o e o dto cotenham o mesmo username
     * e falso caso contrário.
     */
    private boolean checkUsuario(Usuario usuario, UsuarioPostDTO dto)
    {
        return isNull(usuario);
    }
}
