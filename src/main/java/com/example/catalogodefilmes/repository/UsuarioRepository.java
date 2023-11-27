package com.example.catalogodefilmes.repository;

import com.example.catalogodefilmes.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para a entidade Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{
    /**
     * Retorna o primeiro usuário que contém o username passado.
     * @param username username usado na busca.
     * @return o usuário que contém o username ou null caso o mesmo não exista.
     */
    public Usuario findFirstByUsername(String username);
    /**
     * Retorna o primeiro usuário que contém o e-mail passado.
     * @param email e-mail usado na busca.
     * @return o usuário que contém o e-mail ou null caso o mesmo não exista.
     */
    public Usuario findFirstByEmail(String email);
    /**
     * Retorna o primeiro usuário que contém o CPF passado.
     * @param cpf CPF usado na busca.
     * @return o usuário que contém o CPF ou null caso o mesmo não exista.
     */
    public Usuario findFirstByCpf(String cpf);
}
