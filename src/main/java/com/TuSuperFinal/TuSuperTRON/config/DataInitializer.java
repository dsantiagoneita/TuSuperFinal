package com.TuSuperFinal.TuSuperTRON.config;

import com.TuSuperFinal.TuSuperTRON.entity.Producto;
import com.TuSuperFinal.TuSuperTRON.entity.Rol;
import com.TuSuperFinal.TuSuperTRON.entity.Usuario;
import com.TuSuperFinal.TuSuperTRON.repository.ProductoRepository;
import com.TuSuperFinal.TuSuperTRON.repository.RolRepository;
import com.TuSuperFinal.TuSuperTRON.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository, 
                          UsuarioRepository usuarioRepository,
                          ProductoRepository productoRepository,
                          PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        inicializarRoles();
        inicializarUsuarios();
        inicializarProductos();
    }

    private void inicializarRoles() {
        if (!rolRepository.existsByNombre("ROLE_USUARIO")) {
            rolRepository.save(new Rol("ROLE_USUARIO"));
        }
        if (!rolRepository.existsByNombre("ROLE_TENDERO")) {
            rolRepository.save(new Rol("ROLE_TENDERO"));
        }
        if (!rolRepository.existsByNombre("ROLE_ADMIN")) {
            rolRepository.save(new Rol("ROLE_ADMIN"));
        }
    }

    private void inicializarUsuarios() {
        // Usuario Admin
        if (!usuarioRepository.existsByEmail("admin@tusuper.com")) {
            Usuario admin = new Usuario("Administrador", "admin@tusuper.com", 
                passwordEncoder.encode("admin123"));
            admin.setTelefono("3001234567");
            admin.setDireccion("Oficina Central TuSuper");
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").orElseThrow();
            admin.agregarRol(rolAdmin);
            usuarioRepository.save(admin);
        }

        // Usuario Tendero (Neita)
        if (!usuarioRepository.existsByEmail("neita@tusuper.com")) {
            Usuario tendero = new Usuario("Neita Tendero", "neita@tusuper.com", 
                passwordEncoder.encode("tendero123"));
            tendero.setTelefono("3109876543");
            tendero.setDireccion("Tienda TuSuper - Calle 10 #5-20");
            Rol rolTendero = rolRepository.findByNombre("ROLE_TENDERO").orElseThrow();
            tendero.agregarRol(rolTendero);
            usuarioRepository.save(tendero);
        }

        // Usuario Cliente (Andrey)
        if (!usuarioRepository.existsByEmail("andrey@cliente.com")) {
            Usuario cliente = new Usuario("Andrey Cliente", "andrey@cliente.com", 
                passwordEncoder.encode("cliente123"));
            cliente.setTelefono("3205551234");
            cliente.setDireccion("Carrera 15 #45-30, Bogotá");
            Rol rolUsuario = rolRepository.findByNombre("ROLE_USUARIO").orElseThrow();
            cliente.agregarRol(rolUsuario);
            usuarioRepository.save(cliente);
        }
    }

    private void inicializarProductos() {
        if (productoRepository.count() == 0) {
            // Productos colombianos con precios en COP
            crearProducto("Café Juan Valdez Clásico 500g", "Café colombiano premium 100% arábica", 
                new BigDecimal("28500"), 50, "7702032100018", "Bebidas", "Juan Valdez", "/images/cafe-juan-valdez.jpg");
            
            crearProducto("Panela Doña Panela 1kg", "Panela orgánica del Valle del Cauca", 
                new BigDecimal("4500"), 100, "7701234567890", "Endulzantes", "Doña Panela", "/images/panela.jpg");
            
            crearProducto("Arroz Diana 5kg", "Arroz blanco premium colombiano", 
                new BigDecimal("22900"), 80, "7702535001015", "Granos", "Diana", "/images/arroz-diana.jpg");
            
            crearProducto("Aceite Gourmet 3L", "Aceite vegetal premium", 
                new BigDecimal("35900"), 40, "7702004001238", "Aceites", "Gourmet", "/images/aceite-gourmet.jpg");
            
            crearProducto("Chocolate Corona 500g", "Chocolate de mesa tradicional", 
                new BigDecimal("12800"), 60, "7702007001396", "Bebidas", "Corona", "/images/chocolate-corona.jpg");
            
            crearProducto("Bocadillo Veleño 12 unidades", "Bocadillo tradicional de Vélez", 
                new BigDecimal("8500"), 45, "7703456789012", "Dulces", "Veleño", "/images/bocadillo.jpg");
            
            crearProducto("Aguardiente Antioqueño 750ml", "Aguardiente sin azúcar", 
                new BigDecimal("42000"), 30, "7702918001124", "Licores", "Antioqueño", "/images/aguardiente.jpg");
            
            crearProducto("Leche Alpina Entera 1L", "Leche entera pasteurizada", 
                new BigDecimal("4200"), 120, "7702001148011", "Lácteos", "Alpina", "/images/leche-alpina.jpg");
            
            crearProducto("Queso Costeño 500g", "Queso fresco de la Costa", 
                new BigDecimal("15800"), 35, "7704567890123", "Lácteos", "Costeño", "/images/queso-costeno.jpg");
            
            crearProducto("Arepas Don Maíz 10 unidades", "Arepas blancas precocidas", 
                new BigDecimal("6900"), 70, "7702847001033", "Panadería", "Don Maíz", "/images/arepas.jpg");
            
            crearProducto("Frijol Rojo Caraota 1kg", "Frijol rojo seleccionado", 
                new BigDecimal("8900"), 55, "7705678901234", "Granos", "Caraota", "/images/frijol.jpg");
            
            crearProducto("Chorizo Santarrosano 500g", "Chorizo tradicional antioqueño", 
                new BigDecimal("18500"), 25, "7706789012345", "Carnes", "Santarrosano", "/images/chorizo.jpg");
            
            crearProducto("Empanadas Congeladas x6", "Empanadas de carne listas para freír", 
                new BigDecimal("9800"), 40, "7707890123456", "Congelados", "La Especial", "/images/empanadas.jpg");
            
            crearProducto("Ron Medellín 8 Años 750ml", "Ron añejo colombiano", 
                new BigDecimal("68000"), 20, "7702918002831", "Licores", "Ron Medellín", "/images/ron-medellin.jpg");
            
            crearProducto("Salchichas Zenú 500g", "Salchichas premium", 
                new BigDecimal("12500"), 50, "7702007000115", "Carnes", "Zenú", "/images/salchichas.jpg");
            
            crearProducto("Yogurt Alpina Griego 150g", "Yogurt griego natural", 
                new BigDecimal("3800"), 80, "7702001148295", "Lácteos", "Alpina", "/images/yogurt-griego.jpg");
            
            crearProducto("Galletas Noel Saltinas 3 tacos", "Galletas de sal tradicionales", 
                new BigDecimal("5600"), 90, "7702007012262", "Snacks", "Noel", "/images/saltinas.jpg");
            
            crearProducto("Azúcar Manuelita 2.5kg", "Azúcar blanca refinada", 
                new BigDecimal("9500"), 65, "7702123456789", "Endulzantes", "Manuelita", "/images/azucar.jpg");
            
            crearProducto("Pasta Doria Spaghetti 500g", "Pasta italiana estilo colombiano", 
                new BigDecimal("4300"), 100, "7702025101013", "Granos", "Doria", "/images/pasta-doria.jpg");
            
            crearProducto("Salsa de Tomate Fruco 400g", "Salsa de tomate natural", 
                new BigDecimal("6200"), 75, "7702047001010", "Salsas", "Fruco", "/images/salsa-fruco.jpg");
        }
    }

    private void crearProducto(String nombre, String descripcion, BigDecimal precio, 
                               Integer cantidad, String codigoBarras, String categoria, 
                               String marca, String imagen) {
        Producto producto = new Producto(nombre, precio, cantidad);
        producto.setDescripcion(descripcion);
        producto.setCodigoBarras(codigoBarras);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setImagen(imagen);
        productoRepository.save(producto);
    }
}
