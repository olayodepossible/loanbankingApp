package com.possible.loanbanking.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
public class CustomerReq {



        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @Email(message = "Invalid email format")
        private String email;

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        private String phoneNumber;
    }

