package com.example.catalogodefilmes.validation.usuario;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotação para validação de UsuarioPostDTO.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsuarioPutDTOValidator.class)
@Documented
public @interface ValidUsuarioPutDTO
{
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
