package com.nazer.e_commerce.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nazer.e_commerce.auth.Dto.RigsterDto;
import com.nazer.e_commerce.auth.Dto.LoginResponseDto;
import com.nazer.e_commerce.auth.Dto.LoginDto;
import com.nazer.e_commerce.users.schema.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody @Valid RigsterDto request){
        return this.authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto request){
        return this.authService.login(request);
    }

    

}
