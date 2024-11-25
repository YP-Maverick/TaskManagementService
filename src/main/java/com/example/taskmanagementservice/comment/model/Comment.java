package com.example.taskmanagementservice.comment.model;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id", nullable = false)
    private User commentator;

    @NotBlank(message = "Content is mandatory")
    @Column(nullable = false, length = 2500)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    //TODO Вложенность комментариев
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @Transient
    private List<Comment> children = new ArrayList<>();*/
}
