package com.TuSuperFinal.TuSuperTRON.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_PREPARACION,
        LISTO,
        EN_CAMINO,
        ENTREGADO,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", unique = true, nullable = false, length = 20)
    private String numeroPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(name = "direccion_entrega", length = 255)
    private String direccionEntrega;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (numeroPedido == null) {
            numeroPedido = generarNumeroPedido();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    private String generarNumeroPedido() {
        return "PED-" + System.currentTimeMillis();
    }

    public Pedido() {}

    public Pedido(Usuario usuario) {
        this.usuario = usuario;
        this.total = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
        calcularTotal();
    }

    public void removerItem(ItemPedido item) {
        items.remove(item);
        item.setPedido(null);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = items.stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCantidadItems() {
        return items.stream()
            .mapToInt(ItemPedido::getCantidad)
            .sum();
    }
}
