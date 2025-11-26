# TuSuper - Plataforma de E-commerce Local

Plataforma de comercio electrónico local desarrollada con **Spring Boot 4.0.0** que permite a usuarios locales comprar productos de una tienda y que el tendero reciba y gestione esos pedidos.

## Características

- **Sistema de autenticación** con tres roles: Usuario, Tendero, Admin
- **Catálogo de productos** colombianos con precios en COP
- **Carrito de compras** con gestión de cantidades
- **Sistema de pedidos** con seguimiento de estados
- **Panel de administración** para gestión completa
- **Interfaz moderna** con Bootstrap 5 y Thymeleaf
- **Preparado para código de barras USB**

## Tecnologías

- Java 21
- Spring Boot 4.0.0
- Spring Security 6
- Spring Data JPA
- MySQL 8.0
- Thymeleaf + Bootstrap 5
- Maven

## Requisitos Previos

- JDK 21 o superior
- MySQL 8.0 o superior
- Maven 3.9+
- Eclipse IDE (recomendado) o cualquier IDE Java

## Configuración de Base de Datos

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE TuSuper CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Configurar credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

## Instalación y Ejecución

### Opción 1: Desde Eclipse

1. Importar proyecto como "Existing Maven Project"
2. Click derecho en `TuSuperTronApplication.java`
3. Run As → Spring Boot App
4. Acceder a: http://localhost:8083

### Opción 2: Desde Terminal

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/TuSuperFinal.git
cd TuSuperFinal/TuSuperTRON

# Compilar y ejecutar
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run
```

## Estructura del Proyecto

```
TuSuperTRON/
├── src/main/java/com/TuSuperFinal/TuSuperTRON/
│   ├── config/          # Configuraciones (DataInitializer)
│   ├── controller/      # Controladores MVC
│   ├── entity/          # Entidades JPA
│   ├── repository/      # Repositorios Spring Data
│   ├── security/        # Configuración Spring Security
│   └── service/         # Lógica de negocio
├── src/main/resources/
│   ├── templates/       # Vistas Thymeleaf
│   │   ├── admin/       # Panel administrador
│   │   ├── tendero/     # Panel tendero
│   │   ├── usuario/     # Panel cliente
│   │   └── fragments/   # Componentes reutilizables
│   ├── static/          # Archivos estáticos
│   └── application.properties
└── pom.xml
```

## Usuarios de Prueba

| Rol | Email | Contraseña |
|-----|-------|------------|
| Admin | admin@tusuper.com | admin123 |
| Tendero | neita@tusuper.com | tendero123 |
| Cliente | andrey@cliente.com | cliente123 |

## Flujo del Sistema

### Cliente (Usuario)
1. Registro/Login
2. Navegar catálogo de productos
3. Agregar productos al carrito
4. Realizar pedido
5. Ver historial y estado de pedidos

### Tendero
1. Login
2. Ver pedidos pendientes
3. Cambiar estados de pedidos
4. Gestionar inventario de productos
5. Ver reportes básicos

### Administrador
1. Login
2. Gestionar usuarios
3. Crear nuevos tenderos
4. Ver todos los pedidos
5. Acceso a reportes completos

## Estructura de Ramas Git

```
main                    # Rama principal (producción)
├── usuario/andrey      # Desarrollo funcionalidad cliente
└── tendero/neita       # Desarrollo funcionalidad tendero
```

### Flujo de trabajo recomendado:

```bash
# Crear rama de desarrollo
git checkout -b tendero/neita

# Trabajar y hacer commits
git add .
git commit -m "feat: implementar gestión de pedidos"

# Subir cambios
git push origin tendero/neita

# Cuando esté estable, hacer merge a main
git checkout main
git merge tendero/neita
```

## Funcionalidades Futuras

- [ ] Cálculo de tiempos de entrega
- [ ] Pasarela de pagos (PSE, tarjetas)
- [ ] Carga masiva de inventario (CSV/Excel)
- [ ] Notificaciones en tiempo real
- [ ] Reportes avanzados con gráficos
- [ ] Implementación completa de código de barras USB

## API de Estados de Pedido

| Estado | Descripción |
|--------|-------------|
| PENDIENTE | Pedido creado, esperando confirmación |
| CONFIRMADO | Pedido confirmado por el cliente |
| EN_PREPARACION | Tendero preparando el pedido |
| LISTO | Pedido listo para envío |
| EN_CAMINO | Pedido en camino al cliente |
| ENTREGADO | Pedido entregado exitosamente |
| CANCELADO | Pedido cancelado |

## Configuración del Puerto

El servidor corre en el puerto **8083** por defecto. Para cambiar:

```properties
# application.properties
server.port=8083
```

## Equipo de Desarrollo

- **Neita** - Funcionalidad del Tendero
- **Andrey** - Funcionalidad del Cliente

## Licencia

Este proyecto es de uso educativo.

---

**TuSuper** - Tu tienda local en línea 🛒
