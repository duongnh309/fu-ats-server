package com.fu.fuatsbe.response;

import java.sql.Date;

import com.fu.fuatsbe.entity.CV;
import com.fu.fuatsbe.entity.Candidate;
import com.fu.fuatsbe.entity.Employee;
import com.fu.fuatsbe.entity.RecruitmentRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplyResponse {
    private int id;

    private Date date;
    private String expectSalary;
    private String status;
    
    private RecruitmentRequest recruitmentRequest;
    private Candidate candidate;
    private CV cv;
    private Employee applier;
}