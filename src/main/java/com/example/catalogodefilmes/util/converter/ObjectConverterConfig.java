package com.example.catalogodefilmes.util.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe para injeção do Bean do ObjectConverter.
 */
@Configuration
public class ObjectConverterConfig
{
    @Bean
    public ObjectConverter getConverter()
    {
        return new ObjectConverterImpl();
    }
}
