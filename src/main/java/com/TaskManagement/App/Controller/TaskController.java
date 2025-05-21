package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Dto.TaskRequest;
import com.TaskManagement.App.Exception.ApiException;
import com.TaskManagement.App.Model.Task;
import com.TaskManagement.App.Model.TaskStatus;
import com.TaskManagement.App.Repository.TaskRepository;
import com.TaskManagement.App.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;


    // create tasks by ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request,
                                           Authentication authentication) {

        Task task = taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getAssignedToId(),
                request.getDueDate(),
                request.getConnectTo()
        );

        return ResponseEntity.ok(task);
    }

    // Update Tasks by Admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        Task updatedTask = taskService.updateTask(
                taskId,
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getDueDate()
        );
        return ResponseEntity.ok(updatedTask);
    }

    // Get All Tasks - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }


    // Get Employee by ID
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','SUPERVISOR')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    // COMPLETED Task by Client - only CLIENT
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/complete/{taskId}")
    public ResponseEntity<Task> completeTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    // END Task by ADMIN - only ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/complete/{taskId}")
    public ResponseEntity<Task> completeTaskByAdmin(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }


    // Delete Tasks by Admin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

}
