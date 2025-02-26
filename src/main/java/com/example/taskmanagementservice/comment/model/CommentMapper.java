package com.example.taskmanagementservice.comment.model;

import com.example.taskmanagementservice.comment.dto.CommentDto;
import com.example.taskmanagementservice.user.model.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserMapper.class)
public interface CommentMapper {

    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdAt", target = "timestamp")
    @Mapping(source = "replies", target = "replies")
    CommentDto toDto(Comment comment);

    @Mapping(target = "commentId", source = "commentId")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createdAt", source = "timestamp")
    @Mapping(target = "replies", source = "replies")
    Comment toEntity(CommentDto commentDto);


    List<CommentDto> toDtoList(List<Comment> comments);
}