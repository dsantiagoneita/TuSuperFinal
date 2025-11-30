package com.TuSuperFinal.TuSuperTRON.controller;

import com.TuSuperFinal.TuSuperTRON.entity.Pedido;
import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.service.CarritoService;
import com.TuSuperFinal.TuSuperTRON.service.PedidoService;
import com.TuSuperFinal.TuSuperTRON.service.ProductoService;
import com.TuSuperFinal.TuSuperTRON.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final CarritoService carritoService;

    public UsuarioController(ProductoService productoService, 
                             PedidoService pedidoService,
                             UsuarioService usuarioService,
                             CarritoService carritoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.carritoService = carritoService;
    }

    private Usuario obtenerUsuarioActual(Authentication authentication) {
        return usuarioService.buscarPorEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping("/panel")
    public String panel(Authentication authentication, Model model) {
        Usuario usuario = obtenerUsuarioActual(authentication);
        List<Pedido> pedidosRecientes = pedidoService.listarPorUsuario(usuario);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("pedidosRecientes", pedidosRecientes.stream().limit(5).toList());
        model.addAttribute("totalPedidos", pedidosRecientes.size());
        model.addAttribute("itemsCarrito", carritoService.getCantidadTotal());
        return "usuario/panel";
    }

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String categoria,
                                  @RequestParam(required = false) String buscar,
                                  Model model) {
        List<Producto> productos;
        
        if (buscar != null && !buscar.trim().isEmpty()) {
            productos = productoService.buscarProductos(buscar);
            model.addAttribute("busqueda", buscar);
        } else if (categoria != null && !categoria.isEmpty()) {
            productos = productoService.listarPorCategoria(categoria);
            model.addAttribute("categoriaSeleccionada", categoria);
        } else {
            productos = productoService.listarDisponibles();
        }
        
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", productoService.listarCategorias());
        model.addAttribute("itemsCarrito", carritoService.getCantidadTotal());
        return "usuario/productos";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId,
                                   @RequestParam(defaultValue = "1") Integer cantidad,
                                   RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (!producto.hayStock() || producto.getCantidad() < cantidad) {
                redirectAttributes.addFlashAttribute("error", "Stock insuficiente");
                return "redirect:/usuario/productos";
            }
            
            carritoService.agregarProducto(producto, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/productos";
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.getTotal());
        model.addAttribute("cantidadItems", carritoService.getCantidadTotal());
        return "usuario/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCarrito(@RequestParam Long productoId,
                                    @RequestParam Integer cantidad,
                                    RedirectAttributes redirectAttributes) {
        carritoService.actualizarCantidad(productoId, cantidad);
        redirectAttributes.addFlashAttribute("mensaje", "Carrito actualizado");
        return "redirect:/usuario/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Long productoId,
                                     RedirectAttributes redirectAttributes) {
        carritoService.removerProducto(productoId);
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
        return "redirect:/usuario/carrito";
    }

    @PostMapping("/carrito/vaciar")
    public String vaciarCarrito(RedirectAttributes redirectAttributes) {
        carritoService.vaciar();
        redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado");
        return "redirect:/usuario/carrito";
    }

    @GetMapping("/checkout")
    public String checkout(Authentication authentication, Model model) {
        if (carritoService.estaVacio()) {
            return "redirect:/usuario/carrito";
        }
        
        Usuario usuario = obtenerUsuarioActual(authentication);
        model.addAttribute("usuario", usuario);
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.getTotal());
        return "usuario/checkout";
    }

    @PostMapping("/pedido/crear")
    public String crearPedido(Authentication authentication,
                              @RequestParam String direccionEntrega,
                              @RequestParam(required = false) String observaciones,
                              RedirectAttributes redirectAttributes) {
        try {
            if (carritoService.estaVacio()) {
                redirectAttributes.addFlashAttribute("error", "El carrito está vacío");
                return "redirect:/usuario/carrito";
            }
            
            Usuario usuario = obtenerUsuarioActual(authentication);
            Pedido pedido = pedidoService.crearPedido(usuario);
            
            for (var entry : carritoService.getItems().entrySet()) {
                var item = entry.getValue();
                pedidoService.agregarProducto(pedido.getId(), 
                    item.getProducto().getId(), item.getCantidad());
            }
            
            pedido = pedidoService.confirmarPedido(pedido.getId(), direccionEntrega, observaciones);
            carritoService.vaciar();
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Pedido #" + pedido.getNumeroPedido() + " creado exitosamente");
            return "redirect:/usuario/pedidos/" + pedido.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear pedido: " + e.getMessage());
            return "redirect:/usuario/checkout";
        }
    }

    @GetMapping("/pedidos")
    public String misPedidos(Authentication authentication, Model model) {
        Usuario usuario = obtenerUsuarioActual(authentication);
        List<Pedido> pedidos = pedidoService.listarPorUsuario(usuario);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("itemsCarrito", carritoService.getCantidadTotal());
        return "usuario/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, 
                                Authentication authentication, 
                                Model model) {
        Usuario usuario = obtenerUsuarioActual(authentication);
        Pedido pedido = pedidoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/usuario/pedidos";
        }
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("itemsCarrito", carritoService.getCantidadTotal());
        return "usuario/detalle-pedido";
    }

    @PostMapping("/pedidos/{id}/cancelar")
    public String cancelarPedido(@PathVariable Long id,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            
            if (!pedido.getUsuario().getId().equals(usuario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para cancelar este pedido");
                return "redirect:/usuario/pedidos";
            }
            
            if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE && 
                pedido.getEstado() != Pedido.EstadoPedido.CONFIRMADO) {
                redirectAttributes.addFlashAttribute("error", "No se puede cancelar este pedido");
                return "redirect:/usuario/pedidos/" + id;
            }
            
            pedidoService.cancelarPedido(id);
            redirectAttributes.addFlashAttribute("mensaje", "Pedido cancelado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/pedidos/" + id;
    }

    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {
        Usuario usuario = obtenerUsuarioActual(authentication);
        model.addAttribute("usuario", usuario);
        model.addAttribute("itemsCarrito", carritoService.getCantidadTotal());
        return "usuario/perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(Authentication authentication,
                                   @RequestParam String nombre,
                                   @RequestParam String telefono,
                                   @RequestParam String direccion,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(authentication);
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuarioService.actualizar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/perfil";
    }
}
