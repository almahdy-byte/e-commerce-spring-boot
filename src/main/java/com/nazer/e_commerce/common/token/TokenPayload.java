package com.nazer.e_commerce.common.token;

import org.bson.types.ObjectId;

import com.nazer.e_commerce.users.enums.UserRoles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenPayload {
    private ObjectId id;
    private UserRoles role;
}
