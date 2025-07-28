# Plan de pruebas (test unitarios)

Lista de items que corresponde cada uno a un test que se debe realizar para cada clase. Esta lista sirve como guía para verificar que se tiene en cuenta cada uno de los caminos posibles.

## Repository

Test unitarios para los componentes encargados de la persistencia de datos.

### ContactoTransferenciaRepositoryTest

- (test) buscar contactos por espacio de trabajo y que exista alguno
- (test) buscar contactos por espacio de trabajo y que no exista ninguno

### EspacioTrabajoRepositoryTest

- (test) Buscar espacios de trabajo donde participa un usuario y que exista alguno
- (test) Buscar espacios de trabajo donde participa un usuario y que no exista ninguno

### MotivoTransaccionRepositoryTest

- (test) buscar motivos por espacio de trabajo y que exista alguno
- (test) buscar motivos por espacio de trabajo y que no exista ninguno

### UsuarioRepositoryTest

- (test) Buscar usuario por email y proveedor y que exista
- (test) Buscar usuario por email y proveedor y que no exista

## Service

Test unitarios para los componentes encargados de la lógica de negocio.

### EspacioTrabajoServiceTest

- public void registrarEspacioTrabajo(EspacioTrabajoDTO espacioTrabajoDTO)
  - (test) espacioTrabajoDTO == null -> registro no exitoso
  - (test) espacioTrabajoDTO != null, no existe usuario con ese id -> registro no exitoso
  - (test) espacioTrabajoDTO != null, existe usuario con ese id -> registro exitoso

- public void compartirEspacioTrabajo(String email, Long idEspacioTrabajo, Long idUsuarioAdmin)
  - (test) email == null || idEspacioTrabajo == null || idUsuarioAdmin == null -> no es posible compartir espacio
  - (test) idEspacioTrabajo no existe -> no es posible compartir espacio
  - (test) idUsuarioAdmin no es el usuario administrador del espacio -> no es posible compartir espacio
  - (test) email no existe -> no es posible compartir espacio
  - (test) opcion correcta -> espacio compartido

### TransaccionServiceTest

- public TransaccionDTO registrarTransaccion(TransaccionDTO transaccionDTO)
  - (test) transaccionDTO == null -> registro no exitoso
  - (test) transaccionDTO.idEspacioTrabajo() == null -> registro no exitoso
  - (test) transaccionDTO.idMotivo() == null -> registro no exitoso
  - (test) espacio de trabajo no existe -> registro no eixtoso
  - (test) motivo no existe -> registro no eixtoso
  - (test) se proporciona un id de contacto pero este no existe -> registro no exitoso
  - (test) opcion correcta -> registro exitoso

- public void removerTransaccion(Long id)
  - (test) id == null -> no es posible remover transaccion
  - (test) transaccion no existe -> no es posible remover transaccion
  - (test) opcion correcta -> se remueve la transaccion y se actualiza el espacio de trabajo

- public List<TransaccionListadoDTO> buscarTransaccion(TransaccionBusquedaDTO datosBusqueda)
  - (test) datosBusqueda == null -> busqueda no exitosa
  - (test) datosBusqueda.idEspacioTrabajo() == null -> busqueda no exitosa
  - (test) datosBusqueda.anio() == null && datosBusqueda.mes() != null -> busqueda no exitosa
  - (test) busqueda con anio != null y mes == null -> busqueda exitosa (encuentra alguna transaccion)
  - (test) busqueda con anio != null y mes != null -> busqueda exitosa (encuentra alguna transaccion)
  - (test) busqueda con contacto != null -> busqueda exitosa (encuentra alguna transaccion)
  - (test) busqueda con motivo != null -> busqueda exitosa (encuentra alguna transaccion)
  - (test) busqueda sin filtros -> busqueda exitosa (encuentra alguna transaccion)
  - (test) busqueda sin resultados -> busqueda exitosa (no encuentra ninguna transaccion)

- public ContactoDTO registrarContactoTransferencia(ContactoDTO contactoDTO)
  - (test) contactoDTO == null -> registro no exitoso
  - (test) contactoDTO.nombre() == null || contactoDTO.nombre().isEmpty() -> registro no exitoso
  - (test) contactoDTO.idEspacioTrabajo() == null -> registro no exitoso
  - (test) espacio de trabajo no existe -> registro no exitoso
  - (test) opcion correcta -> registro exitoso

- public MotivoDTO nuevoMotivoTransaccion(MotivoDTO motivoDTO)
  - (test) motivoDTO == null -> registro no exitoso
  - (test) motivoDTO.motivo() == null || motivoDTO.motivo().isEmpty() -> registro no exitoso
  - (test) motivoDTO.idEspacioTrabajo() == null -> registro no exitoso
  - (test) espacio de trabajo no existe -> registro no exitoso
  - (test) opcion correcta -> registro exitoso

