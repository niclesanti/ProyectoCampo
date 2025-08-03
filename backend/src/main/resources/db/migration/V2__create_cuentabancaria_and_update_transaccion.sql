-- Crear la nueva tabla para la entidad CuentaBancaria
CREATE TABLE public.cuentas_bancarias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    entidad_financiera VARCHAR(50) NOT NULL,
    saldo_actual REAL NOT NULL,
    id_espacio_trabajo BIGINT NOT NULL,
    CONSTRAINT fk_cuentabancaria_espaciotrabajo
        FOREIGN KEY (id_espacio_trabajo)
        REFERENCES public.espacios_trabajo(id)
);

-- Modificar la tabla transacciones para agregar la relación con CuentaBancaria
ALTER TABLE public.transacciones
ADD COLUMN cuenta_bancaria_id BIGINT,
ADD CONSTRAINT fk_transaccion_cuentabancaria
    FOREIGN KEY (cuenta_bancaria_id)
    REFERENCES public.cuentas_bancarias(id);
