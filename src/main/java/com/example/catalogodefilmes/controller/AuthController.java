package com.example.catalogodefilmes.controller;

import com.example.catalogodefilmes.entity.dto.usuario.UsuarioLoginDTO;
import com.example.catalogodefilmes.security.ApiTokenResponse;
import com.example.catalogodefilmes.security.JwtTokenProvider;
import com.example.catalogodefilmes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
public class AuthController
{
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.header}")
    private String auth_header;
    @Value("${jwt.prefix}")
    private String auth_prefix;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider)
    {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @RequestMapping(value = "${app.authController.mapping}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiTokenResponse> login(@Valid @RequestBody UsuarioLoginDTO dto)
    {
        try
        {
            if (usuarioService.isUsuarioDesativado(dto.getUsername()))
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "desativado");
            }
        }
        catch (ResponseStatusException e)
        {
            throw e;
        }
        catch (Exception ignored){}

        int status = 200;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.genToken(authentication);

        ApiTokenResponse response = new ApiTokenResponse();
        response.setToken(jwt);
        return ResponseEntity.status(status).header(auth_header, auth_prefix + " " + jwt)
                .body(response);
    }
}
