package com.nazer.e_commerce.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nazer.e_commerce.common.global.PaginatedResponse;
import com.nazer.e_commerce.common.global.PaginatedResponse.Pagination;
import com.nazer.e_commerce.users.Dto.UpdateProfileDto;
import com.nazer.e_commerce.users.enums.Provider;
import com.nazer.e_commerce.users.enums.UserRoles;
import com.nazer.e_commerce.users.repository.UserRepository;
import com.nazer.e_commerce.users.schema.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User getProfile() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public User updateProfile(UpdateProfileDto dto) {
        User user = getProfile();

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getFirstName() != null || dto.getLastName() != null) {
            user.setFullName(user.getFirstName() + " " + user.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getDob() != null) {
            user.setDob(dto.getDob());
        }

        return userRepository.save(user);
    }

    @Override
    public PaginatedResponse<User> getUsers(int page, int size, UserRoles role, Provider provider, String search) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (role != null) {
            criteriaList.add(Criteria.where("role").is(role));
        }
        if (provider != null) {
            criteriaList.add(Criteria.where("provider").is(provider));
        }
        if (search != null && !search.isBlank()) {
            String regex = search.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
            criteriaList.add(new Criteria().orOperator(
                Criteria.where("userName").regex(regex, "i"),
                Criteria.where("email").regex(regex, "i")
            ));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        System.out.println(query);
        long total = mongoTemplate.count(query, User.class);

        Pageable pageable = PageRequest.of(page - 1, size);
        System.out.println(pageable);
        query.with(pageable);
        System.out.println(query);
        List<User> users = mongoTemplate.find(query, User.class);

        Pagination pagination = Pagination.builder()
            .total(total)
            .totalPages((int) Math.ceil((double) total / size))
            .page(page)
            .count(users.size())
            .build();

        return PaginatedResponse.<User>builder()
            .data(users)
            .pagination(pagination)
            .build();
    }
}
