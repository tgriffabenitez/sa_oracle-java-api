package com.sistemasactivos.oraclejava.controller;

import com.sistemasactivos.oraclejava.model.Employee;
import com.sistemasactivos.oraclejava.model.SaveResult;
import com.sistemasactivos.oraclejava.repository.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeDAO employeeDAO;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(employeeDAO.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeDAO.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Employee employee) {
        SaveResult saveResult = employeeDAO.save(employee);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody Employee employee) {
        SaveResult saveResult = employeeDAO.update(employee);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        SaveResult saveResult = employeeDAO.delete(id);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}
