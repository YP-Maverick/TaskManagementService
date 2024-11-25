package com.example.taskmanagementservice.task.model;

import com.example.taskmanagementservice.comment.model.Comment;
import com.example.taskmanagementservice.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    TaskStatus status;

    @NotNull(message = "Priority is mandatory")
    @Enumerated(EnumType.STRING)
    TaskPriority taskPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", nullable = false)
    private User performer;

    /*@OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;*/
}
