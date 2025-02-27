package com.example.taskmanagementservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    private static final List<Tag> TAGS = List.of(
            new Tag().name("Auth").description("Регистрация и аутентификация, обновление токенов, выход"),
            new Tag().name("User").description("Управление пользователями"),
            new Tag().name("Task").description("Управление задачами"),
            new Tag().name("Comment").description("Управление комментариями")
    );

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpenAPI specification TaskManagementService")
                        .description("Documentation of the project management of tasks")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Egor Ivanov")
                                .email("Egor.Ivanov.v@yandex.ru"))
                        .license(new License()
                                .name("")
                                .url("")))
                .tags(TAGS)
                .servers(List.of(
                        new Server()
                                .description("Local ENV")
                                .url("http//:localhost:9090"),
                        new Server()
                                .description("Prod")
                                .url("")
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token Auth", new SecurityScheme()
                                .name("Bearer Token Auth")
                                .description("JWT auth with refresh and access tokens")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)));
    }


    @Bean
    public OpenApiCustomizer tagSortingOpenAPICustomizer() {
        return openApi -> {
            List<Tag> configuredTags = TAGS;

            List<Tag> allTags = openApi.getTags();

            if (allTags != null) {
                allTags.removeIf(tag -> configuredTags.stream()
                        .anyMatch(configuredTag -> configuredTag.getName().equals(tag.getName())));
            } else {
                allTags = new ArrayList<>();
            }

            List<Tag> sortedTags = new ArrayList<>(configuredTags);
            sortedTags.addAll(allTags);

            openApi.setTags(sortedTags);
        };
    }

    @Bean
    public OpenApiCustomizer schemaSortingOpenAPICustomizer() {
        return openApi -> {
            // Получаем все схемы из OpenAPI
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();

            if (schemas != null) {
                // Создаем список для сортировки схем
                List<Map.Entry<String, Schema>> schemaList = new ArrayList<>(schemas.entrySet());

                // Определяем порядок сортировки
                schemaList.sort((entry1, entry2) -> {
                    String name1 = entry1.getKey();
                    String name2 = entry2.getKey();

                    // Приоритет 1: схемы с "Reg" или "Auth"
                    boolean isRegOrAuth1 = name1.contains("Reg") || name1.contains("Auth");
                    boolean isRegOrAuth2 = name2.contains("Reg") || name2.contains("Auth");
                    if (isRegOrAuth1 != isRegOrAuth2) {
                        return isRegOrAuth1 ? -1 : 1;
                    }

                    // Приоритет 2: схемы с "User", "Task", "Comment"
                    boolean isUserTaskComment1 = name1.contains("User") || name1.contains("Task") || name1.contains("Comment");
                    boolean isUserTaskComment2 = name2.contains("User") || name2.contains("Task") || name2.contains("Comment");
                    if (isUserTaskComment1 != isUserTaskComment2) {
                        return isUserTaskComment1 ? -1 : 1;
                    }

                    // Все остальные схемы сортируются по алфавиту
                    return name1.compareTo(name2);
                });

                Map<String, Schema> sortedSchemas = new LinkedHashMap<>();
                for (Map.Entry<String, Schema> entry : schemaList) {
                    sortedSchemas.put(entry.getKey(), entry.getValue());
                }

                openApi.getComponents().setSchemas(sortedSchemas);
            }
        };
    }
}