### Análisis del Problema

El desafío principal es cómo presentar dos flujos de trabajo distintos (transacción normal vs. transferencia entre cuentas propias) dentro del mismo modal sin abrumar al usuario con demasiados campos a la vez. El objetivo es que la interfaz sea clara, intuitiva y minimice la posibilidad de errores.

---

### Solución 1: Pestañas (Tabs)

Esta es una opción muy común y efectiva para separar contenido relacionado pero distinto.

**Diseño Propuesto:**

1.  **En la parte superior del modal**, justo debajo del título "Nueva transacción", se colocarían dos pestañas.
2.  La primera pestaña se llamaría **"Gastos/Ingresos"**.
3.  La segunda pestaña se llamaría **"Movimiento entre cuentas"**.
4.  Por defecto, la pestaña de "Gastos/Ingresos" estaría seleccionada, mostrando los campos actuales de tu modal.
5.  Al hacer clic en la pestaña "Movimiento entre cuentas", los campos de "Gastos/Ingresos" se ocultarían y se mostrarían los nuevos campos:
    * Cuenta de origen (lista desplegable de las cuentas bancarias)
    * Cuenta de destino (lista desplegable de las cuentas bancarias)
    * Monto ($)

### Guía para la IA (con HTML/CSS/JS)

Puedes proporcionarle a la IA las siguientes instrucciones para la implementación de la solución de pestañas:

1.  **Estructura HTML:**
    * El modal `div` contendrá un `div` para las pestañas.
    * Dentro de ese `div`, habrá un `div` con la clase `.tabs` que contendrá dos botones o anclas, uno para "Gasto / Ingreso" y otro para "Transferencia".
    * Habrá dos `div`s con la clase `.tab-content`, uno para cada tipo de formulario.
    * El `div` de "Gasto / Ingreso" tendrá todos los campos actuales.
    * El `div` de "Transferencia" tendrá los nuevos campos (cuenta origen, cuenta destino, monto).

2.  **Estilos CSS:**
    * Estiliza las pestañas para que parezcan botones que se pueden seleccionar.

3.  **Lógica JavaScript:**
    * Al cargar el modal, la pestaña de "Gastos/Ingresos" debe tener la clase `.active` y su contenido debe ser visible.
    * Añade un evento `click` a cada pestaña.
    * Cuando se haga clic en una pestaña, la función debe:
        * Remover la clase `.active` de la pestaña y el contenido actualmente visibles.
        * Añadir la clase `.active` a la pestaña que se acaba de cliquear y a su contenido correspondiente.

Con esta guía, la IA podrá generar un código frontend que no solo sea funcional, sino también intuitivo y estéticamente agradable.

- El contenido de la pestaña "Gastos/Ingresos" debe funcionar tal como funciona actualmente.
- Al registrar una nueva transaccion del tipo "movimiento entre cuentas", al guardar la transaccion se debe consumir la API /cuentabancaria/transaccion/{idCuentaOrigen}/{idCuentaDestino}/{monto}
- El modal debe funcionar tal como funciona actualmente, solo se debe agregar el comportamiento de las pestañas en el modal "Nueva transaccion".
- Se debe notificar al usuario cuando alguna transaccion es correcta o no lo es, tal como funciona actualmente.