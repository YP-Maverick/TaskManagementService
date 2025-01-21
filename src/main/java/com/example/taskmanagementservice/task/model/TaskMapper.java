package com.example.taskmanagementservice.task.model;

import com.example.taskmanagementservice.task.dto.TaskDto;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDateTime.class})
public interface TaskMapper {

    @Mapping(source = "task.author.id", target = "authorId")
    @Mapping(source = "task.performer.id", target = "performerId")
    @Mapping(source = "task.status", target = "status")
    @Mapping(source = "task.priority", target = "priority")
    TaskDto toDto(Task task);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "performer.id", source = "performerId")
    Task toTask(TaskDto taskDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "status", source = "request.status"),
            @Mapping(target = "author", source = "author"),
            @Mapping(target = "performer", source = "performer")
    })
    Task toTask(CreateTaskRequest request, User author, User performer);

    List<TaskDto> toDtoList(List<Task> taskList);
    List<Task> toEntityList(List<TaskDto> taskDtoList);



        default String mapTaskStatus(TaskStatus status) {
        return status != null ? status.name() : null;
    }

    default String mapTaskPriority(TaskPriority priority) {
        return priority != null ? priority.name() : null;
    }
}
