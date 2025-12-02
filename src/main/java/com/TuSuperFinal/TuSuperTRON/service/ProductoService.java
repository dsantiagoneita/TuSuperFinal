package com.TuSuperFinal.TuSuperTRON.service;

import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto guardar(Producto producto) {
        if (producto.getCodigoBarras() != null && !producto.getCodigoBarras().isEmpty()) {
            if (productoRepository.existsByCodigoBarras(producto.getCodigoBarras())) {
                Optional<Producto> existente = productoRepository.findByCodigoBarras(producto.getCodigoBarras());
                if (existente.isPresent() && !existente.get().getId().equals(producto.getId())) {
                    throw new RuntimeException("El código de barras ya existe: " + producto.getCodigoBarras());
                }
            }
        }
        return productoRepository.save(producto);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarDisponibles() {
        return productoRepository.findProductosDisponibles();
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarProductos(String termino) {
        return productoRepository.buscarProductos(termino);
    }

    public List<String> listarCategorias() {
        return productoRepository.findAllCategorias();
    }

    public List<Producto> listarConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    public void actualizarStock(Long productoId, Integer cantidad) {
        productoRepository.findById(productoId).ifPresent(producto -> {
            producto.setCantidad(producto.getCantidad() + cantidad);
            productoRepository.save(producto);
        });
    }

    public boolean reducirStock(Long productoId, Integer cantidad) {
        Optional<Producto> optProducto = productoRepository.findById(productoId);
        if (optProducto.isPresent()) {
            Producto producto = optProducto.get();
            if (producto.getCantidad() >= cantidad) {
                producto.setCantidad(producto.getCantidad() - cantidad);
                productoRepository.save(producto);
                return true;
            }
        }
        return false;
    }

    public void desactivar(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            producto.setActivo(false);
            productoRepository.save(producto);
        });
    }

    public void activar(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            producto.setActivo(true);
            productoRepository.save(producto);
        });
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public long contarProductos() {
        return productoRepository.count();
    }

    public long contarProductosActivos() {
        return productoRepository.findByActivoTrue().size();
    }

    // Preparación para lector de código de barras USB
    public Optional<Producto> procesarCodigoBarrasUSB(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            return Optional.empty();
        }
        String codigoLimpio = codigoBarras.trim().replaceAll("[^0-9]", "");
        
        // 1. Primero intentar búsqueda exacta
        Optional<Producto> resultado = buscarPorCodigoBarras(codigoLimpio);
        if (resultado.isPresent()) {
            return resultado;
        }
        
        // 2. Intentar búsqueda sin el último dígito (dígito de verificación)
        if (codigoLimpio.length() > 8) {
            String codigoSinVerificador = codigoLimpio.substring(0, codigoLimpio.length() - 1);
            resultado = buscarPorCodigoBarras(codigoSinVerificador);
            if (resultado.isPresent()) {
                return resultado;
            }
        }
        
        // 3. Búsqueda flexible: el código escaneado contiene el código de BD o viceversa
        List<Producto> resultadosFlexibles = productoRepository.buscarPorCodigoBarrasFlexible(codigoLimpio);
        if (!resultadosFlexibles.isEmpty()) {
            // Retornar el que tenga el código más similar (mayor coincidencia)
            return resultadosFlexibles.stream()
                .filter(p -> p.getCodigoBarras() != null)
                .min((p1, p2) -> {
                    int diff1 = Math.abs(p1.getCodigoBarras().length() - codigoLimpio.length());
                    int diff2 = Math.abs(p2.getCodigoBarras().length() - codigoLimpio.length());
                    return Integer.compare(diff1, diff2);
                });
        }
        
        return Optional.empty();
    }

    // Método preparatorio para carga masiva de inventario
    public List<Producto> cargarInventarioMasivo(List<Producto> productos) {
        return productoRepository.saveAll(productos);
    }

    // Método preparatorio para validar código de barras
    public boolean validarCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isEmpty()) {
            return false;
        }
        return codigoBarras.matches("^[0-9]{8,14}$");
    }
}
