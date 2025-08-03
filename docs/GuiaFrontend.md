## Guía de Buenas Prácticas para el Frontend de tu Aplicación Web (HTML, CSS, JS)

### HTML: La Estructura de Tu Contenido

  * **Semántica es clave**: Utiliza las etiquetas HTML de forma semántica. Esto significa que cada etiqueta debe describir el propósito de su contenido, no solo cómo se ve.
      * Usa `<header>`, `<nav>`, `<main>`, `<article>`, `<section>`, `<aside>`, `<footer>` para estructurar tu página.
      * Usa `<h1>` a `<h6>` para encabezados, `<p>` para párrafos, `<ul>`/`<ol>` para listas, etc.
      * Evita el uso excesivo de `<div>` y `<span>` cuando existan etiquetas más específicas.
  * **Indentración consistente**: Mantén una indentración uniforme para mejorar la legibilidad. Usa 2 o 4 espacios, pero sé consistente.
  * **Comentarios claros**: Agrega comentarios HTML para secciones complejas o para explicar decisiones importantes en la estructura.
  * **Metaetiquetas esenciales**: Incluye metaetiquetas para la codificación de caracteres (`<meta charset="UTF-8">`), la vista móvil (`<meta name="viewport" content="width=device-width, initial-scale=1.0">`) y una descripción básica (`<meta name="description" content="...">`).
  * **Enlace de CSS al `<head>`**: Coloca el enlace a tus archivos CSS dentro de la sección `<head>`.
  * **Enlace de JS al final del `<body>`**: Coloca el enlace a tus archivos JavaScript justo antes de la etiqueta de cierre `</body>`. Esto asegura que el HTML se cargue primero y mejora el rendimiento percibido.

### CSS: El Estilo de Tu Aplicación

  * **Organización**:
      * **Archivo principal**: Mantén un `style.css` para estilos globales.
      * **Separación de responsabilidades**: Si tu proyecto crece, considera separar tus estilos por componentes o secciones, importándolos en tu `style.css` principal.
      * **Comentarios**: Usa comentarios para agrupar secciones de estilos (ej. `/* Global Styles */`, `/* Navigation */`, `/* Buttons */`).
  * **Nomenclatura (BEM es una buena opción)**:
      * **BEM (Block, Element, Modifier)** es una metodología popular para nombrar clases CSS que fomenta la modularidad y la reusabilidad.
          * **Bloque (`.button`)**: Una entidad independiente.
          * **Elemento (`.button__icon`)**: Una parte de un bloque que no tiene significado por sí misma.
          * **Modificador (`.button--primary`)**: Una bandera que cambia la apariencia o el comportamiento de un bloque o elemento.
      * Esto te ayuda a evitar conflictos de nombres y hace que sea más fácil entender qué hace cada clase.
  * **Especificidad controlada**: Trata de mantener la especificidad de tus selectores lo más baja posible para evitar sobrescrituras complejas. Prefiere usar clases sobre IDs para estilos, ya que los IDs tienen una especificidad muy alta.
  * **Variables CSS (Custom Properties)**: Utiliza variables CSS para valores que se repiten, como colores, tamaños de fuente o espaciados. Esto facilita los cambios globales y mantiene la consistencia.
    ```css
    :root {
      --primary-color: #007bff;
      --font-size-base: 16px;
    }

    body {
      color: var(--primary-color);
      font-size: var(--font-size-base);
    }
    ```
  * **Unidades relativas**: Prefiere unidades relativas como `em`, `rem`, `%`, `vw`, `vh` sobre píxeles fijos para un diseño más adaptable y responsivo.
  * **No usar `!important` a menos que sea absolutamente necesario**: `!important` rompe la cascada de CSS y dificulta la depuración. Es mejor ajustar la especificidad de tus selectores.

### JavaScript: La Lógica y la Interactividad

  * **Modularización**:
      * Divide tu código JavaScript en funciones más pequeñas y reutilizables.
      * Si el proyecto lo requiere, agrupa funciones relacionadas en archivos separados (ej. `utils.js` para funciones de ayuda, `dom-elements.js` para manipulación del DOM).
  * **Nomenclatura clara y descriptiva**:
      * Usa nombres de variables y funciones que describan claramente su propósito (ej. `calculateTotalPrice`, `updateShoppingCartDisplay`).
      * Prefiere `camelCase` para variables y funciones.
      * Usa `UPPER_SNAKE_CASE` para constantes.
  * **Comentarios**: Explica la lógica compleja o las intenciones detrás de ciertas decisiones.
  * **Manejo de eventos**:
      * Adjunta **escuchadores de eventos (event listeners)** usando `addEventListener()`. Evita el uso de atributos `on*` en el HTML (ej. `onclick="myFunction()"`) ya que mezclan la estructura con el comportamiento.
      * Considera la **delegación de eventos** para elementos generados dinámicamente o para optimizar el rendimiento en listas grandes.
  * **Manipulación del DOM**:
      * Minimiza el acceso directo al DOM. Accede a los elementos una vez y guárdalos en variables si los vas a usar múltiples veces.
      * Cuando necesites actualizar muchos elementos del DOM, considera construir el HTML en JavaScript y luego insertarlo de una vez para mejorar el rendimiento.
  * **Evitar variables globales**: Limita el uso de variables globales para prevenir conflictos y hacer tu código más predecible. Encapsula tu lógica en funciones o módulos.
  * **Programación asíncrona (cuando sea necesario)**: Si necesitas interactuar con APIs o cargar recursos, usa `async/await` o `Promises` para manejar operaciones asíncronas de forma limpia.
  * **Validación de entradas**: Si tu aplicación toma entradas del usuario, asegúrate de validarlas tanto en el frontend como en el backend para prevenir errores y vulnerabilidades.

