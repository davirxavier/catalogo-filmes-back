package com.example.catalogodefilmes.validation.usuario;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String>
{
    private final UsuarioService usuarioService;
    @Autowired
    private MessageSource messageSource;

    public UsernameValidator(UsuarioService usuarioService)
    {
        this.usuarioService = usuarioService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext)
    {
        Usuario usuario = usuarioService.retrieveByUsername(username);
        boolean ret = isNull(usuario);

        if (!ret)
        {
            String message = messageSource.getMessage("error.usernameexistente", null,
                    LocaleContextHolder.getLocale());

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    message).addConstraintViolation();
        }

        return ret;
    }
}
