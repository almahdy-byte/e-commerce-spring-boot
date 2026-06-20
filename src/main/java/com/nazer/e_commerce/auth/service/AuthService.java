package com.nazer.e_commerce.auth.service;

import com.nazer.e_commerce.auth.Dto.LoginDto;
import com.nazer.e_commerce.auth.Dto.LoginResponseDto;
import com.nazer.e_commerce.auth.Dto.RigsterDto;
import com.nazer.e_commerce.users.schema.User;

public interface AuthService {
    User register(RigsterDto request);
    LoginResponseDto login(LoginDto request);
}
