package com.example.todo.controller;

import com.example.todo.dto.TaskDtos.CreateTaskRequest;
import com.example.todo.dto.TaskDtos.TaskResponse;
import com.example.todo.dto.TaskDtos.UpdateTaskStatusRequest;
import com.example.todo.entity.Task;
import com.example.todo.entity.TaskStatus;
import com.example.todo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Gestión de tareas")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @GetMapping
    @Operation(summary = "Listar tareas de un usuario (opcional filtrar por estado)")
    public ResponseEntity<List<TaskResponse>> listByUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) TaskStatus status,
            HttpServletRequest request) {
        Long effectiveUserId = userId;
        if (effectiveUserId == null) {
            Object attr = request.getAttribute("userId");
            if (attr instanceof Long) effectiveUserId = (Long) attr;
            else if (attr instanceof Integer) effectiveUserId = ((Integer) attr).longValue();
        }
        if (effectiveUserId == null) throw new IllegalArgumentException("userId no proporcionado");
        List<TaskResponse> out = taskService.listByUser(effectiveUserId, status).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(out);
    }

    @PostMapping
    @Operation(summary = "Crear tarea")
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid CreateTaskRequest req, HttpServletRequest request) {
        Long uid = req.getUserId();
        if (uid == null) {
            Object attr = request.getAttribute("userId");
            if (attr instanceof Long) uid = (Long) attr;
            else if (attr instanceof Integer) uid = (attr == null ? null : ((Integer) attr).longValue());
        }
        if (uid == null) throw new IllegalArgumentException("userId no proporcionado");
        Task t = taskService.create(uid.intValue(), req.getTitle(), req.getDescription(), req.getPriority());
        return ResponseEntity.created(URI.create("/api/tasks/" + t.getId())).body(toResponse(t));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Cambiar estado de tarea")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable Long id, @RequestBody @Valid UpdateTaskStatusRequest req) {
        Task t = taskService.changeStatus(id, req.getStatus());
        return ResponseEntity.ok(toResponse(t));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarea")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getUser().getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
