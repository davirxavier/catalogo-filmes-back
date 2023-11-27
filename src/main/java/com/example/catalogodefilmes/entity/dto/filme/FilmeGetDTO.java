package com.example.catalogodefilmes.entity.dto.filme;

import com.example.catalogodefilmes.entity.Categoria;
import com.example.catalogodefilmes.entity.Idioma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilmeGetDTO
{
    private int id;

    @NotBlank
    private String titulo;

    private String sinopse;

    @NotNull
    private Idioma idioma;

    @NotNull
    private Categoria categoria;

    private byte[] imagem;

    private String imagemAlt;

    @NotNull
    private LocalDate dataLancamento;

    @NotNull
    private Duration duracao;

    private boolean desativado;
}
