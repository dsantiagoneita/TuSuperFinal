package com.TuSuperFinal.TuSuperTRON.service;

import com.TuSuperFinal.TuSuperTRON.entity.ItemPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Pedido;
import com.TuSuperFinal.TuSuperTRON.entity.Pedido.EstadoPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.repository.PedidoRepository;
import com.TuSuperFinal.TuSuperTRON.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    public Pedido crearPedido(Usuario usuario) {
        Pedido pedido = new Pedido(usuario);
        pedido.setDireccionEntrega(usuario.getDireccion());
        return pedidoRepository.save(pedido);
    }

    public Pedido agregarProducto(Long pedidoId, Long productoId, Integer cantidad) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
        
        if (!producto.hayStock() || producto.getCantidad() < cantidad) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        }
        
        Optional<ItemPedido> itemExistente = pedido.getItems().stream()
            .filter(item -> item.getProducto().getId().equals(productoId))
            .findFirst();
        
        if (itemExistente.isPresent()) {
            ItemPedido item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            ItemPedido nuevoItem = new ItemPedido(producto, cantidad);
            pedido.agregarItem(nuevoItem);
        }
        
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarCantidadItem(Long pedidoId, Long itemId, Integer nuevaCantidad) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .ifPresent(item -> {
                if (nuevaCantidad <= 0) {
                    pedido.removerItem(item);
                } else {
                    item.setCantidad(nuevaCantidad);
                }
            });
        
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    public Pedido removerItem(Long pedidoId, Long itemId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.getItems().removeIf(item -> item.getId().equals(itemId));
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    public Pedido confirmarPedido(Long pedidoId, String direccionEntrega, String observaciones) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (pedido.getItems().isEmpty()) {
            throw new RuntimeException("No se puede confirmar un pedido vacío");
        }
        
        for (ItemPedido item : pedido.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getCantidad() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setCantidad(producto.getCantidad() - item.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.setDireccionEntrega(direccionEntrega);
        pedido.setObservaciones(observaciones);
        pedido.setEstado(EstadoPedido.CONFIRMADO);
        
        return pedidoRepository.save(pedido);
    }

    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (pedido.getEstado() == EstadoPedido.CONFIRMADO || 
            pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            for (ItemPedido item : pedido.getItems()) {
                Producto producto = item.getProducto();
                producto.setCantidad(producto.getCantidad() + item.getCantidad());
                productoRepository.save(producto);
            }
        }
        
        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Optional<Pedido> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    public List<Pedido> listarPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaPedidoDesc(usuario);
    }

    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> listarPendientes() {
        return pedidoRepository.findPedidosPendientesOrdenados(EstadoPedido.CONFIRMADO);
    }

    public List<Pedido> listarActivos() {
        return pedidoRepository.findPedidosActivos();
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllOrdenadosPorFecha();
    }

    public List<Pedido> listarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findPedidosPorFecha(inicio, fin);
    }

    public Long contarPorEstado(EstadoPedido estado) {
        return pedidoRepository.contarPorEstado(estado);
    }

    public BigDecimal calcularTotalVentas() {
        return pedidoRepository.findByEstado(EstadoPedido.ENTREGADO).stream()
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long contarPedidos() {
        return pedidoRepository.count();
    }
}
