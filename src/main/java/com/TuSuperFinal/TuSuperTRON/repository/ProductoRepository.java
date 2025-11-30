package com.TuSuperFinal.TuSuperTRON.repository;

import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    
    List<Producto> findByCategoria(String categoria);
    
    List<Producto> findByMarca(String marca);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.nombre LIKE %:nombre%")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.cantidad <= p.cantidadMinima")
    List<Producto> findProductosConStockBajo();
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.cantidad > 0")
    List<Producto> findProductosDisponibles();
    
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.activo = true AND p.categoria IS NOT NULL")
    List<String> findAllCategorias();
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarProductos(@Param("termino") String termino);
    
    boolean existsByCodigoBarras(String codigoBarras);
}
