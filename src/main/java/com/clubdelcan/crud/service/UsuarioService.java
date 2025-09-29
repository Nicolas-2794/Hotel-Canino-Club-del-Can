package com.clubdelcan.crud.service;

import com.clubdelcan.crud.entity.Rol;
import com.clubdelcan.crud.entity.Usuario;
import com.clubdelcan.crud.repository.RolRepository;
import com.clubdelcan.crud.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.encoder = encoder;
    }

    /** Crea los roles base si no existen (puedes llamarlo al arrancar la app). */
    public void ensureBaseRoles() {
        rolRepo.findByNombre("ROLE_ADMIN").orElseGet(() -> rolRepo.save(Rol.builder().nombre("ROLE_ADMIN").build()));
        rolRepo.findByNombre("ROLE_CLIENTE").orElseGet(() -> rolRepo.save(Rol.builder().nombre("ROLE_CLIENTE").build()));
    }

    // ---------------- Consultas ----------------

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtener(Long id) {
        return usuarioRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Rol> listarRoles() {
        return rolRepo.findAll();
    }

    // ---------------- Comandos ----------------

    @Transactional
    public Usuario crear(Usuario u, Set<String> rolesSeleccionados) {
        if (usuarioRepo.existsByEmail(u.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        // Encripta password
        u.setPassword(encoder.encode(u.getPassword()));
        // Asigna roles (ROLE_CLIENTE por defecto si vienen vacíos)
        u.setRoles(resolveRoles(rolesSeleccionados));
        return usuarioRepo.save(u);
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario changes, Set<String> rolesSeleccionados) {
        Usuario u = usuarioRepo.findById(id).orElseThrow();

        // Si cambió email, valida duplicado
        if (!u.getEmail().equalsIgnoreCase(changes.getEmail())
                && usuarioRepo.existsByEmail(changes.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        u.setNombre(changes.getNombre());
        u.setEmail(changes.getEmail());
        u.setActivo(changes.getActivo() != null ? changes.getActivo() : u.getActivo());

        // Cambia password solo si viene algo
        if (changes.getPassword() != null && !changes.getPassword().isBlank()) {
            u.setPassword(encoder.encode(changes.getPassword()));
        }

        // Actualiza roles (si no mandas ninguno → queda ROLE_CLIENTE)
        u.setRoles(resolveRoles(rolesSeleccionados));

        return usuarioRepo.save(u);
    }

    @Transactional
    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    // ---------------- Helpers ----------------

    private Set<Rol> resolveRoles(Set<String> names) {
        Set<Rol> out = new HashSet<>();
        if (names == null || names.isEmpty()) {
            rolRepo.findByNombre("ROLE_CLIENTE").ifPresent(out::add);
            return out;
        }
        for (String n : names) {
            Rol rol = rolRepo.findByNombre(n)
                    .orElseThrow(() -> new IllegalArgumentException("Rol inexistente: " + n));
            out.add(rol);
        }
        return out;
    }
}
