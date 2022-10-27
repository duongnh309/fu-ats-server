package com.fu.fuatsbe.response;

import java.util.Collection;

import com.fu.fuatsbe.entity.Candidate;
import com.fu.fuatsbe.entity.Position;
import com.fu.fuatsbe.entity.Skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CvResponse {

    private int id;
    private String linkCV;
    private String result;
    private String note;
    private String experience;
    private String Location;
    private String suitablePosition;
    private Collection<Skill> skills;

    private Position position;

    private Candidate candidate;

}