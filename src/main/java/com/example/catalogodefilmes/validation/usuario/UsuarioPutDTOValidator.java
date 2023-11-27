package com.example.catalogodefilmes.validation.usuario;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioPutDTO;
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
public class UsuarioPutDTOValidator implements ConstraintValidator<ValidUsuarioPutDTO, UsuarioPutDTO>
{
    private final UsuarioService usuarioService;
    @Autowired
    private MessageSource messageSource;

    public UsuarioPutDTOValidator(UsuarioService usuarioService)
    {
        this.usuarioService = usuarioService;
    }

    /**
     * Checa se o e-mail e o CPF passados no DTO já estão em uso.
     * @param dto
     * @param constraintValidatorContext
     * @return verdadeiro caso os dois não estejam sendo usados e falso caso sim.
     */
    @Override
    public boolean isValid(UsuarioPutDTO dto, ConstraintValidatorContext constraintValidatorContext)
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
     * Retorna verdadeiro caso o usuário passado o e o dto cotenham o mesmo id
     * e falso caso contrário.
     */
    private boolean checkUsuario(Usuario usuario, UsuarioPutDTO dto)
    {
        return isNull(usuario) ||
                usuario.getId() == dto.getId();
    }
}
