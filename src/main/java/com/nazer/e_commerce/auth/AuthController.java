package com.nazer.e_commerce.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nazer.e_commerce.auth.Dto.RigsterDto;
import com.nazer.e_commerce.users.schema.User;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping
    public User register(@RequestBody @Valid RigsterDto request){
        return this.authService.register(request);
    }

}
