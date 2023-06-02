package com.sistemasactivos.oraclejava.repository;

import com.sistemasactivos.oraclejava.model.Job;
import com.sistemasactivos.oraclejava.model.SaveResult;

import java.util.List;

public interface JobDAO {
    List<Job> getAll();
    Job getJobById(String id);
    SaveResult save(Job job);
    SaveResult update(Job job);
    SaveResult delete(String id);
}
