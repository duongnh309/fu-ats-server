package com.fu.fuatsbe.DTO;

import com.fu.fuatsbe.constant.validation_message.ValidationMessage;
import com.fu.fuatsbe.constant.validation_size.ValidationSize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterDto {
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX, message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String password;
    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE)
    private String email;
    @NotBlank(message = ValidationMessage.ROLE_VALID_MESSAGE)
    private String role;

    @NotBlank(message = ValidationMessage.NAME_VALID_MESSAGE)
    private String name;
    @NotBlank(message = ValidationMessage.EMPLOYEE_CODE_VALID_MESSAGE)
    private String EmployeeCode;
    @NotBlank(message = ValidationMessage.PHONE_NOT_EMPTY_VALID_MESSAGE)
    @Size(min = ValidationSize.PHONE_MIN, max = ValidationSize.PHONE_MAX, message = ValidationMessage.PHONE_SIZE_VALID_MESSAGE)
    private String phone;

    private String image;

    @NotBlank(message = ValidationMessage.ADDRESS_VALID_MESSAGE)
    private String address;
    @NotBlank(message = ValidationMessage.DEPARTMENT_VALID_MESSAGE)
    private String departmentName;
    @NotBlank(message = ValidationMessage.POSITION_VALID_MESSAGE)
    private String positionName;
}
