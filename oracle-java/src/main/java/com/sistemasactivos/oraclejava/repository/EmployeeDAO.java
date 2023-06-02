package com.sistemasactivos.oraclejava.repository;

import com.sistemasactivos.oraclejava.model.Employee;
import com.sistemasactivos.oraclejava.model.SaveResult;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> getAll();
    Employee getEmployeeById(Long id);
    SaveResult save(Employee employee);
    SaveResult update(Employee employee);
    SaveResult delete(Long id);
}
