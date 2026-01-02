package mo.demo.controller;

import lombok.RequiredArgsConstructor;
import mo.demo.model.Task;
import mo.demo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        Task createdTask = taskService.createTask(task, authentication.getName());
        return ResponseEntity.ok(createdTask);
    }
    
    @PostMapping("/{id}/save-draft")
    public ResponseEntity<Task> saveAsDraft(@PathVariable UUID id, @RequestBody Task task, Authentication authentication) {
        task.setId(id);
        Task savedTask = taskService.saveAsDraft(task, authentication.getName());
        return ResponseEntity.ok(savedTask);
    }
    
    @PutMapping("/{id}/submit")
    public ResponseEntity<Task> submitTask(@PathVariable UUID id, Authentication authentication) {
        Task submittedTask = taskService.submitTask(id, authentication.getName());
        return ResponseEntity.ok(submittedTask);
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<Task>> getMyTasks(Authentication authentication) {
        List<Task> tasks = taskService.getMyTasks(authentication.getName());
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/submitted")
    public ResponseEntity<List<Task>> getSubmittedTasks(Authentication authentication) {
        List<Task> tasks = taskService.getSubmittedTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<Task> approveTask(@PathVariable UUID id, Authentication authentication) {
        Task approvedTask = taskService.approveTask(id, authentication.getName());
        return ResponseEntity.ok(approvedTask);
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<Task> rejectTask(@PathVariable UUID id, @RequestBody String rejectionReason, Authentication authentication) {
        Task rejectedTask = taskService.rejectTask(id, rejectionReason, authentication.getName());
        return ResponseEntity.ok(rejectedTask);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
}
