package com.TuSuperFinal.TuSuperTRON.service;

import com.TuSuperFinal.TuSuperTRON.entity.Rol;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.repository.RolRepository;
import com.TuSuperFinal.TuSuperTRON.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, 
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario, String rolNombre) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
        }
        
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        Rol rol = rolRepository.findByNombre(rolNombre)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
        usuario.agregarRol(rol);
        
        return usuarioRepository.save(usuario);
    }

    public Usuario registrarCliente(Usuario usuario) {
        return registrarUsuario(usuario, "ROLE_USUARIO");
    }

    public Usuario registrarTendero(Usuario usuario) {
        return registrarUsuario(usuario, "ROLE_TENDERO");
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    public List<Usuario> listarPorRol(String rolNombre) {
        return usuarioRepository.findByRolNombre(rolNombre);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void desactivar(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        });
    }

    public void activar(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        });
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    public void cambiarPassword(Long usuarioId, String nuevaPassword) {
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);
        });
    }

    public long contarUsuarios() {
        return usuarioRepository.count();
    }
}
