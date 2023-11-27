package com.example.catalogodefilmes.entity.dto.categoria;

import com.example.catalogodefilmes.entity.Idioma;
import com.example.catalogodefilmes.validation.categoria.ValidCategoriaPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ValidCategoriaPostDTO
public class CategoriaPostDTO
{
    private int id;

    @NotNull
    private String nome;

    @NotNull
    private String tag;

    @NotNull
    private Idioma idioma;

    private boolean desativado;
}
