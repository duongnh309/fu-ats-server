package com.fu.fuatsbe.response;

import java.sql.Date;

import com.fu.fuatsbe.entity.Employee;
import com.fu.fuatsbe.entity.Position;
import com.fu.fuatsbe.entity.RecruitmentPlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDetailResponseDTO {

    private int id;
    private int amount;
    private Date date;
    private String skills;
    private String status;
    private RecruitmentPlan recruitmentPlan;
    private Employee approver;
    private Position position;

}