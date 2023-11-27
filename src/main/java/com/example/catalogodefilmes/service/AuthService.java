package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.entity.dto.usuario.UsuarioDetailsDTO;
import com.example.catalogodefilmes.repository.UsuarioRepository;
import com.example.catalogodefilmes.util.converter.ObjectConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * Serviço para operações relacionadas à autenticação de usuários.
 */
@Service
public class AuthService implements UserDetailsService
{
    private final UsuarioRepository usuarioRepository;
    private final ObjectConverter converter;

    /**
     * Serviço para operações relacionadas à autenticação de usuários.
     *
     * @param usuarioRepository
     * @param converter
     */
    public AuthService(UsuarioRepository usuarioRepository,
                                         ObjectConverter converter)
    {
        this.usuarioRepository = usuarioRepository;
        this.converter = converter;
    }

    /**
     * Busca um usuário por userename e retorna o mapeamento do mesmo em um UserDetails.
     *
     * @param username username do usuário à ser mapeado.
     * @return UserDetails do usuário buscado.
     * @throws UsernameNotFoundException caso o username não seja encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Usuario usuario = usuarioRepository.findFirstByUsername(username);
        if (isNull(usuario))
            throw new UsernameNotFoundException(username);

        return converter.convert(usuario, UsuarioDetailsDTO.class);
    }
}
