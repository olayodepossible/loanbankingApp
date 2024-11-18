package com.possible.loanbanking.controller;

import com.possible.loanbanking.model.Customer;
import com.possible.loanbanking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Services")
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }


    @Operation(summary = "This method is used to create Admin User.")
    @PostMapping("/admin/register")
    public ResponseEntity<ResponseDto<UserInfo>> registerAdmin(@RequestBody @Valid UserDto admin)
    {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        ResponseDto<UserInfo> responseDto = userService.registerUser(admin, role);

        if (responseDto.getStatusCode() != 200) {
            return  ResponseEntity.badRequest().body(responseDto);
        }
        return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "This method is used to create Admin User.")
    @PostMapping("/customer/register")
    public ResponseEntity<ResponseDto<UserInfo>> registerCustomer(@RequestBody @Valid UserDto user)
    {
        Role role = new Role();
        role.setRoleName("ROLE_CUSTOMER");
        ResponseDto<UserInfo> responseDto = userService.registerUser(user, role);

        if (responseDto.getStatusCode() != 200) {
            return  ResponseEntity.badRequest().body(responseDto);
        }
        return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "User Login method.")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserInfo>> userLogin(@RequestBody LoginDto user)
    {
        ResponseDto<UserInfo> responseDto = userService.userLogin(user);

        if (responseDto.getStatusCode() != 200) {
            return  ResponseEntity.badRequest().body(responseDto);
        }
        return  new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(userService.userLogout(request, response), HttpStatus.OK);
    }
}

