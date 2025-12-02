package com.TuSuperFinal.TuSuperTRON.controller;

import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST API para lectura de códigos de barras mediante lector USB.
 * 
 * Este controlador proporciona endpoints JSON para búsqueda asíncrona de productos
 * por código de barras, diseñado para funcionar con lectores USB HID que emulan
 * entrada de teclado.
 * 
 * DEPENDENCIAS UTILIZADAS:
 * - spring-boot-starter-webmvc (ya presente en pom.xml)
 * 
 * NOTA: Los lectores de código de barras USB funcionan como dispositivos HID
 * (Human Interface Device) que emulan un teclado. No se requieren librerías
 * especiales para USB - la captura se realiza via JavaScript en el frontend.
 * 
 * @author TuSuper Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/codigo-barras")
public class CodigoBarrasApiController {

    private final ProductoService productoService;

    public CodigoBarrasApiController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Busca un producto por su código de barras.
     * Endpoint diseñado para respuesta rápida del lector USB.
     * 
     * @param codigo El código de barras escaneado
     * @return ResponseEntity con el producto encontrado o mensaje de error
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorCodigoBarras(
            @RequestParam String codigo) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validar que el código no esté vacío
        if (codigo == null || codigo.trim().isEmpty()) {
            response.put("encontrado", false);
            response.put("mensaje", "Código de barras vacío");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Limpiar el código (remover caracteres no numéricos que puedan venir del lector)
        String codigoLimpio = codigo.trim().replaceAll("[^0-9]", "");
        
        // Buscar producto usando el servicio existente
        Optional<Producto> productoOpt = productoService.procesarCodigoBarrasUSB(codigoLimpio);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            response.put("encontrado", true);
            response.put("producto", crearProductoDTO(producto));
            response.put("mensaje", "Producto encontrado");
        } else {
            response.put("encontrado", false);
            response.put("codigoBuscado", codigoLimpio);
            response.put("mensaje", "No se encontró producto con código: " + codigoLimpio);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Valida si un código de barras tiene formato válido.
     * 
     * @param codigo El código de barras a validar
     * @return ResponseEntity con el resultado de la validación
     */
    @GetMapping("/validar")
    public ResponseEntity<Map<String, Object>> validarCodigoBarras(
            @RequestParam String codigo) {
        
        Map<String, Object> response = new HashMap<>();
        String codigoLimpio = codigo != null ? codigo.trim().replaceAll("[^0-9]", "") : "";
        
        boolean esValido = productoService.validarCodigoBarras(codigoLimpio);
        
        response.put("codigo", codigoLimpio);
        response.put("valido", esValido);
        response.put("mensaje", esValido 
            ? "Código de barras válido (8-14 dígitos)" 
            : "Código de barras inválido. Debe tener entre 8 y 14 dígitos numéricos.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica si un código de barras ya existe en el sistema.
     * Útil para el registro de nuevos productos.
     * 
     * @param codigo El código de barras a verificar
     * @return ResponseEntity indicando si el código existe
     */
    @GetMapping("/existe")
    public ResponseEntity<Map<String, Object>> existeCodigoBarras(
            @RequestParam String codigo) {
        
        Map<String, Object> response = new HashMap<>();
        String codigoLimpio = codigo != null ? codigo.trim() : "";
        
        Optional<Producto> productoOpt = productoService.buscarPorCodigoBarras(codigoLimpio);
        boolean existe = productoOpt.isPresent();
        
        response.put("codigo", codigoLimpio);
        response.put("existe", existe);
        
        if (existe) {
            response.put("producto", crearProductoDTO(productoOpt.get()));
            response.put("mensaje", "El código de barras ya está registrado");
        } else {
            response.put("mensaje", "El código de barras está disponible");
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un DTO simplificado del producto para la respuesta JSON.
     * Evita problemas de serialización circular.
     */
    private Map<String, Object> crearProductoDTO(Producto producto) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", producto.getId());
        dto.put("nombre", producto.getNombre());
        dto.put("descripcion", producto.getDescripcion());
        dto.put("precio", producto.getPrecio());
        dto.put("cantidad", producto.getCantidad());
        dto.put("cantidadMinima", producto.getCantidadMinima());
        dto.put("codigoBarras", producto.getCodigoBarras());
        dto.put("categoria", producto.getCategoria());
        dto.put("marca", producto.getMarca());
        dto.put("imagen", producto.getImagen());
        dto.put("activo", producto.isActivo());
        dto.put("stockBajo", producto.tieneStockBajo());
        dto.put("hayStock", producto.hayStock());
        return dto;
    }
}
