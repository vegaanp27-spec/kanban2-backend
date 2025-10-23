package com.example.todo.service;

import com.example.todo.entity.*;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> listByUser(Long userId, TaskStatus status) {
        User user = userRepository.findById(userId).orElseThrow();
        if (status == null) return taskRepository.findByUserOrderByCreatedAtDesc(user);
        return taskRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
    }

    @Transactional
    public Task create(Integer userId, String title, String description, TaskPriority priority) {
        User user = userRepository.findById(userId.longValue()).orElseThrow();
        Task t = new Task();
        t.setUser(user);
        t.setTitle(title);
        t.setDescription(description);
        t.setPriority(priority == null ? TaskPriority.MEDIUM : priority);
        t.setStatus(TaskStatus.TODO);
        return taskRepository.save(t);
    }

    @Transactional
    public Task changeStatus(Long taskId, TaskStatus to) {
        Task t = taskRepository.findById(taskId).orElseThrow();
        t.setStatus(to);
        return t;
    }

    @Transactional
    public void delete(Long taskId) { taskRepository.deleteById(taskId); }
}
