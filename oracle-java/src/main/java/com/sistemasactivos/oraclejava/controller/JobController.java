package com.sistemasactivos.oraclejava.controller;

import com.sistemasactivos.oraclejava.model.Job;
import com.sistemasactivos.oraclejava.model.SaveResult;
import com.sistemasactivos.oraclejava.repository.JobDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobDAO jobDAO;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(jobDAO.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        return new ResponseEntity<>(jobDAO.getJobById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Job job) {
        SaveResult saveResult = jobDAO.save(job);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody Job job) {
        SaveResult saveResult = jobDAO.update(job);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        SaveResult saveResult = jobDAO.delete(id);
        return new ResponseEntity<>(saveResult.getMessage(), saveResult.getSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

}
