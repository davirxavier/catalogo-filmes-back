package com.example.catalogodefilmes.util.converter;

import java.util.List;

/**
 * Interface para conversão entre objetos.
 */
public interface ObjectConverter
{
    /**
     * Converte um objeto T em um objeto S.
     * @param t
     * @param s
     * @param <T>
     * @param <S>
     * @return novo S
     */
    <T, S> S convert(T t, Class<S> s);

    /**
     * Converte uma lista de objetos T para uma lista de objetos S.
     * @param listt
     * @param s
     * @param <T>
     * @param <S>
     * @return List<S>
     */
    <T, S> List<S> convert(List<T> listt, Class<S> s);
}
