package com.example.catalogodefilmes.entity.dto.usuario;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.validation.usuario.ValidUsername;
import com.example.catalogodefilmes.validation.usuario.ValidUsuarioPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Classe usada no lugar de Usuario em operações POST no UsuarioController.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ValidUsuarioPostDTO

public class UsuarioPostDTO
{
    @NotNull
    private Idioma idioma;

    @Size(max = 32)
    @NotBlank
    @ValidUsername
    private String username;

    @NotBlank
    private String nome;

    // TODO Validação de CPF mais frouxa
    //@CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String telefone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String perfil;

    private boolean desativado;
}
