package mo.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@CrossOrigin(origins = "${cors.allowed-origins:*}")
@Slf4j
public class TaskController {
    
    private final TaskService taskService; 
    

    //Role-User: Any authenticated user can create tasks
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        Task createdTask = taskService.createTask(task, authentication.getName());
        log.info("Task created: {} by user: {}", createdTask.getId(), authentication.getName());
        return ResponseEntity.ok(createdTask);
    }
    
    // Role-User: Only users with MANAGER role can save drafts
    @PostMapping("/{id}/save-draft")
    public ResponseEntity<Task> saveAsDraft(@PathVariable UUID id, @RequestBody Task task, Authentication authentication) {
        task.setId(id);
        Task savedTask = taskService.saveAsDraft(task, authentication.getName());
        log.info("Task saved as draft: {} by user: {}", savedTask.getId(), authentication.getName());
        return ResponseEntity.ok(savedTask);
    }
    
    // Role-User: Any authenticated user can submit tasks
    @PutMapping("/{id}/submit")
    public ResponseEntity<Task> submitTask(@PathVariable UUID id, Authentication authentication) {
        Task submittedTask = taskService.submitTask(id, authentication.getName());
        log.info("Task submitted: {} by user: {}", submittedTask.getId(), authentication.getName());
        return ResponseEntity.ok(submittedTask);
    }
    
    // Role-User: Any authenticated user can view their own tasks
    @GetMapping("/my")
    public ResponseEntity<List<Task>> getMyTasks(Authentication authentication) {
        List<Task> tasks = taskService.getMyTasks(authentication.getName());
        log.info("Retrieved {} tasks for user: {}", tasks.size(), authentication.getName());
        return ResponseEntity.ok(tasks);
    }
    
    // Role-User: Any authenticated user can view submitted tasks
    @GetMapping("/submitted")
    public ResponseEntity<List<Task>> getSubmittedTasks(Authentication authentication) {
        List<Task> tasks = taskService.getSubmittedTasks();
        log.info("Retrieved {} submitted tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }
    
    // Role-Approver: Only users with APPROVER role can view submitted tasks
    @PutMapping("/{id}/approve")
    public ResponseEntity<Task> approveTask(@PathVariable UUID id, Authentication authentication) {
        Task approvedTask = taskService.approveTask(id, authentication.getName());
        log.info("Task approved: {} by user: {}", approvedTask.getId(), authentication.getName());
        return ResponseEntity.ok(approvedTask);
    }
    
    
    // Role-Approver: Only users with APPROVER role can reject tasks
    @PutMapping("/{id}/reject")
    public ResponseEntity<Task> rejectTask(@PathVariable UUID id, @RequestBody String rejectionReason, Authentication authentication) {
        Task rejectedTask = taskService.rejectTask(id, rejectionReason, authentication.getName());
        log.info("Task rejected: {} by user: {} with reason: {}", rejectedTask.getId(), authentication.getName(), rejectionReason);
        return ResponseEntity.ok(rejectedTask);
    }
    
    
    // Role-All: All authenticated users can view individual tasks
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);
        log.info("Task retrieved: {}", task.getId());
        return ResponseEntity.ok(task);
    }
    
    
    // Role-All: All authenticated users can view tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        log.info("Tasks retrieved by status: {} count: {}", status, tasks.size());
        return ResponseEntity.ok(tasks);
    }
}
