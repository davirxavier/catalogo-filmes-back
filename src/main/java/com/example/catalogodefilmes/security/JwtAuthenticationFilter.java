package com.example.catalogodefilmes.security;

import com.example.catalogodefilmes.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Filtro de requisições HTTP para checagem da presença de um token JWT.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.prefix}")
    private String prefix;
    @Value("${app.authController.mapping}")
    private String loginMapping;
    @Value("${app.controller.publicGetMappings}")
    private String[] publicGetMappings;
    @Value("${app.controller.publicPostMappings}")
    private String[] publicPostMappings;
    @Value("${app.controller.publicPutMappings}")
    private String[] publicPutMappings;
    @Value("${app.controller.publicDeleteMappings}")
    private String[] publicDeleteMappings;

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthService AuthService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Filtra a requisição HTTP, caso exista um token JWT, descompacta o mesmo e constrói
     * o contexto de autenticação do Spring Security. Caso o token JWT seja inválido levanta
     * um erro HTTP 400.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if (!shouldFilterRequest(request))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenString = getJwtFromRequest(request);
        if (StringUtils.hasText(tokenString) && tokenProvider.validateToken(tokenString))
        {
            try
            {
                String username = tokenProvider.getUsernameFromJWT(tokenString);

                UserDetails userDetails = AuthService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
            catch (Exception e)
            {
                String message = messageSource.getMessage("error.onauthuser", null,
                        LocaleContextHolder.getLocale());
                log.error(message + e.getMessage());
                response.sendError(403, message + e.getMessage());
            }
        }
        else
        {
            String message = messageSource.getMessage("error.invalidtoken", null,
                    LocaleContextHolder.getLocale());
            response.sendError(400, message);
        }

    }

    /**
     * O token JWT de uma requisição HTTP.
     *
     * @param req requisição HTTP.
     * @return uma String contendo o token JWT mandado na requisição HTTP,
     * se o mesmo não estiver presente retorna null.
     */
    private String getJwtFromRequest(HttpServletRequest req)
    {
        String token = req.getHeader(header);
        if (StringUtils.hasText(token) && token.startsWith(prefix + " "))
        {
            return token.replace(prefix + " ", "");
        }

        return null;
    }

    /**
     * Checa se a requisição passada deve passar pelo filtro de autenticação,
     * utilizando as propriedades no application.properties.
     * @param request requisição HTTP à ser checada, não deve ser nula;
     * @return retorna verdadeiro se
     */
    private boolean shouldFilterRequest(@NotNull HttpServletRequest request)
    {
        String[] mappingsToCheck = null;

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        if (method == HttpMethod.GET)
        {
            mappingsToCheck = publicGetMappings;
        }
        else if (method == HttpMethod.POST)
        {
            mappingsToCheck = publicPostMappings;
        }
        else if (method == HttpMethod.PUT)
        {
            mappingsToCheck = publicPutMappings;
        }
        else if (method == HttpMethod.DELETE)
        {
            mappingsToCheck = publicDeleteMappings;
        }

        if (mappingsToCheck == null)
            mappingsToCheck = new String[0];

        AntPathMatcher matcher = new AntPathMatcher();
        for (String mapping : mappingsToCheck)
        {
            if (matcher.match(mapping, request.getRequestURI()))
            {
                return false;
            }
        }
        return true;
    }
}
