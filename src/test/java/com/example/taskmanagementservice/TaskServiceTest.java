package com.example.taskmanagementservice;

import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskMapper;
import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.repository.TaskRepository;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;
import com.example.taskmanagementservice.task.service.TaskServiceImpl;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User author;
    private User performer;
    private Task task;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1);
        author.setEmail("author@example.com");

        performer = new User();
        performer.setId(2);
        performer.setEmail("performer@example.com");

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .author(author)
                .performer(performer)
                .build();
    }

    private void setAuthentication(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null)
        );
    }

    @Test
    void createTask_Success() {
        // Arrange
        setAuthentication(author);


        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Task")
                .description("Desc")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .authorId(author.getId())
                .performerId(performer.getId())
                .build();

        when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(userRepository.findById(2)).thenReturn(Optional.of(performer));
        when(taskMapper.toTask(request, author, performer)).thenReturn(task);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskService.createTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_Success() {
        // Arrange

        setAuthentication(author);
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .taskId(1L)
                .title("Updated")
                .description("New Desc")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .performerId(performer.getId())
                .build();

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated")
                .description("New Desc")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .author(author)
                .performer(performer)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2)).thenReturn(Optional.of(performer));

        // Мок
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = taskService.updateTask(request);

        // Assert
        assertEquals("Updated", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus()); // Проверяем статус
        assertEquals(TaskPriority.HIGH, result.getPriority()); // Проверяем приоритет
        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void updateTaskStatus_ByAuthor_Success() {
        // Arrange
        setAuthentication(author);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskService.updateTaskStatus(1L, TaskStatus.COMPLETED);

        // Assert
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskStatus_ByPerformer_Success() {
        // Arrange
        setAuthentication(performer);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void updateTaskStatus_Unauthorized_ThrowsException() {
        // Arrange
        User stranger = new User();
        stranger.setId(3);
        setAuthentication(stranger);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(AuthenticationException.class, () ->
                taskService.updateTaskStatus(1L, TaskStatus.COMPLETED));
    }

    @Test
    void getTaskById_NotFound_ThrowsException() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                taskService.getTaskById(1L));
    }

    @Test
    void getAllTasks_WithFilters_ReturnsPage() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        when(taskRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty());

        // Act
        Page<Task> result = taskService.getAllTasks(TaskStatus.PENDING, TaskPriority.MEDIUM, pageable);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getTasksForAuthor_ReturnsList() {
        // Arrange
        setAuthentication(author);
        when(taskRepository.findByAuthor(author)).thenReturn(Collections.singletonList(task));

        // Act
        List<Task> result = taskService.getTasksForAuthor();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void deleteTask_Success() {
        // Arrange
        setAuthentication(author);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository).delete(task);
    }
}