package com.TuSuperFinal.TuSuperTRON.controller;

import com.TuSuperFinal.TuSuperTRON.entity.Pedido;
import com.TuSuperFinal.TuSuperTRON.entity.Pedido.EstadoPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.service.PedidoService;
import com.TuSuperFinal.TuSuperTRON.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tendero")
public class TenderoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public TenderoController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        List<Pedido> pedidosPendientes = pedidoService.listarPorEstado(EstadoPedido.CONFIRMADO);
        List<Pedido> pedidosEnPreparacion = pedidoService.listarPorEstado(EstadoPedido.EN_PREPARACION);
        List<Producto> productosStockBajo = productoService.listarConStockBajo();
        
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        model.addAttribute("pedidosEnPreparacion", pedidosEnPreparacion);
        model.addAttribute("cantidadPendientes", pedidosPendientes.size());
        model.addAttribute("cantidadEnPreparacion", pedidosEnPreparacion.size());
        model.addAttribute("productosStockBajo", productosStockBajo);
        model.addAttribute("totalProductos", productoService.contarProductosActivos());
        return "tendero/panel";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(@RequestParam(required = false) String estado, Model model) {
        List<Pedido> pedidos;
        
        if (estado != null && !estado.isEmpty()) {
            try {
                EstadoPedido estadoEnum = EstadoPedido.valueOf(estado);
                pedidos = pedidoService.listarPorEstado(estadoEnum);
                model.addAttribute("estadoSeleccionado", estado);
            } catch (IllegalArgumentException e) {
                pedidos = pedidoService.listarActivos();
            }
        } else {
            pedidos = pedidoService.listarActivos();
        }
        
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", EstadoPedido.values());
        return "tendero/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("estados", EstadoPedido.values());
        return "tendero/detalle-pedido";
    }

    @PostMapping("/pedidos/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam String estado,
                                RedirectAttributes redirectAttributes) {
        try {
            EstadoPedido nuevoEstado = EstadoPedido.valueOf(estado);
            pedidoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/tendero/pedidos/" + id;
    }

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String categoria, Model model) {
        List<Producto> productos;
        
        if (categoria != null && !categoria.isEmpty()) {
            productos = productoService.listarPorCategoria(categoria);
            model.addAttribute("categoriaSeleccionada", categoria);
        } else {
            productos = productoService.listarTodos();
        }
        
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", productoService.listarCategorias());
        return "tendero/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", productoService.listarCategorias());
        return "tendero/producto-form";
    }

    @GetMapping("/productos/{id}/editar")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", productoService.listarCategorias());
        return "tendero/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/tendero/productos/nuevo";
        }
        return "redirect:/tendero/productos";
    }

    @PostMapping("/productos/{id}/stock")
    public String actualizarStock(@PathVariable Long id,
                                  @RequestParam Integer cantidad,
                                  RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarStock(id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Stock actualizado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tendero/productos";
    }

    @PostMapping("/productos/{id}/desactivar")
    public String desactivarProducto(@PathVariable Long id, 
                                     RedirectAttributes redirectAttributes) {
        productoService.desactivar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Producto desactivado");
        return "redirect:/tendero/productos";
    }

    @PostMapping("/productos/{id}/activar")
    public String activarProducto(@PathVariable Long id, 
                                  RedirectAttributes redirectAttributes) {
        productoService.activar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Producto activado");
        return "redirect:/tendero/productos";
    }

    @GetMapping("/productos/codigo-barras")
    public String buscarPorCodigoBarras(@RequestParam String codigo, Model model) {
        Producto producto = productoService.procesarCodigoBarrasUSB(codigo)
            .orElse(null);
        
        model.addAttribute("producto", producto);
        model.addAttribute("codigoBuscado", codigo);
        
        if (producto == null) {
            model.addAttribute("mensaje", "Producto no encontrado con código: " + codigo);
        }
        
        return "tendero/resultado-codigo-barras";
    }

    @GetMapping("/inventario")
    public String inventario(Model model) {
        List<Producto> productos = productoService.listarTodos();
        List<Producto> stockBajo = productoService.listarConStockBajo();
        
        model.addAttribute("productos", productos);
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("productosActivos", productoService.contarProductosActivos());
        return "tendero/inventario";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        model.addAttribute("totalPedidos", pedidoService.contarPedidos());
        model.addAttribute("pedidosPendientes", pedidoService.contarPorEstado(EstadoPedido.CONFIRMADO));
        model.addAttribute("pedidosEntregados", pedidoService.contarPorEstado(EstadoPedido.ENTREGADO));
        model.addAttribute("totalVentas", pedidoService.calcularTotalVentas());
        model.addAttribute("productosStockBajo", productoService.listarConStockBajo().size());
        return "tendero/reportes";
    }

    /**
     * Muestra la vista de escáner de código de barras USB.
     * Esta vista permite escanear productos con un lector de código de barras USB
     * o buscar manualmente por código de barras.
     * 
     * @return Vista del escáner de código de barras
     */
    @GetMapping("/escaner")
    public String escanerCodigoBarras() {
        return "tendero/escaner-codigo-barras";
    }
}
