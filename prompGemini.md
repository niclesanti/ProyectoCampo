Necesito que configures los archivos del frontend (dashboard.html, styles.css, script.js) para registrar un nuevo espacio de trabajo, es decir, hay que configurar las siguientes cuestiones:
- Agregar la restriccion que para que en modal de "Nuevo Espacio de Trabajo", en el campo "Nombre del Espacio" el usuario solo pueda ingresar 50 caracteres. Una vez que llega a los 50 caracteres no puede ingresar mas.
- Agregar una restricción que no puede quedar vacío el campo "Nombre del Espacio". Si el usuario deja vacío este campo y presiona "Guardar Espacio", a lado de "Nombre del Espacio" debe aparecer un "*" rojo.
- Al cumplir con las restricciones y presionar "Guardar Espacio", debe generarse un JSON con:
{
  "nombre": "nombreDelEspacio",
  "idUsuarioAdmin": 1
}

Aun no está hecho, pero se supone que cuando un usuario se loguea, queda guardada su informacion (idUsuarioAdmin, nombre y email). No se donde queda guardada esta infomación.

Luego de generar el JSON se consume la API rest (POST) http://localhost:8080/espaciotrabajo/registrar