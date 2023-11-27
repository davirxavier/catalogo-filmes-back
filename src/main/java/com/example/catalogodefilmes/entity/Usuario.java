package com.example.catalogodefilmes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "usuario")
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "idioma")
    private Idioma idioma;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String nome;

    // TODO Validação de CPF mais frouxa
    //@CPF
    @NotBlank
    @Column(unique = true)
    private String cpf;

    @NotBlank
    private String telefone;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String senha;

    private String perfil;

    private boolean desativado;
}
