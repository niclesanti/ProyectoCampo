## Rediseño de la tarjeta "Saldo actual" del dashboard

Actualmente en el dashboard hay una tarjeta que indica el "Saldo actual" del espacio de trabajo seleccionado por el usuario.

Ahora necesito que trabajemos sobre esto. Quiero que en lugar de que haya una sola tarjeta saldo actual que ocupe todo el ancho del dashboard, ahora estará dividido en 4 pequeñas tarjetas que tienen el mismo diseño que la tarjeta saldo actual, pero que muestran información distinta.

La información que se debe mostrar en las tarjetas es la siguiente:

1. Ingresos del mes
2. Gastos del mes
3. Saldo neto del mes
4. Saldo historico

Lo importante es que no es necesario buscar esta información en la base de datos, porque se puede obtener del estado actual del dashboard, por ejemplo, de los datos que usan los graficos, para así reducir el trafico de la red.

1. Ingresos del mes: la informacion de los ingresos del mes actual se pueden obtener del grafico "Ingresos vs Gastos mensuales". Se obtiene el valor del ingreso para el ultimo mes y el nombre del mes. Debe mostrarse en rojo siguiendo los colores del dashboard.
2. Gastos del mes: la informacion de los gastos del mes actual se pueden obtener del grafico "Ingresos vs Gastos mensuales". Se obtiene el valor del gasto para el ultimo mes y el nombre del mes. Debe mostrarse en rojo siguiendo los colores del dashboard.
3. Saldo neto del mes: esta informacion se obtiene de la misma forma que las dos anteriores, pero se hace la resta entre el ingreso y el gasto del ultimo mes. En este caso debe mostrarse de color verde o rojo siguiendo los colores del dashboard, dependiendo si es positivo o negativo el valor.
4. Saldo historico: esta tarjeta es la actual de "Saldo actual", es decir, muestra la misma información, solo cambia el titulo.

Implementar estas tarjetas en el orden descripto en el dashboard, de izquierda a derecha para pantallas grandes y de arriba hacia abajo para pantallas pequeñas.