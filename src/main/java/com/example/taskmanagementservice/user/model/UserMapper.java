package com.example.taskmanagementservice.user.model;

import com.example.taskmanagementservice.auth.request.AuthenticationRequest;
import com.example.taskmanagementservice.auth.request.RegistrationRequest;
import com.example.taskmanagementservice.auth.request.RegistrationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "request.email", target = "email")
    @Mapping(source = "request.password", target = "password")
    @Mapping(source = "request.role", target = "role")
    User toUser(RegistrationRequest request);

    @Mapping(source = "request.email", target = "email")
    @Mapping(source = "request.password", target = "password")
    User toUser(AuthenticationRequest request);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.role", target = "role")
    RegistrationResponse toRegistrationResponse(User user);
}
