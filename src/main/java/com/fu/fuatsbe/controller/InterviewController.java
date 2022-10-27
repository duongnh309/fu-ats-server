package com.fu.fuatsbe.controller;

import com.fu.fuatsbe.DTO.InterviewCreateDTO;
import com.fu.fuatsbe.constant.interview.InterviewSuccessMessage;
import com.fu.fuatsbe.constant.response.ResponseStatusDTO;
import com.fu.fuatsbe.constant.role.RolePreAuthorize;
import com.fu.fuatsbe.response.InterviewResponse;
import com.fu.fuatsbe.response.ResponseDTO;
import com.fu.fuatsbe.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/interview")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

//    @GetMapping("/getInterviewNoNotification")
//    @PreAuthorize(RolePreAuthorize.ROLE_EMPLOYEE)
//    public ResponseEntity<ResponseDTO> getInterviewNoNotification() {
//        ResponseDTO response = new ResponseDTO();
//
//        return ResponseEntity.ok().body(response);
//    }

    @PostMapping("/createInterview")
    @PreAuthorize(RolePreAuthorize.ROLE_EMPLOYEE)
    public ResponseEntity<ResponseDTO> createInterview(@RequestBody InterviewCreateDTO dto) throws MessagingException {
        ResponseDTO response = new ResponseDTO();
        InterviewResponse interviewResponse = interviewService.createInterview(dto);
        response.setStatus(ResponseStatusDTO.SUCCESS);
        response.setData(interviewResponse);
        response.setMessage(InterviewSuccessMessage.CREATE_INTERVIEW_SUCCESS);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/getInterviewByCandidateID")
    @PreAuthorize(RolePreAuthorize.IS_AUTHENTICATED)
    public ResponseEntity<ResponseDTO> getInterviewByCandidateID(@RequestParam int candidateId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<InterviewResponse> interviewResponses = interviewService.getInterviewByCandidateID(candidateId);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setData(interviewResponses);
        responseDTO.setMessage(InterviewSuccessMessage.GET_INTERVIEW_BY_CANDIDATE_ID);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getInterviewByEmployeeID")
    @PreAuthorize(RolePreAuthorize.IS_AUTHENTICATED)
    public ResponseEntity<ResponseDTO> getInterviewByEmployeeID(@RequestParam int employeeId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<InterviewResponse> interviewResponses = interviewService.getInterviewByEmployeeID(employeeId);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setData(interviewResponses);
        responseDTO.setMessage(InterviewSuccessMessage.GET_INTERVIEW_BY_CANDIDATE_ID);
        return ResponseEntity.ok().body(responseDTO);
    }
}