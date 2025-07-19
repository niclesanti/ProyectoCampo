package com.campito.backend.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.campito.backend.dto.DistribucionGastoDTO;
import com.campito.backend.dto.IngresosGastosMesDTO;
import com.campito.backend.dto.SaldoAcumuladoMesDTO;
import com.campito.backend.model.Transaccion;

@Repository
public interface DashboardRepository extends JpaRepository<Transaccion, Long> {

    @Query(value = """
            SELECT
                TO_CHAR(t.fecha, 'YYYY-MM') as mes,
                SUM(CASE WHEN t.tipo = 'INGRESO' THEN t.monto ELSE 0 END) as ingresos,
                SUM(CASE WHEN t.tipo = 'GASTO' THEN t.monto ELSE 0 END) as gastos
            FROM transacciones t
            WHERE t.espacio_trabajo_id = :idEspacio
              AND t.fecha >= :fechaLimite
            GROUP BY mes
            ORDER BY mes DESC
            """, nativeQuery = true)
    List<IngresosGastosMesDTO> findIngresosVsGastos(@Param("idEspacio") Long idEspacio,
            @Param("fechaLimite") LocalDate fechaLimite);

    @Query(value = """
            SELECT
                mt.motivo,
                (SUM(t.monto) * 100.0 / (SELECT SUM(t2.monto) FROM transacciones t2 WHERE t2.espacio_trabajo_id = :idEspacio AND t2.tipo = 'GASTO' AND t2.fecha >= :fechaLimite)) AS porcentaje
            FROM transacciones t
            JOIN motivos_transaccion mt ON t.motivo_transaccion_id = mt.id
            WHERE t.espacio_trabajo_id = :idEspacio
              AND t.tipo = 'GASTO'
              AND t.fecha >= :fechaLimite
            GROUP BY mt.motivo
            ORDER BY SUM(t.monto) DESC
            """, nativeQuery = true)
    List<DistribucionGastoDTO> findDistribucionGastos(@Param("idEspacio") Long idEspacio,
            @Param("fechaLimite") LocalDate fechaLimite);

    @Query(value = """
            SELECT
                TO_CHAR(t.fecha, 'YYYY-MM') AS mes,
                SUM(CASE WHEN t.tipo = 'INGRESO' THEN t.monto ELSE -t.monto END) AS saldo_acumulado
            FROM transacciones t
            WHERE t.espacio_trabajo_id = :idEspacio
              AND t.fecha >= :fechaLimite
            GROUP BY mes
            ORDER BY mes DESC
            """, nativeQuery = true)
    List<SaldoAcumuladoMesDTO> findSaldosAcumulados(@Param("idEspacio") Long idEspacio,
            @Param("fechaLimite") LocalDate fechaLimite);
}