package com.example.catalogodefilmes.localization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Slf4j
@Configuration
public class LocaleConfig implements WebMvcConfigurer
{
    @Value("${app.locale.default}")
    private String defaultLocale;
    @Value("${app.locale.langHeader}")
    private String langHeader;

    @Bean
    public LocaleResolver localeResolver()
    {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(new Locale(defaultLocale));
        return slr;
    }

    @Bean()
    public ResourceBundleMessageSource messageSource()
    {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }
}
