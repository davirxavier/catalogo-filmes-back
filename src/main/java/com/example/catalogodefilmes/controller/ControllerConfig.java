package com.example.catalogodefilmes.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@ConfigurationProperties(prefix = "app.controller")
@Data
public class ControllerConfig
{
    private int genericExceptionHttpStatus = 0;

    public HttpStatus getGenericExceptionHttpStatus()
    {
        return HttpStatus.valueOf(genericExceptionHttpStatus);
    }
}
