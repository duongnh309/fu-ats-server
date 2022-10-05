package com.fu.fuatsbe.response;

import com.fu.fuatsbe.entity.CV;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponseDTO {

    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String status;
    private CV cv;
}