- public List<ContactoListadoDTO> listarContactos(Long idEspacioTrabajo)
  - (test) idEspacioTrabajo == null -> no es posible listar contactos
  - (test) no existen contactos para ese espacio de trabajo -> es posible listar contactos
  - (test) existen contactos para ese espacio de trabajo -> es posible listar contactos

- public List<MotivoListadoDTO> listarMotivos(Long idEspacioTrabajo)
  - (test) idEspacioTrabajo == null -> no es posible listar contactos
  - (test) no existen motivos para ese espacio de trabajo -> es posible listar motivos
  - (test) existen motivos para ese espacio de trabajo -> es posible listar motivos

- public List<TransaccionListadoDTO> buscarTransaccionesRecientes(Long idEspacioTrabajo)
  - (test) idEspacioTrabajo == null -> no es posible buscar transacciones recientes
  - (test) no existen transacciones recientes para ese espacio de trabajo -> es posible buscar transacciones recientes
  - (test) opcion correcta (se obtienen las ultimas 6 transacciones) -> es posible buscar transacciones recientes

## Controller

Test unitarios que prueban las APIs del sistema.

### EspacioTrabajoController
- public ResponseEntity<Void> registrarEspacioTrabajo(@Valid @RequestBody EspacioTrabajoDTO espacioTrabajoDTO)
  - (test) status 500 -> registro no exitoso
  - (test) status 400 -> registro no exitoso
  - (test) status 201 -> registro exitoso

- public ResponseEntity<Void> compartirEspacioTrabajo(
            @PathVariable String email,
            @PathVariable Long idEspacioTrabajo,
            @PathVariable Long idUsuarioAdmin)
  - (test) status 400 -> no es posible compartir espacio
  - (test) status 200 -> espacio compartido

- public ResponseEntity<List<EspacioTrabajoListadoDTO>> listarEspaciosTrabajoPorUsuario(@PathVariable Long idUsuario)
  - (test) status 500 -> no es posible listar espacios de trabajo
  - (test) status 400 -> no es posible listar espacios de trabajo
  - (test) status 200 -> es posible listar espacios de trabajo

### TransaccionController

- public ResponseEntity<TransaccionDTO> registrarTransaccion(@Valid @RequestBody TransaccionDTO transaccionDTO)
  - (test) status 500 -> registro no exitoso
  - (test) status 400 -> registro no exitoso
  - (test) status 201 -> registro exitoso

- public ResponseEntity<Void> removerTransaccion(@PathVariable Long id)
  - (test) status 500 -> no es posible remover transaccion
  - (test) status 404 -> no es posible remov er transaccion
  - (test) status 200 -> se remueve la transaccion

- public ResponseEntity<List<TransaccionListadoDTO>> buscarTransaccion(@Valid @RequestBody TransaccionBusquedaDTO datosBusqueda)
  - (test) status 500 -> busqueda no exitosa
  - (test) status 400 -> busqueda no exitosa
  - (test) status 200 -> busqueda exitosa

- public ResponseEntity<ContactoDTO> registrarContactoTransferencia(@Valid @RequestBody ContactoDTO contactoDTO)
  - (test) status 500 -> registro no exitoso
  - (test) status 400 -> registro no exitoso
  - (test) status 201 -> registro exitoso

- public ResponseEntity<List<ContactoListadoDTO>> listarContactos(@PathVariable Long idEspacioTrabajo)
  - (test) status 500 -> no es posible listar contactos
  - (test) status 404 -> no es posible listar contactos
  - (test) status 200 -> es posible listar contactos

- public ResponseEntity<MotivoDTO> nuevoMotivoTransaccion(@Valid @RequestBody MotivoDTO motivoDTO)
  - (test) status 500 -> registro no exitoso
  - (test) status 400 -> registro no exitoso
  - (test) status 201 -> registro exitoso

- public ResponseEntity<List<MotivoListadoDTO>> listarMotivos(@PathVariable Long idEspacioTrabajo)
  - (test) status 500 -> no es posible listar motivos
  - (test) status 404 -> no es posible listar motivos
  - (test) status 200 -> es posible listar motivos

- public ResponseEntity<List<TransaccionListadoDTO>> buscarTransaccionesRecientes(@PathVariable Long idEspacio)
  - (test) status 500 -> no es posible buscar transacciones recientes
  - (test) status 404 -> no es posible buscar transacciones recientes
  - (test) status 200 -> es posible buscar transacciones recientes