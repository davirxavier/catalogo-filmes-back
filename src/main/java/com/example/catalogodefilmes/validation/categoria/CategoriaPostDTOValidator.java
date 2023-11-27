package com.example.catalogodefilmes.validation.categoria;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.dto.categoria.CategoriaPostDTO;
import com.example.catalogodefilmes.service.CategoriaService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

/**
 *  Classe validadora para CategoriaPostDTO.
 */
public class CategoriaPostDTOValidator implements ConstraintValidator<ValidCategoriaPostDTO, CategoriaPostDTO>
{
    private final CategoriaService categoriaService;
    private final MessageSource messageSource;

    public CategoriaPostDTOValidator(CategoriaService categoriaService,
                                     MessageSource messageSource)
    {
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean isValid(CategoriaPostDTO dto, ConstraintValidatorContext constraintValidatorContext)
    {
        try
        {
            boolean valid = true;

            Categoria buscada = categoriaService.retrieveByNomeAndIdioma(dto.getNome(), dto.getIdioma());
            if (!isNull(buscada) && buscada.getId() != dto.getId())
            {
                String message = messageSource.getMessage("error.nomeexistente",
                        null, LocaleContextHolder.getLocale());
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
                valid = false;
            }

            buscada = categoriaService.retrieveByTag(dto.getTag());
            if (!isNull(buscada) && buscada.getId() != dto.getId())
            {
                String message = messageSource.getMessage("error.tagexistente",
                        null, LocaleContextHolder.getLocale());
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
                valid = false;
            }

            return valid;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
