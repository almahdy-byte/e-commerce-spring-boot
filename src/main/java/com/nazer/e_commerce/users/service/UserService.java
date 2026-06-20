package com.nazer.e_commerce.users.service;

import com.nazer.e_commerce.common.global.PaginatedResponse;
import com.nazer.e_commerce.users.Dto.UpdateProfileDto;
import com.nazer.e_commerce.users.enums.Provider;
import com.nazer.e_commerce.users.enums.UserRoles;
import com.nazer.e_commerce.users.schema.User;

public interface UserService {
    User getProfile();
    User updateProfile(UpdateProfileDto dto);
    PaginatedResponse<User> getUsers(int page, int size, UserRoles role, Provider provider, String search);
}
