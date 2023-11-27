package com.example.catalogodefilmes.entity.dto.usuario;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.validation.usuario.ValidUsuarioPutDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Classe usada no lugar de Usuario em operações POST no UsuarioController.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ValidUsuarioPutDTO
public class UsuarioPutDTO
{
    @Min(1)
    int id;

    @NotNull
    private Idioma idioma;

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

    /**
     * Nova senha à ser inserida.
     */
    private String senha;

    private String perfil;

    private boolean desativado;
}
