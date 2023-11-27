package com.example.catalogodefilmes.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resposta de uma tentativa de login com sucesso.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiTokenResponse
{
    private String token;
}
