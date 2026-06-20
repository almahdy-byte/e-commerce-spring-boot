package com.nazer.e_commerce.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nazer.e_commerce.auth.Dto.*;
import com.nazer.e_commerce.common.security.SecurityService;
import com.nazer.e_commerce.users.repository.UserRepository;
import com.nazer.e_commerce.users.schema.User;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private SecurityService securityService;
    public AuthServiceImpl(UserRepository userRepository , SecurityService securityService){
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Override
    public  User register(RigsterDto request){
        
        boolean existingUser = this.userRepository.existsByEmail(request.getEmail());
        if(existingUser){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                    "Email already exist"
            );
        }

        User createUser = User.builder()
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .fullName(request.getFirstName() + " " + request.getLastName())
                            .password(this.securityService.hash(request.getPassword()))
                            .role(request.getRole())
                            .phoneNumber(this.securityService.encrypt(request.getPhoneNumber()))
                            .build();

        User saveUser = this.userRepository.save(createUser);
        return saveUser;

    }

    @Override
    public LoginResponseDto login(LoginDto request){

        
        User user = this.userRepository.findByEmail(request.getEmail())
            .orElseThrow(()->{
                return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,                
                "invalid credentials");
            });

        if(!this.securityService.compareHashed(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "invalid credentails"
            );
        }

        LoginResponseDto respone = new LoginResponseDto("a7a" , "a7a2");
        return respone;
    }
    
}
