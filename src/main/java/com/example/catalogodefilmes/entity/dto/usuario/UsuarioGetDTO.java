package com.example.catalogodefilmes.entity.dto.usuario;

import com.example.catalogodefilmes.entity.Idioma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Classe usada no lugar de Usuario para operações GET no UsuarioController.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioGetDTO
{
    private int id;

    @NotNull
    private Idioma idioma;

    @NotBlank
    private String username;

    @NotBlank
    private String nome;

    @CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String telefone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private String perfil;

    private boolean desativado;
}
