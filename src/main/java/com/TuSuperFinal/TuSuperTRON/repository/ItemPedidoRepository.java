package com.TuSuperFinal.TuSuperTRON.repository;

import com.TuSuperFinal.TuSuperTRON.entity.ItemPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Pedido;
import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    List<ItemPedido> findByPedido(Pedido pedido);
    
    List<ItemPedido> findByProducto(Producto producto);
    
    @Query("SELECT SUM(ip.cantidad) FROM ItemPedido ip WHERE ip.producto = :producto")
    Long totalVendidosPorProducto(@Param("producto") Producto producto);
}