### Consideraciones Adicionales para Mantenibilidad

  * **Control de versiones (Git)**: ¡Absolutamente esencial\! Utiliza Git para rastrear los cambios en tu código, colaborar con otros y revertir a versiones anteriores si es necesario.
  * **Formateadores de código (Prettier, ESLint)**: Aunque para este proyecto no los necesitas, a medida que tu código crezca, estas herramientas automatizan el formato y la detección de errores, manteniendo una base de código consistente entre desarrolladores.
  * **Pruebas (futuro)**: A medida que tu aplicación crece, las pruebas unitarias y de integración son fundamentales para asegurar que los cambios no rompan funcionalidades existentes.

-----

## Prompt para otra IA para Organizar tu Código

Aquí tienes un prompt detallado que puedes usar con otra IA para que te ayude a organizar tu código siguiendo estas pautas:

```
Actúa como un experto en desarrollo frontend con un enfoque en la mantenibilidad y la limpieza del código. Recibirás código HTML, CSS y JavaScript para una aplicación web básica. Tu tarea es refactorizar y organizar este código siguiendo las mejores prácticas de la industria para mejorar su estructura, legibilidad y mantenibilidad.

Aplica las siguientes pautas estrictas:

**1. Estructura de Archivos y Carpetas:**
    - Organiza el código en una estructura de carpetas lógica: `css/`, `js/`, `img/`.
    - El archivo HTML principal debe ser `index.html` en la raíz.
    - Los estilos generales irán en `css/style.css`.
    - La lógica JavaScript principal irá en `js/main.js`.

**2. HTML (index.html):**
    - Asegura el uso de **etiquetas HTML semánticas** (`<header>`, `<nav>`, `<main>`, `<section>`, `<article>`, `<aside>`, `<footer>`, `<h1>-<h6>`, `<p>`, `<ul>`, `<ol>`, etc.) para describir el contenido, no solo la apariencia. Evita el uso excesivo de `<div>` y `<span>` cuando existan alternativas semánticas.
    - Mantén una **indentración consistente** (2 o 4 espacios).
    - Agrega **comentarios claros** para explicar secciones complejas de la estructura o decisiones de diseño importantes.
    - Incluye las **metaetiquetas esenciales**: `charset="UTF-8"` y `viewport`.
    - Enlaza los archivos CSS en la sección `<head>`.
    - Enlaza los archivos JavaScript justo antes de la etiqueta de cierre `</body>`.

**3. CSS (css/style.css):**
    - Implementa una **nomenclatura de clases basada en BEM (Block__Element--Modifier)** para todas las clases CSS.
    - Agrupa los estilos con **comentarios claros** (ej. `/* Estilos Globales */`, `/* Navegación */`, `/* Botones */`).
    - Utiliza **variables CSS (Custom Properties)** para valores que se repiten (colores, tamaños de fuente, espaciados). Define estas variables en `:root`.
    - Prioriza el uso de **unidades relativas** (`em`, `rem`, `%`, `vw`, `vh`) sobre píxeles fijos cuando sea posible para mejorar la responsividad.
    - Evita el uso de `!important` a menos que sea estrictamente necesario.
    - Asegura una **indentración consistente** (2 o 4 espacios).

**4. JavaScript (js/main.js):**
    - **Modulariza** el código en funciones pequeñas, enfocadas y reutilizables.
    - Utiliza una **nomenclatura de variables y funciones clara y descriptiva** (`camelCase` para variables y funciones, `UPPER_SNAKE_CASE` para constantes).
    - Agrega **comentarios** para explicar la lógica compleja o las decisiones importantes.
    - Adjunta **escuchadores de eventos (event listeners)** utilizando `addEventListener()`. Elimina cualquier `on*` atributo en el HTML.
    - Minimiza el acceso directo al DOM. Accede a los elementos una vez y almacénalos en variables si van a ser usados múltiples veces.
    - Evita el uso de variables globales. Encapsula la lógica dentro de funciones o módulos.

**Formato de Salida:**
Presenta el código refactorizado y organizado de la siguiente manera:
1. Una descripción general de los cambios realizados y por qué.
2. La estructura final de archivos y carpetas propuesta.
3. El contenido del archivo `index.html`.
4. El contenido del archivo `css/style.css`.
5. El contenido del archivo `js/main.js`.
```

-----

Al usar este prompt con otra IA, asegúrate de proporcionarle tu código HTML, CSS y JavaScript actual para que pueda aplicarle estas pautas. ¡Esto te ayudará a tener una base de código mucho más robusta y fácil de mantener\!

¿Hay alguna parte de estas pautas que te gustaría que te explicara con más detalle o que adaptara para tu proyecto específico?