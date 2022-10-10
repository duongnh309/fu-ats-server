package com.fu.fuatsbe.response;

import com.fu.fuatsbe.entity.Candidate;
import com.fu.fuatsbe.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private int accountId;

    private String token;
    private String roleName;
    private String email;
    private String status;
    private Employee employee;
    private Candidate candidate;
}
