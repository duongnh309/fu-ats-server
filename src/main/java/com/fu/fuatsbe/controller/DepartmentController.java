package com.fu.fuatsbe.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.fuatsbe.DTO.DepartmentCreateDTO;
import com.fu.fuatsbe.DTO.DepartmentUpdateDTO;
import com.fu.fuatsbe.constant.department.DepartmentSuccessMessage;
import com.fu.fuatsbe.constant.role.RolePreAuthorize;
import com.fu.fuatsbe.entity.Department;
import com.fu.fuatsbe.response.DepartmentResponse;
import com.fu.fuatsbe.response.ListResponseDTO;
import com.fu.fuatsbe.response.ResponseDTO;
import com.fu.fuatsbe.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/department/getAll")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ListResponseDTO> getAllDepartments() {
        ListResponseDTO<DepartmentResponse> response = new ListResponseDTO();
        List<DepartmentResponse> list = departmentService.getAllDepartments();
        response.setData(list);
        response.setSuccessMessage(DepartmentSuccessMessage.GET_ALL_DEPARTMENT);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/department/getById")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ResponseDTO> getDepartmentById(@RequestParam("id") int id) {
        ResponseDTO<DepartmentResponse> responseDTO = new ResponseDTO();
        DepartmentResponse departmentResponse = departmentService.getDepartmentById(id);
        responseDTO.setData(departmentResponse);
        responseDTO.setSuccessMessage(DepartmentSuccessMessage.GET_DEPARTMENT_BY_ID);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/department/getByName")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ListResponseDTO> getDepartmentByName(@RequestParam("name") String name) {
        ListResponseDTO<DepartmentResponse> response = new ListResponseDTO();
        List<DepartmentResponse> list = departmentService.getDepartmentByName(name);
        response.setData(list);
        response.setSuccessMessage(DepartmentSuccessMessage.GET_DEPARTMENT_BY_NAME);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/department/edit/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> updateDepartment(@RequestParam("id") int id,
            @RequestBody DepartmentUpdateDTO updateDTO) {
        ResponseDTO<DepartmentResponse> responseDTO = new ResponseDTO();
        DepartmentResponse department = departmentService.updateDepartment(id, updateDTO);
        responseDTO.setData(department);
        responseDTO.setSuccessMessage("Update department successfully");
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/department/create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> createDepartment(@RequestBody DepartmentCreateDTO createDTO) {
        ResponseDTO<Department> responseDTO = new ResponseDTO();
        Department department = departmentService.createDepartment(createDTO);
        responseDTO.setData(department);
        responseDTO.setSuccessMessage("Create department successfully");
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/department/delete")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity deleteDepartmnetById(@RequestParam("id") int id) {
        ResponseDTO<Department> responseDTO = new ResponseDTO();
        departmentService.deleteDepartmentById(id);
        responseDTO.setSuccessMessage("delete department successfully");
        return ResponseEntity.ok().body(responseDTO);
    }

}
