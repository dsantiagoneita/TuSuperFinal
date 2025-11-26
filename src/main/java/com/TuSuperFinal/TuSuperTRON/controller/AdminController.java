package com.TuSuperFinal.TuSuperTRON.controller;

import com.TuSuperFinal.TuSuperTRON.entity.Pedido.EstadoPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.service.PedidoService;
import com.TuSuperFinal.TuSuperTRON.service.ProductoService;
import com.TuSuperFinal.TuSuperTRON.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public AdminController(UsuarioService usuarioService, 
                          ProductoService productoService,
                          PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
        model.addAttribute("totalProductos", productoService.contarProductos());
        model.addAttribute("totalPedidos", pedidoService.contarPedidos());
        model.addAttribute("pedidosPendientes", pedidoService.contarPorEstado(EstadoPedido.CONFIRMADO));
        model.addAttribute("totalVentas", pedidoService.calcularTotalVentas());
        model.addAttribute("usuariosRecientes", usuarioService.listarTodos().stream().limit(5).toList());
        model.addAttribute("pedidosRecientes", pedidoService.listarTodos().stream().limit(5).toList());
        return "admin/panel";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String rol, Model model) {
        if (rol != null && !rol.isEmpty()) {
            model.addAttribute("usuarios", usuarioService.listarPorRol(rol));
            model.addAttribute("rolSeleccionado", rol);
        } else {
            model.addAttribute("usuarios", usuarioService.listarTodos());
        }
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String detalleUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario));
        return "admin/detalle-usuario";
    }

    @PostMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable Long id, 
                                    RedirectAttributes redirectAttributes) {
        usuarioService.desactivar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/activar")
    public String activarUsuario(@PathVariable Long id, 
                                 RedirectAttributes redirectAttributes) {
        usuarioService.activar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario activado");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo-tendero")
    public String nuevoTendero(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/tendero-form";
    }

    @PostMapping("/usuarios/crear-tendero")
    public String crearTendero(@ModelAttribute Usuario usuario,
                               @RequestParam String confirmarPassword,
                               RedirectAttributes redirectAttributes) {
        try {
            if (!usuario.getPassword().equals(confirmarPassword)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/admin/usuarios/nuevo-tendero";
            }
            
            usuarioService.registrarTendero(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Tendero creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/usuarios/nuevo-tendero";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", productoService.listarCategorias());
        return "admin/productos";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(@RequestParam(required = false) String estado, Model model) {
        if (estado != null && !estado.isEmpty()) {
            try {
                EstadoPedido estadoEnum = EstadoPedido.valueOf(estado);
                model.addAttribute("pedidos", pedidoService.listarPorEstado(estadoEnum));
                model.addAttribute("estadoSeleccionado", estado);
            } catch (IllegalArgumentException e) {
                model.addAttribute("pedidos", pedidoService.listarTodos());
            }
        } else {
            model.addAttribute("pedidos", pedidoService.listarTodos());
        }
        model.addAttribute("estados", EstadoPedido.values());
        return "admin/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
        model.addAttribute("estados", EstadoPedido.values());
        return "admin/detalle-pedido";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
        model.addAttribute("totalClientes", usuarioService.listarPorRol("ROLE_USUARIO").size());
        model.addAttribute("totalTenderos", usuarioService.listarPorRol("ROLE_TENDERO").size());
        model.addAttribute("totalProductos", productoService.contarProductos());
        model.addAttribute("productosActivos", productoService.contarProductosActivos());
        model.addAttribute("totalPedidos", pedidoService.contarPedidos());
        model.addAttribute("pedidosEntregados", pedidoService.contarPorEstado(EstadoPedido.ENTREGADO));
        model.addAttribute("totalVentas", pedidoService.calcularTotalVentas());
        return "admin/reportes";
    }
}
