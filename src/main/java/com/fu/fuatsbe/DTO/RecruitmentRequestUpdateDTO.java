package com.fu.fuatsbe.DTO;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentRequestUpdateDTO {
    private Date expiryDate;
    private String industry;
    private int amount;
    private String jobLevel;
    private String experience;
    private String salary;
    private String typeOfWork;
    private String description;
    private int positionId;
}