package com.TuSuperFinal.TuSuperTRON.repository;

import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByActivoTrue();
    
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :rolNombre")
    List<Usuario> findByRolNombre(@Param("rolNombre") String rolNombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true AND u.nombre LIKE %:nombre%")
    List<Usuario> buscarPorNombre(@Param("nombre") String nombre);

    /**
     * Busca un usuario por su código de barras de acceso.
     * Utilizado para autenticación de administradores con lector USB.
     */
    Optional<Usuario> findByCodigoBarrasAcceso(String codigoBarrasAcceso);

    /**
     * Verifica si existe un usuario con el código de barras de acceso especificado.
     */
    boolean existsByCodigoBarrasAcceso(String codigoBarrasAcceso);
}
