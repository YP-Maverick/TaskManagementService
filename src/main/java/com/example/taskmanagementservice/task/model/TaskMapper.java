package com.example.taskmanagementservice.task.model;

import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class})
public interface TaskMapper {

    @Mapping(source = "task.author.id", target = "authorId")
    @Mapping(source = "task.performer.id", target = "performerId")
    @Mapping(source = "task.status", target = "status")
    @Mapping(source = "task.taskPriority", target = "taskPriority")
    TaskDto toDto(Task task);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "taskPriority", expression = "java(TaskPriority.valueOf(createTaskDto.getPriority()))")
    @Mapping(target = "author.id", source = "createTaskDto.authorId")
    @Mapping(target = "performer.id", source = "createTaskDto.performerId")
    Task toTask(CreateTaskRequest createTaskDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", expression = "java(TaskStatus.valueOf(taskDto.getStatus()))")
    @Mapping(target = "taskPriority", expression = "java(TaskPriority.valueOf(taskDto.getTaskPriority()))")
    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "performer.id", source = "performerId")
    Task toTask(TaskDto taskDto);

    List<TaskDto> toDtoList(List<Task> taskList);
    List<Task> toEntityList(List<TaskDto> taskDtoList);

    default String mapTaskStatus(TaskStatus status) {
        return status != null ? status.name() : null;
    }

    default String mapTaskPriority(TaskPriority priority) {
        return priority != null ? priority.name() : null;
    }
}