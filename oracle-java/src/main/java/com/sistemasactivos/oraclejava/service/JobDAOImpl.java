package com.sistemasactivos.oraclejava.service;

import com.sistemasactivos.oraclejava.model.Employee;
import com.sistemasactivos.oraclejava.model.Job;
import com.sistemasactivos.oraclejava.model.SaveResult;
import com.sistemasactivos.oraclejava.repository.JobDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

@Service
public class JobDAOImpl implements JobDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Job> getAll() {
        String sql = "SELECT * FROM hr.JOBS WHERE BAJA IS NULL";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Job.class));
    }

    @Override
    public Job getJobById(String id) {
        String sql = "SELECT * FROM hr.JOBS WHERE JOB_ID = ? AND BAJA IS NULL";
        List<Job> jobs = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Job.class));
        return jobs.size() > 0 ? jobs.get(0) : null;
    }

    @Override
    public SaveResult save(Job job) {
        String jobId = job.getJobId();
        String jobTitle = job.getJobTitle();
        Long minSalary = job.getMinSalary();
        Long maxSalary = job.getMaxSalary();

        try {
            String sql = "{ ? = call hr.pa_gestion_hr.agregar_puesto(?, ?, ?, ?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            cstmt.setString(2, jobId);
            cstmt.setString(3, jobTitle);
            cstmt.setLong(4, minSalary);
            cstmt.setLong(5, maxSalary);

            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.execute();

            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(6);

            cstmt.close();

            if (success == 1) {
                return new SaveResult(true, "Job creado exitosamente.");
            } else {
                return new SaveResult(false, "Error: " + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public SaveResult update(Job job) {
        try {
            // Preparar la llamada a la función PL/SQL
            String sql = "{ ? = call hr.pa_gestion_hr.modificar_puesto(?, ?, ?, ?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            // Configurar los parámetros de entrada
            cstmt.setString(2, job.getJobId());
            cstmt.setString(3, job.getJobTitle());
            cstmt.setLong(4, job.getMinSalary());
            cstmt.setLong(5, job.getMaxSalary());

            // Configurar los parámetros de salida
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            // Ejecutar la función
            cstmt.execute();

            // Obtener los resultados
            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(6);

            // Cerrar la conexión
            cstmt.close();

            if (success == 1) {
                return new SaveResult(true, "Job modificado exitosamente.");
            } else {
                return new SaveResult(false, "Error: " + errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public SaveResult delete(String id) {
        try {
            // Preparar la llamada a la función PL/SQL
            String sql = "{ ? = call hr.pa_gestion_hr.eliminar_puesto(?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            // Configurar los parámetros de entrada
            cstmt.setString(2, id);

            // Configurar los parámetros de salida
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            // Ejecutar la función
            cstmt.execute();

            // Obtener los resultados
            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(3);

            // Cerrar la conexión
            cstmt.close();

            if (success == 1) {
                return new SaveResult(true, "Job eliminado exitosamente.");
            } else {
                return new SaveResult(false, "Error: " + errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }
}
