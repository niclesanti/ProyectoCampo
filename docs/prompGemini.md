Ahora necesito que modifiquemos cosas del frontend. Necesito que impplementemos la gestion de Cuentas Bancarias en el frontend, para ello, lo primero que hay que hacer es modificar el modal de "Nueva Transaccion". Necesito que agregues un nueva lista desplegable que permita seleccionar una cuenta bancaria. Este nueva entrada que puede elegir el usuario debe estar por debajo de "Motivo", debe que tener un label "Cuenta bancaria (opcional)", una lista desplegable que tenga por defecto "Seleccionar cuenta" y que se poble esta lista en el momento que el usuario elige un espacio de trabajo (como las demas listas del modal) con la API /listar/{idEspacioTrabajo} y mantener en algun lugar los datos obtenidos para recargar la lista sin tener que acceder a la base de datos todo el tiempo.
Además al lado de la lista desplegable de cuentas debe haber un boton "Nueva" que debe desplegar los campos para cargar en la base de datos una nueva cuenta (de la misma manera que lo hace para motivo y para contacto). Los campos que debe desplegar el boton "Nueva" deben ser un campo simple de texto "Nombre cuenta" y una lista desplegable de entidades financieras. Esta lista debes llenarla con las siguientes entidades:

- BNA
- Banco Santander
- BBVA
- Galicia
- HSBC
- ICBC
- Banco Macro
- Banco Patagonia
- Banco Credicoop
- Banco de Santa Fe
- Mercado Pago
- Ualá
- Brubank
- Naranja X
- Lemon Cash
- Personal Pay

(ordenar alfabeticamente para facilitar la busqueda del usuario).
La lista de entidades financieras podria estar guardadas en algun arreglo de String en el front.
De igual manera agregar los botones "Guardar" y "Cancelar" que ocultaran nuevamente estos campos.
El boton guardar lo configuramos luego.
Muy importante seguir el mismo estilo y diseño que en todo el dashboard.