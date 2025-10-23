package com.example.todo.repository;

import com.example.todo.entity.Task;
import com.example.todo.entity.TaskStatus;
import com.example.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserOrderByCreatedAtDesc(User user);
    List<Task> findByUserAndStatusOrderByCreatedAtDesc(User user, TaskStatus status);
}
