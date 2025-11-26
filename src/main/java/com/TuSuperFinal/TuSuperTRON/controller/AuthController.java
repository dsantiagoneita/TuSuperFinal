package com.TuSuperFinal.TuSuperTRON.controller;

import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "expired", required = false) String expired,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión exitosamente");
        }
        if (expired != null) {
            model.addAttribute("error", "Tu sesión ha expirado. Por favor inicia sesión nuevamente");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario,
                           @RequestParam String confirmarPassword,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!usuario.getPassword().equals(confirmarPassword)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/registro";
            }
            
            if (usuarioService.existePorEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/registro";
            }
            
            usuarioService.registrarCliente(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Por favor inicia sesión");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}
