package com.TuSuperFinal.TuSuperTRON.repository;

import com.TuSuperFinal.TuSuperTRON.entity.Pedido;
import com.TuSuperFinal.TuSuperTRON.entity.Pedido.EstadoPedido;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByUsuario(Usuario usuario);
    
    List<Pedido> findByUsuarioOrderByFechaPedidoDesc(Usuario usuario);
    
    List<Pedido> findByEstado(EstadoPedido estado);
    
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado ORDER BY p.fechaPedido ASC")
    List<Pedido> findPedidosPendientesOrdenados(@Param("estado") EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :inicio AND :fin ORDER BY p.fechaPedido DESC")
    List<Pedido> findPedidosPorFecha(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long contarPorEstado(@Param("estado") EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllOrdenadosPorFecha();
    
    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('ENTREGADO', 'CANCELADO') ORDER BY p.fechaPedido ASC")
    List<Pedido> findPedidosActivos();
}
