# TuSuper Design System
## Guía de Diseño Brutalista-Pop Premium

---

## Índice

1. [Filosofía de Diseño](#filosofía-de-diseño)
2. [Tokens de Diseño](#tokens-de-diseño)
3. [Tipografía](#tipografía)
4. [Paleta de Colores](#paleta-de-colores)
5. [Espaciado](#espaciado)
6. [Componentes](#componentes)
7. [Breakpoints y Responsive](#breakpoints-y-responsive)
8. [Accesibilidad](#accesibilidad)
9. [Animaciones](#animaciones)
10. [Checklist de Implementación](#checklist-de-implementación)

---

## Filosofía de Diseño

### Brutalismo Pop Premium

El sistema de diseño de TuSuper fusiona la estética brutalista (honestidad material, formas geométricas, tipografía bold) con elementos pop vibrantes para crear una experiencia visual memorable y funcional.

**Principios fundamentales:**

- **Contraste extremo**: Negro sobre blanco/crema como base
- **Sombras duras**: Sin degradados, desplazamiento sólido
- **Tipografía dominante**: Headlines grandes y capitalizados
- **Colores de acento vibrantes**: Para CTAs y estados
- **Bordes pronunciados**: Sin border-radius (máximo 4px)
- **Geometría clara**: Formas rectangulares definidas

---

## Tokens de Diseño

### Archivo: `tokens.css`

Todos los valores de diseño están centralizados como CSS Custom Properties para mantener consistencia.

```css
/* Ejemplo de uso */
.mi-componente {
    color: var(--ts-black);
    font-family: var(--ts-font-display);
    padding: var(--ts-space-4);
    border: var(--ts-border-medium) solid var(--ts-black);
    box-shadow: var(--ts-shadow-brutal-md);
}
```

---

## Tipografía

### Familias Tipográficas

| Token | Fuente | Uso |
|-------|--------|-----|
| `--ts-font-display` | Playfair Display | Titulares, precios, elementos destacados |
| `--ts-font-body` | Inter | Texto corriente, etiquetas, botones |
| `--ts-font-mono` | JetBrains Mono | Códigos, datos técnicos |

### Escala Tipográfica (Ratio 1.25)

| Token | Tamaño | Uso típico |
|-------|--------|------------|
| `--ts-text-xs` | 12px / 0.75rem | Labels, badges, metadata |
| `--ts-text-sm` | 14px / 0.875rem | Texto secundario, botones pequeños |
| `--ts-text-base` | 16px / 1rem | Texto base de párrafos |
| `--ts-text-lg` | 18px / 1.125rem | Subtítulos, texto enfatizado |
| `--ts-text-xl` | 20px / 1.25rem | H5, títulos de tarjeta |
| `--ts-text-2xl` | 24px / 1.5rem | H4, precios |
| `--ts-text-3xl` | 30px / 1.875rem | H3 |
| `--ts-text-4xl` | 36px / 2.25rem | H2, títulos de sección |
| `--ts-text-5xl` | 48px / 3rem | H1, números destacados |
| `--ts-text-6xl` | 60px / 3.75rem | Display, estadísticas |
| `--ts-text-7xl` | 72px / 4.5rem | Hero, display extra |

### Pesos Tipográficos

| Token | Peso | Uso |
|-------|------|-----|
| `--ts-font-regular` | 400 | Texto corriente |
| `--ts-font-medium` | 500 | Énfasis ligero |
| `--ts-font-semibold` | 600 | Etiquetas, navegación |
| `--ts-font-bold` | 700 | Títulos secundarios |
| `--ts-font-black` | 900 | Headlines, precios, CTAs |

### Reglas Tipográficas

1. **Titulares (h1-h3)**: Siempre en `text-transform: uppercase` y `letter-spacing: -0.025em`
2. **Labels**: `text-transform: uppercase` con `letter-spacing: 0.15em`
3. **Precios**: Siempre con `--ts-font-display` y color `--ts-accent-primary`

---

## Paleta de Colores

### Colores Base

| Token | Hex | Uso |
|-------|-----|-----|
| `--ts-black` | #0A0A0A | Texto principal, bordes, fondos oscuros |
| `--ts-white` | #FAFAFA | Fondos de cards, texto invertido |
| `--ts-cream` | #F5F2ED | Fondo de página principal |

### Colores de Acento

| Token | Hex | Uso | Ratio WCAG |
|-------|-----|-----|------------|
| `--ts-accent-primary` | #FF3D00 | CTAs principales, enlaces activos | 4.5:1 sobre blanco |
| `--ts-accent-secondary` | #FFD600 | Highlights, badges especiales | 1.8:1 (usar con negro) |
| `--ts-accent-electric` | #00E5FF | Estados hover, info | 3.2:1 (usar con negro) |
| `--ts-accent-neon` | #76FF03 | Éxito, disponible | 1.5:1 (usar con negro) |
| `--ts-accent-magenta` | #E040FB | Alertas especiales | 4.0:1 sobre blanco |

### Escala de Grises

| Token | Hex | Uso |
|-------|-----|-----|
| `--ts-gray-900` | #121212 | Fondos muy oscuros |
| `--ts-gray-800` | #1E1E1E | Sidebar hover |
| `--ts-gray-700` | #2D2D2D | Bordes en dark mode |
| `--ts-gray-600` | #424242 | Texto deshabilitado |
| `--ts-gray-500` | #616161 | Texto secundario |
| `--ts-gray-400` | #9E9E9E | Placeholders |
| `--ts-gray-300` | #BDBDBD | Iconos deshabilitados |
| `--ts-gray-200` | #E0E0E0 | Bordes sutiles |
| `--ts-gray-100` | #F0F0F0 | Fondos alternos |

### Estados Semánticos

| Token | Hex | Uso |
|-------|-----|-----|
| `--ts-success` | #00C853 | Confirmaciones, stock OK |
| `--ts-warning` | #FFAB00 | Advertencias, stock bajo |
| `--ts-error` | #FF1744 | Errores, cancelaciones |
| `--ts-info` | #00B0FF | Información neutral |

### Estados de Pedido

| Estado | Token | Color |
|--------|-------|-------|
| PENDIENTE | `--ts-estado-pendiente` | #FFD600 |
| CONFIRMADO | `--ts-estado-confirmado` | #00B0FF |
| EN_PREPARACION | `--ts-estado-preparacion` | #E040FB |
| LISTO | `--ts-estado-listo` | #00C853 |
| EN_CAMINO | `--ts-estado-camino` | #FF9100 |
| ENTREGADO | `--ts-estado-entregado` | #00E676 |
| CANCELADO | `--ts-estado-cancelado` | #FF1744 |

---

## Espaciado

### Sistema de 4px

| Token | Valor | Uso |
|-------|-------|-----|
| `--ts-space-1` | 4px | Micro espacios |
| `--ts-space-2` | 8px | Entre elementos inline |
| `--ts-space-3` | 12px | Padding botones pequeños |
| `--ts-space-4` | 16px | Padding estándar |
| `--ts-space-5` | 20px | Padding cards |
| `--ts-space-6` | 24px | Gap entre cards |
| `--ts-space-8` | 32px | Secciones |
| `--ts-space-10` | 40px | Padding grande |
| `--ts-space-12` | 48px | Separadores de sección |
| `--ts-space-16` | 64px | Hero sections |

---

## Componentes

### Botones

#### Clases disponibles

| Clase | Descripción |
|-------|-------------|
| `.btn-tusuper` | Botón principal (naranja) |
| `.btn-primary` | Botón cyan eléctrico |
| `.btn-secondary` | Botón gris neutro |
| `.btn-success` | Botón verde neón |
| `.btn-danger` | Botón rojo error |
| `.btn-warning` | Botón amarillo |

#### Especificaciones

```css
/* Botón base */
padding: 12px 20px;
font-size: 14px;
font-weight: 700;
text-transform: uppercase;
letter-spacing: 0.025em;
border: 2px solid #0A0A0A;
border-radius: 0;
box-shadow: 4px 4px 0 #0A0A0A;

/* Hover */
transform: translate(-2px, -2px);
box-shadow: 6px 6px 0 [color-acento];

/* Active */
transform: translate(2px, 2px);
box-shadow: 2px 2px 0 #0A0A0A;
```

### Cards

#### Clases disponibles

| Clase | Descripción |
|-------|-------------|
| `.card` | Card base brutalista |
| `.producto-card` | Card de producto con hover especial |
| `.ts-stat-card` | Card de estadísticas del dashboard |
| `.ts-content-card` | Card de contenido general |

#### Especificaciones

```css
/* Card base */
background: #FAFAFA;
border: 3px solid #0A0A0A;
border-radius: 0;
box-shadow: 6px 6px 0 #0A0A0A;

/* Hover */
transform: translate(-4px, -4px);
box-shadow: 10px 10px 0 #0A0A0A;
```

### Formularios

#### Input base

```css
border: 2px solid #0A0A0A;
border-radius: 0;
padding: 12px 16px;

/* Focus */
border-color: #FF3D00;
box-shadow: 4px 4px 0 #FF3D00;
outline: none;
```

#### Input group

```css
.input-group-text {
    background: #0A0A0A;
    color: #FAFAFA;
    border: 2px solid #0A0A0A;
}
```

### Tablas

```css
/* Header */
background: #0A0A0A;
color: #FAFAFA;
font-size: 12px;
font-weight: 700;
text-transform: uppercase;
letter-spacing: 0.15em;

/* Hover row */
background: #FFD600;
```

### Badges

```css
font-size: 12px;
font-weight: 700;
text-transform: uppercase;
letter-spacing: 0.025em;
padding: 6px 12px;
border-radius: 0;
border: 1px solid #0A0A0A;
```

### Alertas

```css
border: 3px solid #0A0A0A;
border-radius: 0;
padding: 16px 20px;
box-shadow: 4px 4px 0 #0A0A0A;
```

---

## Breakpoints y Responsive

### Puntos de Quiebre

| Token | Valor | Descripción |
|-------|-------|-------------|
| `--ts-bp-sm` | 576px | Mobile landscape |
| `--ts-bp-md` | 768px | Tablet portrait |
| `--ts-bp-lg` | 992px | Tablet landscape / Desktop |
| `--ts-bp-xl` | 1200px | Desktop grande |
| `--ts-bp-2xl` | 1400px | Desktop extra grande |

### Adaptaciones por Breakpoint

#### Mobile (< 768px)
- Sombras reducidas: `4px 4px` en lugar de `6px 6px`
- Sin transformaciones hover en cards
- Sidebar colapsable con borde inferior
- Grid de productos: 1 columna
- Tipografía reducida un nivel

#### Tablet (768px - 991px)
- Grid de productos: 2 columnas
- Sidebar visible o colapsable

#### Desktop (> 992px)
- Todas las interacciones activas
- Grid de productos: 3-4 columnas

---

## Accesibilidad

### Contraste de Colores (WCAG 2.1 AA)

| Combinación | Ratio | Estado |
|-------------|-------|--------|
| Negro sobre blanco | 21:1 | ✅ AAA |
| Negro sobre cream | 18:1 | ✅ AAA |
| Blanco sobre negro | 21:1 | ✅ AAA |
| Negro sobre amarillo | 12:1 | ✅ AAA |
| Negro sobre cyan | 10:1 | ✅ AAA |
| Negro sobre verde neón | 13:1 | ✅ AAA |
| Blanco sobre naranja | 4.5:1 | ✅ AA |
| Blanco sobre rojo error | 4.6:1 | ✅ AA |

### Tamaños Mínimos

| Elemento | Mínimo | Recomendado |
|----------|--------|-------------|
| Texto cuerpo | 16px | 16px |
| Texto secundario | 14px | 14px |
| Labels/badges | 12px | 12px |
| Target táctil | 44x44px | 48x48px |
| Área de clic botones | 44px altura | 48px altura |

### Focus Visible

```css
:focus-visible {
    outline: 3px solid #00E5FF;
    outline-offset: 2px;
}
```

### Reduced Motion

```css
@media (prefers-reduced-motion: reduce) {
    * {
        animation-duration: 0.01ms !important;
        transition-duration: 0.01ms !important;
    }
    
    .card:hover,
    .btn:hover {
        transform: none !important;
    }
}
```

### Checklist de Accesibilidad

- [ ] Todos los elementos interactivos tienen `:focus-visible`
- [ ] Contraste mínimo 4.5:1 para texto normal
- [ ] Contraste mínimo 3:1 para texto grande (>18px bold)
- [ ] Targets táctiles de al menos 44x44px
- [ ] Labels asociados a inputs con `for` attribute
- [ ] Imágenes con `alt` descriptivo
- [ ] Iconos decorativos con `aria-hidden="true"`
- [ ] Alertas con `role="alert"`
- [ ] Navegación con landmarks (`nav`, `main`, `aside`)

---

## Animaciones

### Transiciones Base

| Token | Duración | Easing | Uso |
|-------|----------|--------|-----|
| `--ts-transition-fast` | 150ms | ease | Hovers, focus |
| `--ts-transition-base` | 250ms | ease | Cambios de estado |
| `--ts-transition-slow` | 350ms | ease | Apariciones |
| `--ts-transition-spring` | 400ms | cubic-bezier(0.34, 1.56, 0.64, 1) | Bounces |

### Keyframes Disponibles

```css
/* Shake (para errores) */
@keyframes ts-shake { ... }

/* Pulse (para alertas) */
@keyframes ts-pulse { ... }

/* Slide up (para apariciones) */
@keyframes ts-slide-up { ... }
```

### Clases de Animación

| Clase | Efecto |
|-------|--------|
| `.ts-animate-shake` | Sacudida horizontal |
| `.ts-animate-pulse` | Pulso de opacidad |
| `.ts-animate-in` | Deslizamiento desde abajo |

---

## Checklist de Implementación

### Por Página

#### Login/Registro
- [x] Estructura `.ts-auth-page`
- [x] Card `.ts-auth-card`
- [x] Logo `.ts-auth-logo`
- [x] Formulario `.ts-auth-form`
- [x] Input groups con iconos
- [x] Botón submit `.ts-auth-submit`

#### Dashboard (Admin/Tendero)
- [ ] Aplicar clase `.ts-stat-card` a cards de estadísticas
- [ ] Headers de sección con `.ts-page-title`
- [ ] Tablas con estilos brutalistas
- [ ] Listas de items con `.ts-item-list`

#### Productos
- [ ] Header con `.ts-shop-header`
- [ ] Filtros con `.ts-category-filters`
- [ ] Grid con `.ts-products-grid`
- [ ] Cards con `.ts-product-card`

#### Carrito
- [ ] Título con `.ts-cart-title`
- [ ] Tabla con `.ts-cart-table`
- [ ] Items con `.ts-cart-item`
- [ ] Resumen con `.ts-order-summary`

---

## Estructura de Archivos CSS

```
static/css/
├── tokens.css          # Variables CSS (design tokens)
├── brutalist.css       # Estilos base y componentes
├── pages/
│   ├── auth.css        # Login y registro
│   ├── dashboard.css   # Paneles admin/tendero
│   └── shop.css        # Productos, carrito, checkout
└── DESIGN-SYSTEM.md    # Esta documentación
```

### Orden de Carga

```html
<!-- En layout.html -->
<link href="/css/tokens.css" rel="stylesheet">
<link href="/css/brutalist.css" rel="stylesheet">

<!-- En páginas específicas -->
<link href="/css/pages/auth.css" rel="stylesheet">
<!-- o -->
<link href="/css/pages/dashboard.css" rel="stylesheet">
<!-- o -->
<link href="/css/pages/shop.css" rel="stylesheet">
```

---

## Versionado

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0.0 | 2024-XX-XX | Release inicial del sistema brutalista-pop |

---

**Mantenido por:** Equipo Frontend TuSuper  
**Última actualización:** Diciembre 2024
