Cuando intento probar el endopoint "buscarTransaccion" con el JSON siguiente en Swagger:
{
  "idEspacioTrabajo": 1
}
Funciona correctamente segun lo esperado, es decir, busca todas las transacciones de la base de datos que pertenezcan a ese espacio de trabajo, porque el campo idEspacioTrabajo es obligatorio.
Pero cuando hago cualquier combinación de con los demas campos que no son obligatorios, es decir, busco con todo los campos o con alguno de ellos, se genera un error 500. A continuacion te muestro un JSON de ejemplo de entrada en swagger:
{
  "dia": 8,
  "mes": 7,
  "anio": 2025,
  "motivo": "Venta de rollos",
  "contacto": "Lucas Silva",
  "idEspacioTrabajo": 1
}
Ocurre el siguiente error 500:
{
  "message": "JDBC exception executing SQL [select t1_0.id,t1_0.contacto_transferencia_id,t1_0.descripcion,t1_0.espacio_trabajo_id,t1_0.fecha,t1_0.fecha_creacion,t1_0.monto,t1_0.motivo_transaccion_id,t1_0.nombre_completo_auditoria,t1_0.tipo from transacciones t1_0 join contactos_transferencia c1_0 on c1_0.id=t1_0.contacto_transferencia_id join motivos_transaccion m1_0 on m1_0.id=t1_0.motivo_transaccion_id where 1=1 and t1_0.espacio_trabajo_id=? and day(t1_0.fecha)=? and month(t1_0.fecha)=? and year(t1_0.fecha)=? and lower(m1_0.motivo) like ? escape '' and lower(c1_0.nombre) like ? escape ''] [ERROR: function day(date) does not exist\n  Hint: No function matches the given name and argument types. You might need to add explicit type casts.\n  Position: 411] [n/a]; SQL [n/a]",
  "path": "uri=/transaccion/buscar",
  "timestamp": "1752006537313",
  "status": 500
}
Cual puede ser el problema?