package com.possible.loanbanking.controller;

import com.possible.loanbanking.model.AppUser;
import com.possible.loanbanking.dto.req.LoginDto;
import com.possible.loanbanking.model.Role;
import com.possible.loanbanking.dto.req.UserDto;
import com.possible.loanbanking.dto.req.UserInfo;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Tag(name = "User Services")
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @Operation(summary = "This method is used to create Admin User.")
    @PostMapping("/admin/register")
    public ResponseEntity<ResponseDto<UserInfo>> registerAdmin(@RequestBody @Valid UserDto admin)
    {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ResponseDto<UserInfo> responseDto = userService.registerUser(admin, roles);

        if (responseDto.getStatusCode() != 200) {
            return  ResponseEntity.badRequest().body(responseDto);
        }
        return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "This method is used to create Users.")
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserInfo>> registerCustomer(@RequestBody @Valid UserDto user)
    {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ResponseDto<UserInfo> responseDto = userService.registerUser(user, roles);

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

    @GetMapping("/getAllUser")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<AppUser>> getAllUser(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<String>> deleteUserById(@PathVariable Long userId){
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);

    }


    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(userService.userLogout(request, response), HttpStatus.OK);
    }
}

