package com.possible.loanbanking.service;

import com.possible.loanbanking.dto.req.*;
import com.possible.loanbanking.dto.response.ResponseDto;
import com.possible.loanbanking.model.AppUser;
import com.possible.loanbanking.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


public interface UserService {

    ResponseDto<UserInfo> registerUser(UserDto userDto, Set<Role> roles);
    ResponseDto<UserInfo> userLogin(LoginDto loginDto);
    ResponseDto<Object> userLogout(HttpServletRequest request, HttpServletResponse response);
    ResponseDto<Object> nameEnquiry(String accountNumber);
    ResponseDto<AppUser> getAllUsers();
    ResponseDto<String> deleteUserById(Long userId);
}
