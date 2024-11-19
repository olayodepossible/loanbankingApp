package com.possible.loanbanking.service;

import com.possible.loanbanking.dto.req.*;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface UserService {

    ResponseDto<UserInfo> registerUser(UserDto userDto, Role role);
    ResponseDto<UserInfo> userLogin(LoginDto loginDto);
    ResponseDto userLogout(HttpServletRequest request, HttpServletResponse response);
    ResponseDto nameEnquiry(String accountNumber);
    ResponseDto<AppUser> getAllUsers();
    ResponseDto<String> deleteUserById(Long userId);
}
