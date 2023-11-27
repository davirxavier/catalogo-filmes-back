package com.example.catalogodefilmes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exceção lançada quando uma entidade buscada não é encontrada. Lança um erro HTTP 404 - NOT FOUND
 * se usada no Spring.
 */
public class EntityNotFoundException extends ResponseStatusException
{
    /**
     * Exceção lançada quando uma entidade buscada não é encontrada. Lança um erro HTTP 404 - NOT FOUND
     * se usada no Spring.
     */
    public EntityNotFoundException(String localizedMsg)
    {
        super(HttpStatus.NOT_FOUND, localizedMsg);
    }
}
