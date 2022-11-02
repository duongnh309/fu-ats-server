package com.fu.fuatsbe.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplyCreateDTO {

    private String province;
    private String educationLevel;
    private String foreignLanguage;
    private int recruitmentRequestId;
    private int cvId;
    private int candidateId;
}
