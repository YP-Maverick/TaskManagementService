package com.example.taskmanagementservice;

import com.example.taskmanagementservice.task.model.TaskMapper;
import com.example.taskmanagementservice.task.dto.TaskDto;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {
/*
    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Test
    public void testToDto() {
        Task task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.IN_PROGRESS)
                .taskPriority(TaskPriority.HIGH)
                .author(User.builder().id(2L).build())
                .performer(User.builder().id(3L).build())
                .build();

        TaskDto dto = mapper.toDto(task);

        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getTitle(), dto.getTitle());
        assertEquals(task.getDescription(), dto.getDescription());
        assertEquals(task.getStatus().name(), dto.getStatus());
        assertEquals(task.getTaskPriority().name(), dto.getTaskPriority());
        assertEquals(task.getAuthor().getId(), dto.getAuthorId());
        assertEquals(task.getPerformer().getId(), dto.getPerformerId());
    }

    @Test
    public void testToTaskFromTaskDto() {
        TaskDto taskDto = TaskDto.builder()
                .id(1L)
                .title("Task DTO")
                .description("Task Description")
                .status("IN_PROGRESS")
                .taskPriority("MEDIUM")
                .authorId(4L)
                .performerId(5L)
                .build();

        Task task = mapper.toTask(taskDto);

        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getTitle(), task.getTitle());
        assertEquals(taskDto.getDescription(), task.getDescription());
        assertEquals(TaskStatus.valueOf(taskDto.getStatus()), task.getStatus());
        assertEquals(TaskPriority.valueOf(taskDto.getTaskPriority()), task.getTaskPriority());
        assertEquals(taskDto.getAuthorId(), task.getAuthor().getId());
        assertEquals(taskDto.getPerformerId(), task.getPerformer().getId());
    }

    // Дополнительные тесты для списка
    @Test
    public void testToDtoList() {
        List<Task> taskList = Arrays.asList(
                Task.builder().id(1L).title("Task 1").description("Desc 1").status(TaskStatus.IN_PROGRESS).taskPriority(TaskPriority.HIGH).author(User.builder().id(2L).build()).performer(User.builder().id(3L).build()).build(),
                Task.builder().id(2L).title("Task 2").description("Desc 2").status(TaskStatus.COMPLETED).taskPriority(TaskPriority.LOW).author(User.builder().id(4L).build()).performer(User.builder().id(5L).build()).build()
        );

        List<TaskDto> dtoList = mapper.toDtoList(taskList);

        assertEquals(taskList.size(), dtoList.size());
        for (int i = 0; i < taskList.size(); i++) {
            assertEquals(taskList.get(i).getId(), dtoList.get(i).getId());
            assertEquals(taskList.get(i).getTitle(), dtoList.get(i).getTitle());
            assertEquals(taskList.get(i).getDescription(), dtoList.get(i).getDescription());
            assertEquals(taskList.get(i).getStatus().name(), dtoList.get(i).getStatus());
            assertEquals(taskList.get(i).getTaskPriority().name(), dtoList.get(i).getTaskPriority());
        }
    }

    @Test
    public void testToEntityList() {
        List<TaskDto> dtoList = Arrays.asList(
                TaskDto.builder().id(1L).title("Task DTO 1").description("Description 1").status("IN_PROGRESS").taskPriority("HIGH").authorId(2L).performerId(3L).build(),
                TaskDto.builder().id(2L).title("Task DTO 2").description("Description 2").status("COMPLETED").taskPriority("MEDIUM").authorId(4L).performerId(5L).build()
        );

        List<Task> taskList = mapper.toEntityList(dtoList);

        assertEquals(dtoList.size(), taskList.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getId(), taskList.get(i).getId());
            assertEquals(dtoList.get(i).getTitle(), taskList.get(i).getTitle());
            assertEquals(dtoList.get(i).getDescription(), taskList.get(i).getDescription());
            assertEquals(TaskStatus.valueOf(dtoList.get(i).getStatus()), taskList.get(i).getStatus());
            assertEquals(TaskPriority.valueOf(dtoList.get(i).getTaskPriority()), taskList.get(i).getTaskPriority());
            assertEquals(dtoList.get(i).getAuthorId(), taskList.get(i).getAuthor().getId());
            assertEquals(dtoList.get(i).getPerformerId(), taskList.get(i).getPerformer().getId());
        }
    }*/
}
