package com.example.catalogodefilmes.entity.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioLoginDTO
{
    @NotBlank
    private String username;
    @NotBlank
    private String senha;
}
