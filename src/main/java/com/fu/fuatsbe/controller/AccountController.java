package com.fu.fuatsbe.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fu.fuatsbe.constant.account.AccountSuccessMessage;
import com.fu.fuatsbe.constant.response.ResponseStatusDTO;
import com.fu.fuatsbe.constant.role.RolePreAuthorize;
import com.fu.fuatsbe.response.AccountResponse;
import com.fu.fuatsbe.response.ListResponseDTO;
import com.fu.fuatsbe.response.ResponseDTO;
import com.fu.fuatsbe.service.AccountService;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/getAllAccounts")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ListResponseDTO> getAllAccounts() {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO();
        List<AccountResponse> list = accountService.getAllAccounts();
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACCOUNT_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getActivateAccounts")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ListResponseDTO> getActivateAccounts() {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO();
        List<AccountResponse> list = accountService.getActivateAccounts();
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACTIVE_ACCOUNT_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getDisableAccounts")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ListResponseDTO> getDisableAccounts() {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO();
        List<AccountResponse> list = accountService.getDisableAccounts();
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_DISABLE_ACCOUNT_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAllAccountsByRole")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ListResponseDTO> getAllAccountsByRole(@RequestParam("roleId") int roleId) {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO();
        List<AccountResponse> list = accountService.getDisableAccounts();
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACCOUNT_BY_ROLE_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAccountByEmail/{email}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> getAccountByEmail(@RequestParam("email") String email) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO();
        AccountResponse account = accountService.getAccountByEmail(email);
        responseDTO.setData(account);
        responseDTO.setMessage(AccountSuccessMessage.GET_ACCOUNT_BY_EMAIL_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAccountById/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> getAccountById(@RequestParam("id") int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO();
        AccountResponse account = accountService.getAccountById(id);
        responseDTO.setData(account);
        responseDTO.setMessage(AccountSuccessMessage.GET_ACCOUNT_BY_ID_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> deletePosition(@RequestParam("id") int id) {
        ResponseDTO<Boolean> responseDTO = new ResponseDTO();
        boolean isDelete = accountService.deleteAccountById(id);
        responseDTO.setData(isDelete);
        responseDTO.setMessage(AccountSuccessMessage.DELETE_ACCOUNT_SUCCESS);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
