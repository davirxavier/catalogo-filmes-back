package com.example.catalogodefilmes.security;

import com.example.catalogodefilmes.entity.dto.usuario.UsuarioDetailsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Classe para gerenciamento de tokens JWT. Utiliza o pacote io.jsonwebtoken.
 */
@Component
@Slf4j
public class JwtTokenProvider
{
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    /**
     * Gera um token JWT das informações de autenticação passadas.
     * @param authentication informações de autenticação do usuário.
     * @return string contendo um token JWT válido.
     */
    public String genToken(Authentication authentication)
    {
        UsuarioDetailsDTO user = (UsuarioDetailsDTO) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Valida um token JWT.
     * @param authToken token JWT à ser validado.
     * @return verdadeiro se o token é válido e falso se não.
     */
    public boolean validateToken(String authToken)
    {
        try
        {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Retorna o username codificado em um token JWT.
     * @param jwt token JWT.
     * @return username do token.
     */
    public String getUsernameFromJWT(String jwt)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
