package com.example.catalogodefilmes.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@TypeDef(
        typeClass = PostgreSQLIntervalType.class,
        defaultForType = Duration.class
)
public class Filme
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String titulo;

    private String sinopse;

    @ManyToOne
    @JoinColumn(name = "idioma")
    @NotNull
    private Idioma idioma;

    @ManyToOne
    @JoinColumn(name = "categoria")
    @NotNull
    private Categoria categoria;

    private byte[] imagem;

    @Column(name = "imagem_alt")
    private String imagemAlt;

    @NotNull
    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @NotNull
    private Duration duracao;

    private boolean desativado;
}
