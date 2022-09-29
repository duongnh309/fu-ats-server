package com.fu.fuatsbe.response;

import com.fu.fuatsbe.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private int id;
    private String name;
    private String employeeCode;
    private String phone;
    private String address;
    private String status;
    private Department department;
}
