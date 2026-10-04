package com.taskflow.repo;

import com.taskflow.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerIdOrderByIdDesc(Long ownerId);
}
