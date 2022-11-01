package com.fu.fuatsbe.serviceImp;

import com.fu.fuatsbe.DTO.InterviewCreateDTO;
import com.fu.fuatsbe.DTO.InterviewUpdateDTO;
import com.fu.fuatsbe.DTO.SendNotificationDTO;
import com.fu.fuatsbe.constant.candidate.CandidateErrorMessage;
import com.fu.fuatsbe.constant.employee.EmployeeErrorMessage;
import com.fu.fuatsbe.constant.interview.InterviewErrorMessage;
import com.fu.fuatsbe.constant.interview.InterviewRequestStatus;
import com.fu.fuatsbe.constant.job_apply.JobApplyErrorMessage;
import com.fu.fuatsbe.entity.*;
import com.fu.fuatsbe.exceptions.ListEmptyException;
import com.fu.fuatsbe.exceptions.NotFoundException;
import com.fu.fuatsbe.exceptions.PermissionException;
import com.fu.fuatsbe.repository.*;
import com.fu.fuatsbe.response.InterviewResponse;
import com.fu.fuatsbe.response.ResponseWithTotalPage;
import com.fu.fuatsbe.service.InterviewService;
import com.fu.fuatsbe.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImp implements InterviewService {
    private final InterviewRepository interviewRepository;

    private final CandidateRepository candidateRepository;

    private final EmployeeRepository employeeRepository;
    private final JobApplyRepository jobApplyRepository;

    private final NotificationService notificationService;

    private final InterviewEmployeeRepository interviewEmployeeRepository;

    @Override
    public InterviewResponse createInterview(InterviewCreateDTO interviewCreateDTO) throws MessagingException {
        Candidate candidate = candidateRepository.findById(interviewCreateDTO.getCandidateId())
                .orElseThrow(() -> new NotFoundException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));

        List<Employee> employeeList = new ArrayList<>();
        List<Integer> intervieweeIdList = new ArrayList<>();

        for (Integer employeeId : interviewCreateDTO.getEmployeeId()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new NotFoundException(EmployeeErrorMessage.EMPLOYEE_NOT_FOUND_EXCEPTION));
            employeeList.add(employee);
            intervieweeIdList.add(employeeId);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDate localDate = LocalDate.parse(interviewCreateDTO.getDate(), df);
        LocalTime localTime = LocalTime.parse(interviewCreateDTO.getTime(), tf);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        String dateInput = localDateTime.format(dtf);
        Timestamp dateInterview = Timestamp.valueOf(dateInput);

        LocalDate presentDate = LocalDate.parse(LocalDate.now().toString(), df);

        if (localDate.isBefore(presentDate)) {
            throw new PermissionException(InterviewErrorMessage.DATE_NOT_VALID);
        }
        JobApply jobApply = jobApplyRepository.findById(interviewCreateDTO.getJobApplyId())
                .orElseThrow(() -> new NotFoundException(JobApplyErrorMessage.JOB_APPLY_NOT_FOUND));
        Interview interview = Interview.builder()
                .subject(interviewCreateDTO.getSubject())
                .purpose(interviewCreateDTO.getPurpose())
                .date(dateInterview)
                .address(interviewCreateDTO.getAddress())
                .room(interviewCreateDTO.getRoom())
                .linkMeeting(interviewCreateDTO.getLinkMeeting())
                .round(interviewCreateDTO.getRound())
                .description(interviewCreateDTO.getDescription())
                .status(InterviewRequestStatus.PENDING)
                .type(interviewCreateDTO.getType())
                .candidate(candidate)
                .jobApply(jobApply)
                .build();
        Interview savedInterview = interviewRepository.save(interview);

        List<InterviewEmployee> listInterviewEmployees = new ArrayList<>();

        for (Employee emp : employeeList) {
            InterviewEmployee interviewEmployee = InterviewEmployee.builder()
                    .employee(emp)
                    .interview(savedInterview)
                    .build();
            InterviewEmployee interviewEmployeeSaved = interviewEmployeeRepository.save(interviewEmployee);
            listInterviewEmployees.add(interviewEmployeeSaved);
        }

        SendNotificationDTO sendNotificationDTO = SendNotificationDTO.builder()
                .link(savedInterview.getLinkMeeting())
                .room(savedInterview.getRoom())
                .address(savedInterview.getAddress())
                .date(savedInterview.getDate())
                .candidate(candidate)
                .IntervieweeID(intervieweeIdList)
                .interview(savedInterview)
                .build();
        notificationService.sendNotificationForInterview(sendNotificationDTO);

        InterviewResponse response = InterviewResponse.builder()
                .id(savedInterview.getId())
                .subject(savedInterview.getSubject())
                .purpose(savedInterview.getPurpose())
                .date(Date.valueOf(localDate))
                .time(savedInterview.getDate().toLocalDateTime().toLocalTime())
                .room(savedInterview.getRoom())
                .address(savedInterview.getAddress())
                .linkMeeting(savedInterview.getLinkMeeting())
                .round(savedInterview.getRound())
                .description(savedInterview.getDescription())
                .status(interview.getStatus())
                .type(savedInterview.getType())
                .jobApply(savedInterview.getJobApply())
                .candidateName(savedInterview.getCandidate().getName())
                .build();

        List<String> empName = new ArrayList<>();
        for (InterviewEmployee interviewEmp : listInterviewEmployees) {
            empName.add(interviewEmp.getEmployee().getName());
        }
        response.setEmployeeNames(empName);
        return response;
    }

    @Override
    public List<InterviewResponse> getInterviewByCandidateID(int candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        List<Interview> interviews = interviewRepository.findInterviewByCandidateId(candidate.getId());

        List<InterviewResponse> responseList = new ArrayList<>();
        for (Interview interview : interviews) {
            InterviewResponse response = InterviewResponse.builder()
                    .id(interview.getId())
                    .subject(interview.getSubject())
                    .purpose(interview.getPurpose())
                    .date(Date.valueOf(interview.getDate().toLocalDateTime().toLocalDate()))
                    .time(interview.getDate().toLocalDateTime().toLocalTime())
                    .room(interview.getRoom())
                    .address(interview.getAddress())
                    .linkMeeting(interview.getLinkMeeting())
                    .round(interview.getRound())
                    .description(interview.getDescription())
                    .status(interview.getStatus())
                    .type(interview.getType())
                    .jobApply(interview.getJobApply())
                    .candidateName(interview.getCandidate().getName())
                    .build();
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public List<InterviewResponse> getInterviewByEmployeeID(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(EmployeeErrorMessage.EMPLOYEE_NOT_FOUND_EXCEPTION));
        List<Interview> interviewList = interviewRepository.findInterviewByEmployeeId(employee.getId());

        List<String> empName = new ArrayList<>();

        List<InterviewResponse> responseList = new ArrayList<>();
        for (Interview interview : interviewList) {
            if (empName.size() != 0) {
                empName.clear();
            }
            for (InterviewEmployee interviewEmp : interview.getInterviewEmployees()) {
                empName.add(interviewEmp.getEmployee().getName());
            }
            InterviewResponse response = InterviewResponse.builder()
                    .id(interview.getId())
                    .subject(interview.getSubject())
                    .purpose(interview.getPurpose())
                    .date(Date.valueOf(interview.getDate().toLocalDateTime().toLocalDate()))
                    .time(interview.getDate().toLocalDateTime().toLocalTime())
                    .room(interview.getRoom())
                    .address(interview.getAddress())
                    .linkMeeting(interview.getLinkMeeting())
                    .round(interview.getRound())
                    .description(interview.getDescription())
                    .status(interview.getStatus())
                    .type(interview.getType())
                    .jobApply(interview.getJobApply())
                    .candidateName(interview.getCandidate().getName())
                    .employeeNames(empName)
                    .build();
            responseList.add(response);

        }

        return responseList;

    }

    @Override
    public ResponseWithTotalPage getAllInterview(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Interview> interviews = interviewRepository.findAll(pageable);
        List<Object> result = new ArrayList<>();
        ResponseWithTotalPage response = null;
        List<String> empName = new ArrayList<>();
        if (interviews.hasContent()) {
            for (Interview interview : interviews.getContent()) {
                if (empName.size() != 0) {
                    empName.clear();
                }
                for (InterviewEmployee interviewEmp : interview.getInterviewEmployees()) {
                    empName.add(interviewEmp.getEmployee().getName());
                }
                InterviewResponse interviewResponse = InterviewResponse.builder()
                        .id(interview.getId())
                        .subject(interview.getSubject())
                        .purpose(interview.getPurpose())
                        .date(Date.valueOf(interview.getDate().toLocalDateTime().toLocalDate()))
                        .time(interview.getDate().toLocalDateTime().toLocalTime())
                        .room(interview.getRoom())
                        .address(interview.getAddress())
                        .linkMeeting(interview.getLinkMeeting())
                        .round(interview.getRound())
                        .description(interview.getDescription())
                        .status(interview.getStatus())
                        .type(interview.getType())
                        .jobApply(interview.getJobApply())
                        .candidateName(interview.getCandidate().getName())
                        .employeeNames(empName)
                        .build();

                result.add(interviewResponse);
                response = ResponseWithTotalPage.builder()
                        .totalPage(interviews.getTotalPages())
                        .responseList(result)
                        .build();
            }
        } else
            throw new ListEmptyException(InterviewErrorMessage.LIST_EMPTY_EXCEPTION);

        return response;
    }

    @Override
    public InterviewResponse getInterviewByID(int id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InterviewErrorMessage.INTERVIEW_NOT_FOUND));
        List<String> empName = new ArrayList<>();
        for (InterviewEmployee interviewEmp : interview.getInterviewEmployees()) {
            empName.add(interviewEmp.getEmployee().getName());
        }
        InterviewResponse interviewResponse = InterviewResponse.builder()
                .id(interview.getId())
                .subject(interview.getSubject())
                .purpose(interview.getPurpose())
                .date(Date.valueOf(interview.getDate().toLocalDateTime().toLocalDate()))
                .time(interview.getDate().toLocalDateTime().toLocalTime())
                .room(interview.getRoom())
                .address(interview.getAddress())
                .linkMeeting(interview.getLinkMeeting())
                .round(interview.getRound())
                .description(interview.getDescription())
                .status(interview.getStatus())
                .type(interview.getType())
                .jobApply(interview.getJobApply())
                .candidateName(interview.getCandidate().getName())
                .employeeNames(empName)
                .build();
        return interviewResponse;
    }

    @Override
    public InterviewResponse updateInterview(int id, InterviewUpdateDTO interviewUpdateDTO) throws MessagingException {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InterviewErrorMessage.INTERVIEW_NOT_FOUND));
        Candidate candidate = candidateRepository.findById(interviewUpdateDTO.getCandidateId())
                .orElseThrow(() -> new NotFoundException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        List<Employee> employeeList = new ArrayList<>();
        for (Integer employeeId : interviewUpdateDTO.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new NotFoundException(EmployeeErrorMessage.EMPLOYEE_NOT_FOUND_EXCEPTION));
            employeeList.add(employee);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDate localDate = LocalDate.parse(interviewUpdateDTO.getDate(), df);
        LocalTime localTime = LocalTime.parse(interviewUpdateDTO.getTime(), tf);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        String dateInput = localDateTime.format(dtf);
        Timestamp dateInterview = Timestamp.valueOf(dateInput);

        JobApply jobApply = jobApplyRepository.findById(interviewUpdateDTO.getJobApplyId())
                .orElseThrow(() -> new NotFoundException(JobApplyErrorMessage.JOB_APPLY_NOT_FOUND));
        interview.setSubject(interviewUpdateDTO.getSubject());
        interview.setPurpose(interviewUpdateDTO.getPurpose());
        interview.setDate(dateInterview);
        interview.setAddress(interviewUpdateDTO.getAddress());
        interview.setRoom(interviewUpdateDTO.getRoom());
        interview.setLinkMeeting(interviewUpdateDTO.getLinkMeeting());
        interview.setRound(interviewUpdateDTO.getRound());
        interview.setType(interviewUpdateDTO.getType());
        interview.setDescription(interviewUpdateDTO.getDescription());
        interview.setJobApply(jobApply);
        interview.setCandidate(candidate);

        List<InterviewEmployee> interviewEmployeeList = new ArrayList<>();
        for (InterviewEmployee interviewEmployee : interview.getInterviewEmployees()) {
            interviewEmployeeList.add(interviewEmployee);
        }
        interview.setInterviewEmployees(interviewEmployeeList);
        Interview savedInterview = interviewRepository.save(interview);
        interviewEmployeeRepository.deleteInterviewEmployeeByInterviewId(savedInterview.getId());

        for (Employee emp : employeeList) {
            InterviewEmployee interviewEmployee = InterviewEmployee.builder()
                    .employee(emp)
                    .interview(savedInterview)
                    .build();
            interviewEmployeeRepository.save(interviewEmployee);
        }

        SendNotificationDTO sendNotificationDTO = SendNotificationDTO.builder()
                .link(savedInterview.getLinkMeeting())
                .room(savedInterview.getRoom())
                .address(savedInterview.getAddress())
                .date(savedInterview.getDate())
                .candidate(candidate)
                .IntervieweeID(interviewUpdateDTO.getEmployeeIds())
                .interview(savedInterview)
                .build();
        notificationService.sendNotificationForInterview(sendNotificationDTO);

        List<String> empName = new ArrayList<>();
        for (InterviewEmployee interEmp : savedInterview.getInterviewEmployees()) {
            empName.add(interEmp.getEmployee().getName());
        }
        InterviewResponse interviewResponse = InterviewResponse.builder()
                .id(savedInterview.getId())
                .subject(savedInterview.getSubject())
                .purpose(savedInterview.getPurpose())
                .date(Date.valueOf(savedInterview.getDate().toLocalDateTime().toLocalDate()))
                .time(savedInterview.getDate().toLocalDateTime().toLocalTime())
                .address(savedInterview.getAddress())
                .room(savedInterview.getRoom())
                .linkMeeting(savedInterview.getLinkMeeting())
                .round(savedInterview.getRound())
                .description(savedInterview.getDescription())
                .status(savedInterview.getStatus())
                .type(savedInterview.getType())
                .jobApply(savedInterview.getJobApply())
                .candidateName(savedInterview.getCandidate().getName())
                .employeeNames(empName)
                .build();
        return interviewResponse;
    }

    @Override
    public void closeInterview(int id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InterviewErrorMessage.INTERVIEW_NOT_FOUND));
        interview.setStatus(InterviewRequestStatus.DONE);
        interviewRepository.save(interview);
    }

    @Override
    public void cancelInterview(int id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(InterviewErrorMessage.INTERVIEW_NOT_FOUND));
        interview.setStatus(InterviewRequestStatus.CANCELED);
        interviewRepository.save(interview);
    }
}
