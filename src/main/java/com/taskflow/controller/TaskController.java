package com.taskflow.controller;

import com.taskflow.model.Task;
import com.taskflow.model.User;
import com.taskflow.repo.TaskRepository;
import com.taskflow.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    public record TaskReq(@NotBlank String title, String description, Task.Status status,
                          Task.Priority priority, LocalDate dueDate) {}

    private final TaskRepository tasks;
    private final AuthService auth;

    public TaskController(TaskRepository tasks, AuthService auth) {
        this.tasks = tasks;
        this.auth = auth;
    }

    @GetMapping
    public List<Task> list(@RequestHeader(value = "Authorization", required = false) String h) {
        return tasks.findByOwnerIdOrderByIdDesc(auth.require(h).getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestHeader(value = "Authorization", required = false) String h,
                       @Valid @RequestBody TaskReq r) {
        User u = auth.require(h);
        Task t = new Task();
        t.setOwnerId(u.getId());
        return tasks.save(apply(t, r));
    }

    @PutMapping("/{id}")
    public Task update(@RequestHeader(value = "Authorization", required = false) String h,
                       @PathVariable Long id, @Valid @RequestBody TaskReq r) {
        return tasks.save(apply(owned(h, id), r));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(value = "Authorization", required = false) String h, @PathVariable Long id) {
        tasks.delete(owned(h, id));
    }

    private Task owned(String header, Long id) {
        User u = auth.require(header);
        Task t = tasks.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        if (!u.getId().equals(t.getOwnerId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your task");
        return t;
    }

    private Task apply(Task t, TaskReq r) {
        t.setTitle(r.title());
        t.setDescription(r.description());
        if (r.status() != null) t.setStatus(r.status());
        if (r.priority() != null) t.setPriority(r.priority());
        t.setDueDate(r.dueDate());
        return t;
    }
}
