package com.nazer.e_commerce.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nazer.e_commerce.auth.Dto.*;
import com.nazer.e_commerce.auth.service.AuthService;
import com.nazer.e_commerce.common.email.EmailService;
import com.nazer.e_commerce.common.security.SecurityService;
import com.nazer.e_commerce.common.token.TokenPayload;
import com.nazer.e_commerce.common.token.TokenService;
import com.nazer.e_commerce.users.repository.UserRepository;
import com.nazer.e_commerce.users.schema.User;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private SecurityService securityService;
    private TokenService tokenService;
    private EmailService emailService;
    public AuthServiceImpl(UserRepository userRepository , SecurityService securityService, TokenService tokenService, EmailService emailService){
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.tokenService = tokenService;
        this.emailService = emailService;
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
        
        String encryptedPhone = null;
        if (request.getPhoneNumber() != null) {
            try {
                encryptedPhone = this.securityService.encrypt(request.getPhoneNumber());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }

        User createUser = User.builder()
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .fullName(request.getFirstName() + " " + request.getLastName())
                            .password(this.securityService.hash(request.getPassword()))
                            .role(request.getRole())
                            .phoneNumber(encryptedPhone)
                            .build();


        this.emailService.sendHtmlEmail(request.getEmail(), "Welcome to our platform", "<h1>Welcome to our platform</h1>");
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

        TokenPayload payload = new TokenPayload(user.getId(), user.getRole());
        LoginResponseDto respone = this.tokenService.generateTokens(payload);
        return respone;
    }
    
}
