package com.example.todo.dto;

import com.example.todo.entity.TaskPriority;
import com.example.todo.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskDtos {

    public static class CreateTaskRequest {
        @NotNull
        private Long userId;

        @NotBlank
        @Size(max = 300)
        private String title;
        private String description;
        private TaskPriority priority = TaskPriority.MEDIUM;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public TaskPriority getPriority() {
            return priority;
        }

        public void setPriority(TaskPriority priority) {
            this.priority = priority;
        }
    }

    public static class UpdateTaskStatusRequest {
        @NotNull
        private TaskStatus status;

        public TaskStatus getStatus() {
            return status;
        }

        public void setStatus(TaskStatus status) {
            this.status = status;
        }
    }

    public static class TaskResponse {
        private Long id;
        private Integer userId;
        private String title;
        private String description;
        private TaskStatus status;
        private TaskPriority priority;
        private java.time.OffsetDateTime createdAt;
        private java.time.OffsetDateTime updatedAt;

        public TaskResponse() {
        }

        public TaskResponse(Long id, Integer userId, String title, String description,
                            TaskStatus status, TaskPriority priority,
                            java.time.OffsetDateTime createdAt, java.time.OffsetDateTime updatedAt) {
            this.id = id;
            this.userId = userId;
            this.title = title;
            this.description = description;
            this.status = status;
            this.priority = priority;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public TaskStatus getStatus() {
            return status;
        }

        public void setStatus(TaskStatus status) {
            this.status = status;
        }

        public TaskPriority getPriority() {
            return priority;
        }

        public void setPriority(TaskPriority priority) {
            this.priority = priority;
        }

        public java.time.OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public java.time.OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(java.time.OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
