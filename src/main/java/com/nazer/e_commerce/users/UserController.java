package com.nazer.e_commerce.users;

import com.nazer.e_commerce.common.global.PaginatedResponse;
import com.nazer.e_commerce.users.Dto.UpdateProfileDto;
import com.nazer.e_commerce.users.enums.Provider;
import com.nazer.e_commerce.users.enums.UserRoles;
import com.nazer.e_commerce.users.schema.User;
import com.nazer.e_commerce.users.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/users", "/usres"})
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("")
    public User getProfile(){
      return this.userService.getProfile();
    }

    @PutMapping("")
    public User updateProfile(@RequestBody @Valid UpdateProfileDto dto){
      return this.userService.updateProfile(dto);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole(T(com.nazer.e_commerce.users.enums.UserRoles).ADMIN.name())")
    public PaginatedResponse<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UserRoles role,
            @RequestParam(required = false) Provider provider,
            @RequestParam(required = false) String search) {
        return this.userService.getUsers(page, size, role, provider, search);
    }
    
}
