package com.TaskManagement.App.Service;

import com.TaskManagement.App.Exception.ApiException;
import com.TaskManagement.App.Model.Task;
import com.TaskManagement.App.Model.TaskStatus;
import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Repository.TaskRepository;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Repository.UserEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final UserEmployeeRepository userEmployeeRepository;
    private final UserClientRepository userClientRepository;


    // Create a new task (used by Admin)
    public Task createTask(String title,
                           String description,
                           String status,
                           Long assignedTo,
                           LocalDateTime dueDate,
                           Long connectTo) {
        Optional<UserEmployee> user = userEmployeeRepository.findById(assignedTo);
        Optional<UserClient> userClient = userClientRepository.findById(connectTo);

        if (user.isEmpty()) {
            throw new ApiException("الموظف غير موجود", "Employee not found");
        }
        if (userClient.isEmpty()) {
            throw new ApiException("العميل غير موجود", "Client not found");
        }
        if (taskRepository.existsByConnectTo(userClient.get())) {
            throw new ApiException("هذا العميل لديه مهمة بالفعل", "Client already has a task.");
        }

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.PENDING)
                .assignedTo(user.get())
                .dueDate(dueDate)
                .connectTo(userClient.get())
                .createdAt(LocalDateTime.now())
                .build();
        return taskRepository.save(task);
    }

    // Update tasks in use (role admin only)
    public Task updateTask(Long taskId,
                           String title,
                           String description,
                           String status,
                           LocalDateTime dueDate) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }

        Task task = taskOptional.get();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(dueDate);
        return taskRepository.save(task);
    }


    // Get All Tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks");
        }
        return tasks;
    }



    // Get Tasks By User
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }


    // End Tasks by ADMIN or CLIENT (State to COMPLETED)
    public Task completeTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }
        Task task = taskOptional.get();
        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }


    // Transfer a task to another employee (used by Employee)
    public Task transferTask(Long taskId, Long newAssignedToId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }
        Optional<UserEmployee> newUser = userEmployeeRepository.findById(newAssignedToId);
        if (newUser.isEmpty()) {
            throw new RuntimeException("Target user not found!");
        }
        Task task = taskOptional.get();
        task.setAssignedTo(newUser.get());
        return taskRepository.save(task);
    }



    // Delete task (used by Admin)
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found!");
        }
        taskRepository.deleteById(taskId);
    }








}
