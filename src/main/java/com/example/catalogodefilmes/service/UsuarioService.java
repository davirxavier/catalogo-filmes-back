package com.example.catalogodefilmes.service;

import com.example.catalogodefilmes.entity.Usuario;
import com.example.catalogodefilmes.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Objects.isNull;

/**
 * Serviço provedor de usuários.
 */
@Service
public class UsuarioService
{
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder)
    {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário e salva o mesmo com senha em hash.
     *
     * @param usuario usuário à ser criado.
     */
    public void createUsuario(Usuario usuario)
    {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    /**
     * Atualiza o usuário já existente pelo id. Lança um exceção se o mesmo não existir.
     * Transforma a senha em hash caso a mesma seja diferente da salva atualmente.
     *
     * @param id      id do usuário à ser atualizado.
     * @param usuario informações do usuário à serem salvas.
     * @throws NoSuchElementException se o usuário com id passado não existir.
     */
    public void updateUsuario(int id, Usuario usuario)
    {
        Usuario buscado = usuarioRepository.findById(id).orElseThrow();

        if (!StringUtils.hasText(usuario.getSenha()))
        {
            usuario.setSenha(buscado.getSenha());
        }
        if (!buscado.getSenha().equals(usuario.getSenha()))
        {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuario.setId(id);
        usuario.setUsername(buscado.getUsername());
        usuarioRepository.save(usuario);
    }

    /**
     * Exclui um usuário. Lança uma exceção caso o usuário com id passado não exista.
     *
     * @param id id do usuário à ser excluido.
     * @throws NoSuchElementException se o usuário com id passado não existir.
     */
    public void deleteUsuario(int id)
    {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setDesativado(true);

        usuarioRepository.save(usuario);
    }

    /**
     * Retorna o usuário por meio de seu id. Lança uma exceção caso o usuário com id passado não exista.
     *
     * @param id id do usuário desejado.
     * @return instância de Usuario com id desejado.
     * @throws NoSuchElementException se o usuário com id passado não existir.
     */
    public Usuario retrieveUsuario(int id)
    {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return usuario;
    }

    /**
     * Retorna todos os usuários disponíveis no repositório.
     *
     * @return List de Usuario contendo todos os usuários.
     */
    public List<Usuario> retrieveAllUsuario()
    {
        return usuarioRepository.findAll();
    }

    /**
     * Retorna o usuário que contém o e-mail especificado como parâmetro.
     * @param email o e-mail desejado.
     * @return usuário desejado ou null caso não exista nenhum usuário com
     * esse e-mail.
     */
    public Usuario retrieveByEmail(String email)
    {
        Usuario usuario = usuarioRepository.findFirstByEmail(email);
        return usuario;
    }

    /**
     * Retorna o usuário que contém o CPF especificado como parâmetro.
     * @param cpf o CPF desejado.
     * @return usuário desejado ou null caso não exista nenhum usuário com
     * esse CPF.
     */
    public Usuario retrieveByCpf(String cpf)
    {
        Usuario usuario = usuarioRepository.findFirstByCpf(cpf);
        return usuario;
    }

    /**
     * Retorna o usuário que contém o username especificado como parâmetro.
     * @param username o username desejado.
     * @return usuário desejado ou null caso não exista nenhum usuário com
     * esse username.
     */
    public Usuario retrieveByUsername(String username)
    {
        Usuario usuario = usuarioRepository.findFirstByUsername(username);
        return usuario;
    }

    /**
     * Diz se um usuário está desativado ou não.
     * @param username username do usuário à ser buscado.
     * @return true caso o usuário esteja desativado e false se não.
     * @throws NoSuchElementException caso não exista nenhum usuário com o username passado.
     */
    public boolean isUsuarioDesativado(String username) throws NoSuchElementException
    {
        Usuario usuario = usuarioRepository.findFirstByUsername(username);
        if (isNull(usuario))
        {
            throw new NoSuchElementException();
        }

        return usuario.isDesativado();
    }
}
