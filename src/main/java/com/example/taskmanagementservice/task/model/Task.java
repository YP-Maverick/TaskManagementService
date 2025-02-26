package com.example.taskmanagementservice.task.model;

import com.example.taskmanagementservice.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {

    /**
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )

    + Надо добавить в парамметры spring.jpa.properties.hibernate.id.uuid_representation=POSTGRESQL_UUID
    */

    /**
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obfuscated_id_gen")
    @GenericGenerator(
        name = "obfuscated_id_gen",
        strategy = "com.yourpackage.ObfuscatedIdGenerator"
    )
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    @Enumerated(EnumType.STRING)
    TaskStatus status;

    @Enumerated(EnumType.STRING)
    TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", nullable = false)
    private User performer;
}
