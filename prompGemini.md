# Graficos del dashboard

En base a este proyecto que es un servicio de gestion de finanzas y la necesidad de mostrar informacion en el dashboard, te comentaré mi problema.

Necesito que me ayudes a diseñar la logica para presentar los graficos en el dashboard. Lo que necesito es que me ayudes a tomar la decision de qué diseño es mas apropiado, en base a las buenas practicas de diseño y codificacion backend. A continuación describiremos los gráficos que debemos construir y que informacion necesita que le provea el backend. Gráficos:

1. Ingresos vs Gastos mensuales (grafico de barras): este grafico debe mostrar la comparacion entre ingresos y gastos de los ultimos 6 meses. Para esto, el backend debería buscar información sobre las transacciones hechas para ese espacio en los ultimos 6 meses. El frontend deberia recibir la informacion:

{
  "ingresos": [
    {"mes" : "mes1", "valor": "valor1"},
    {"mes" : "mes2", "valor": "valor2"},
    {"mes" : "mes3", "valor": "valor3"},
    {"mes" : "mes4", "valor": "valor4"},
    {"mes" : "mes5", "valor": "valor5"},
    {"mes" : "mes6", "valor": "valor6"}],
  "gastos": [
    {"mes" : "mes1", "valor": "valor1"},
    {"mes" : "mes2", "valor": "valor2"},
    {"mes" : "mes3", "valor": "valor3"},
    {"mes" : "mes4", "valor": "valor4"},
    {"mes" : "mes5", "valor": "valor5"},
    {"mes" : "mes6", "valor": "valor6"}]
}

Estos valores se obtienen de la sumatoria de transacciones agrupadas por cada mes (ultimos 6 meses), es decir, hay que procesar y generar toda esta informacion a partir de las entidades.

2. Distribucion de gastos (grafico de torta): este grafico muestra el porcentaje de gastos que fueron por cada "Motivo" de las transacciones. Los datos de este grafico se obtienen a partir de las mismas transacciones obtenidas para el grafico anterior (todas las transacciones de los ultimos 6 meses), pero en este caso solo se procesa los datos de las transacciones que son del tipo "GASTO". El frontend deberia recibir la informacion:

{
  "gastos": [
    {"motivo" : "motivo1", "porcentaje": "valor1"},
    {"motivo" : "motivo2", "porcentaje": "valor2"},
    {"motivo" : "motivo3", "porcentaje": "valor3"},
    {"motivo" : "motivoN", "porcentaje": "valorN"}]
}

Estos valores se obtienen de la sumatoria de transacciones agrupadas por cada mes (ultimos 6 meses), es decir, hay que procesar y generar toda esta informacion a partir de las entidades.

3. Tendencia del saldo acumulado (grafico de lineas): este grafico muestra la acumulacion de saldo a lo largo de los ultimos 6 meses. Los datos de este grafico se obtienen a partir de las mismas transacciones obtenidas para los graficos anteriores (todas las transacciones de los ultimos 6 meses). El frontend deberia recibir la informacion:

{
  "saldosAcumulados": [
    {"mes" : "mes1", "valor": "valor1"},
    {"mes" : "mes2", "valor": "valor2"},
    {"mes" : "mes3", "valor": "valor3"},
    {"mes" : "mes4", "valor": "valor4"},
    {"mes" : "mes5", "valor": "valor5"},
    {"mes" : "mes6", "valor": "valor6"}]
}

Estos valores se obtienen de la sumatoria de transacciones agrupadas por cada mes (ultimos 6 meses), es decir, hay que procesar y generar toda esta informacion a partir de las entidades.

En base a este contexto, necesito que me ayudes a tomar una decisión de diseño para el backend de como se deberia obtener estos datos y procesar esta informacion. Para ello, se me ocurren algunas opciones, la cuales necesito que las analices y me ayudes a decidir cual sería la mejor forma en terminos de costo computacional y la que mas sentido tiene en cuanto a las buenas practicas del software.

## Opcion 1: uso de caché en el backend

Se me ocurre que se puede hacer un metodo que obtenga todas las transacciones realizadas en los ultimos 6 meses de la base de datos. Guardar estos datos en una memoria cache (en el backend). Luego hacer tres metodos, uno por cada grafico, que va a solicitar las transacciones de los ultimos 6 meses (estimo que la busqueda en la base de datos ocurrira solo con el primer metodo que lo solicite, los otros dos restantes cuando soliciten estos datos los obtendrán de la memoria cache). Cada uno de estos tres metodos procesará la informacion y la guardará en listas de alguna estructura de datos (probablemente algun DTO) para retornarle la inforación a la API que la solicitó.

## Opcion 2: procesamiento de informacion en la base de datos

Hacer un metodo por cada grafico, que haga consultas en la base de datos (con el uso de diferentes funciones SQL) que obtenga como resultado la informacion procesada que será retornada a las API que la soliciten. No se que tan eficiente es esta opción, pero armar las consultas es complejo.

## Opcion 3: enviarle al frontend las transacciones

La ultima opcion es obtener las transacciones de los ultimos 6 meses de la base de datos y enviarle todos estos datos crudos al frontend, que se encargue de procesar la informacion en el script.js y de armar los graficos. (menos interacciones entre el front y el back, pero creo que le delegaria tareas al front que no se si es correcto que las haga).

## Consulta

Te he dado 3 opciones para que me ayudes a elegir el mejor diseño en base a el uso óptimo de recursos y a las buenas practicas del diseño del software. Si tienes una mejor opción podrías comentarmela. Justifica cual es la mejor opción.