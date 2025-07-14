Necesito que generes el archivo login.html en ProyectoCampo\backend\src\main\resources\static y los estilos y funcionalidades en styles.css y script.js para que un usuario pueda ingresar mail y clave. Debe haber un boton "Iniciar sesion" y otro "Iniciar sesion con Google" (este ultimo boton por ahora no hace nada). Seguir los colores y estilos del dashboard. Al presionar "Iniciar sesion" se debe generar el siguiente JSON:
{
  "email": "string",
  "clave": "string"
}
y consumir la siguiente API: http://localhost:8080/usuario/login

configurar SecurityConfig para que funcione la autenticacion de usuario y acceder al dashboard una vez autenticado. Puedes hacerlo?