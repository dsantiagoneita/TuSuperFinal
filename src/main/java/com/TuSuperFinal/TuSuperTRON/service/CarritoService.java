package com.TuSuperFinal.TuSuperTRON.service;

import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CarritoService {

    private final Map<Long, ItemCarrito> items = new HashMap<>();

    public static class ItemCarrito {
        private Producto producto;
        private Integer cantidad;

        public ItemCarrito(Producto producto, Integer cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public void setProducto(Producto producto) {
            this.producto = producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getSubtotal() {
            return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public void agregarProducto(Producto producto, Integer cantidad) {
        if (items.containsKey(producto.getId())) {
            ItemCarrito item = items.get(producto.getId());
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            items.put(producto.getId(), new ItemCarrito(producto, cantidad));
        }
    }

    public void actualizarCantidad(Long productoId, Integer cantidad) {
        if (items.containsKey(productoId)) {
            if (cantidad <= 0) {
                items.remove(productoId);
            } else {
                items.get(productoId).setCantidad(cantidad);
            }
        }
    }

    public void removerProducto(Long productoId) {
        items.remove(productoId);
    }

    public void vaciar() {
        items.clear();
    }

    public Map<Long, ItemCarrito> getItems() {
        return new HashMap<>(items);
    }

    public int getCantidadTotal() {
        return items.values().stream()
            .mapToInt(ItemCarrito::getCantidad)
            .sum();
    }

    public BigDecimal getTotal() {
        return items.values().stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public int getCantidadItems() {
        return items.size();
    }

    public boolean contieneProducto(Long productoId) {
        return items.containsKey(productoId);
    }

    public Integer getCantidadProducto(Long productoId) {
        if (items.containsKey(productoId)) {
            return items.get(productoId).getCantidad();
        }
        return 0;
    }
}
