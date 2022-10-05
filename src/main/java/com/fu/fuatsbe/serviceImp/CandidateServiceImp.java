package com.fu.fuatsbe.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.fuatsbe.DTO.CandidateUpdateDTO;
import com.fu.fuatsbe.constant.account.AccountErrorMessage;
import com.fu.fuatsbe.constant.account.AccountStatus;
import com.fu.fuatsbe.constant.candidate.CandidateErrorMessage;
import com.fu.fuatsbe.constant.candidate.CandidateStatus;
import com.fu.fuatsbe.entity.Candidate;
import com.fu.fuatsbe.repository.CandidateRepository;
import com.fu.fuatsbe.response.CandidateResponseDTO;
import com.fu.fuatsbe.service.CandidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImp implements CandidateService {

    private final ModelMapper modelMapper;
    private final CandidateRepository candidateRepository;

    @Override
    public List<CandidateResponseDTO> getAllCandidates() {
        List<Candidate> list = candidateRepository.findAll();
        List<CandidateResponseDTO> result = new ArrayList<CandidateResponseDTO>();
        if (list.size() > 0) {
            for (Candidate candidate : list) {
                CandidateResponseDTO candidateResponseDTO = modelMapper.map(candidate, CandidateResponseDTO.class);
                result.add(candidateResponseDTO);
            }
        }
        return result;
    }

    @Override
    public CandidateResponseDTO getCandidateById(int id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        CandidateResponseDTO candidateResponseDTO = modelMapper.map(candidate, CandidateResponseDTO.class);
        return candidateResponseDTO;

    }

    @Override
    public CandidateResponseDTO getCandidateByEmail(String email) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        CandidateResponseDTO candidateResponseDTO = modelMapper.map(candidate, CandidateResponseDTO.class);
        return candidateResponseDTO;

    }

    @Override
    public CandidateResponseDTO getCandidateByPhone(String phone) {
        Candidate candidate = candidateRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalStateException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        CandidateResponseDTO candidateResponseDTO = modelMapper.map(candidate, CandidateResponseDTO.class);
        return candidateResponseDTO;
    }

    @Override
    public CandidateResponseDTO updateCandidateById(CandidateUpdateDTO updateDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Candidate deleteCandidateById(int id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(CandidateErrorMessage.CANDIDATE_NOT_FOUND_EXCEPTION));
        if (candidate.getAccount() != null) {
            if (candidate.getAccount().getStatus().equals(CandidateStatus.DISABLED)) {
                throw new IllegalStateException(AccountErrorMessage.ACCOUNT_ALREADY_DELETED);
            }
            if (candidate.getStatus().equals(CandidateStatus.SUSPENDED)) {
                throw new IllegalStateException(CandidateErrorMessage.CANDIDATE_ALREADY_SUSPENDED);
            }
            candidate.getAccount().setStatus(AccountStatus.DISABLED);
            Candidate candidateSaved = candidateRepository.save(candidate);
            return candidateSaved;
        }
        return null;
    }

}