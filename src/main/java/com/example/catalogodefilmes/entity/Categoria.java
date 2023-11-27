package com.example.catalogodefilmes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Categoria
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique = true)
    private String nome;

    @NotNull
    private String tag;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idioma")
    private Idioma idioma;

    private boolean desativado;
}
