package com.example.catalogodefilmes.util.converter;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para conversão entre objetos. Serve como um wrapper para a classe ModelMapper.
 */
public class ObjectConverterImpl implements ObjectConverter
{
    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * Converte um objeto T em um objeto S utilizando o ModelMapper.
     * @param t
     * @param s
     * @param <T>
     * @param <S>
     * @return
     */
    public <T, S> S convert(T t, Class<S> s)
    {
        return modelMapper.map(t, s);
    }

    /**
     * Converte uma lista de objetos T em uma lista de objetos S utilizando o ModelMapper.
     * @param listt
     * @param s
     * @param <T>
     * @param <S>
     * @return
     */
    public <T, S> List<S> convert(List<T> listt, Class<S> s)
    {
        List<S> ss = new ArrayList<>(listt.size());
        for (T t : listt)
        {
            ss.add(modelMapper.map(t, s));
        }

        return ss;
    }
}
