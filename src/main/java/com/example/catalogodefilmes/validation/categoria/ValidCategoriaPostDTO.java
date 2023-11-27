package com.example.catalogodefilmes.validation.categoria;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotação para validação de CategoriaPostDTO.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoriaPostDTOValidator.class)
@Documented
public @interface ValidCategoriaPostDTO
{
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
