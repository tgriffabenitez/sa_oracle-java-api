package com.sistemasactivos.oraclejava.service;

import com.sistemasactivos.oraclejava.model.Employee;
import com.sistemasactivos.oraclejava.model.SaveResult;
import com.sistemasactivos.oraclejava.repository.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Employee> getAll() {
        String sql = "SELECT * FROM EMPLOYEES WHERE BAJA IS NULL";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }

    @Override
    public Employee getEmployeeById(Long id) {
        String sql = "SELECT * FROM EMPLOYEES WHERE EMPLOYEE_ID = ? AND BAJA IS NULL";
        List<Employee> employees = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Employee.class));
        return employees.size() > 0 ? employees.get(0) : null;
    }

    @Override
    public SaveResult save(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        String email = employee.getEmail();
        String phoneNumber = employee.getPhoneNumber();
        Date hireDate = employee.getHireDate();
        String jobId = employee.getJobId();
        Long salary = employee.getSalary();
        Long commissionPct = employee.getCommissionPct();
        Long managerId = employee.getManagerId();
        Long departmentId = employee.getDepartmentId();

        try {
            // Preparar la llamada a la función PL/SQL
            String sql = "{ ? = call hr.pa_gestion_hr.agregar_empleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            // Configurar los parámetros de entrada
            cstmt.setString(2, firstName);
            cstmt.setString(3, lastName);
            cstmt.setString(4, email);
            cstmt.setString(5, phoneNumber);
            cstmt.setDate(6, new java.sql.Date(hireDate.getTime()));
            cstmt.setString(7, jobId);
            cstmt.setDouble(8, salary);
            cstmt.setDouble(9, commissionPct);
            cstmt.setInt(10, managerId.intValue());
            cstmt.setInt(11, departmentId.intValue());

            // Configuro los parámetros de salida
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(12, Types.VARCHAR);

            // Ejecutar la función
            cstmt.executeUpdate();

            // Obtengo los resultados
            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(12);

            // Manejar los resultados
            cstmt.close();
            if (success == 1) {
                return new SaveResult(true, "Usuario creado exitosamente.");
            } else {
                return new SaveResult(false, "Error: " + errorMsg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public SaveResult update(Employee employee) {
        try {
            // Preparar la llamada a la función PL/SQL
            String sql = "{ ? = call hr.pa_gestion_hr.modificar_empleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            // Configurar los parámetros de entrada
            cstmt.setLong(2, employee.getEmployeeId());
            cstmt.setString(3, employee.getFirstName());
            cstmt.setString(4, employee.getLastName());
            cstmt.setString(5, employee.getEmail());
            cstmt.setString(6, employee.getPhoneNumber());
            cstmt.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));
            cstmt.setString(8, employee.getJobId());
            cstmt.setLong(9, employee.getSalary());
            cstmt.setLong(10, employee.getCommissionPct());
            cstmt.setLong(11, employee.getManagerId());
            cstmt.setLong(12, employee.getDepartmentId());

            // Configurar los parámetros de salida
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(13, Types.VARCHAR);

            // Ejecutar la función
            cstmt.executeUpdate();

            // Obtener los resultados
            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(13);

            // Manejar los resultados
            cstmt.close();
            if (success == 1) {
                return new SaveResult(true, "Empleado modificado exitosamente.");
            } else {
                return new SaveResult(false, "Error al modificar empleado: " + errorMsg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public SaveResult delete(Long id) {
        try {
            // Preparar la llamada a la función PL/SQL
            String sql = "{ ? = call hr.pa_gestion_hr.eliminar_empleado(?, ?) }";
            CallableStatement cstmt = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().prepareCall(sql);

            // Configurar los parámetros de entrada
            cstmt.setLong(2, id);

            // Configurar los parámetros de salida
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            // Ejecutar la función
            cstmt.execute();

            // Obtener los resultados
            int success = cstmt.getInt(1);
            String errorMsg = cstmt.getString(3);

            // Manejar los resultados
            cstmt.close();
            if (success == 1) {
                return new SaveResult(true, "El empleado ha sido eliminado exitosamente.");
            } else {
                return new SaveResult(false, "Error al eliminar el empleado: " + errorMsg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new SaveResult(false, "Error: " + e.getMessage());
        }
    }

}
