package com.sistemasactivos.oraclejava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    private String jobId;
    private String jobTitle;
    private Long minSalary;
    private Long maxSalary;
    private String errorMsg;
    private Date baja;

    @Override
    public String toString() {
        return  "jobId='" + jobId + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary;
    }
}
