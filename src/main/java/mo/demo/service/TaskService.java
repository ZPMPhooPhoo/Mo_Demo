package mo.demo.service;

import lombok.RequiredArgsConstructor;
import mo.demo.dao.TaskDao;
import mo.demo.dao.UserDao;
import mo.demo.model.Task;
import mo.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskDao taskDao;
    private final UserDao userDao;
    
    public Task createTask(Task task, String userEmail) {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));
        
        task.setCreatedBy(user);
        task.setStatus(Task.TaskStatus.DRAFT);
        return taskDao.save(task);
    }
    
    public Task saveAsDraft(Task task, String userEmail) {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));
        
        task.setCreatedBy(user);
        task.setStatus(Task.TaskStatus.DRAFT);
        return taskDao.save(task);
    }
    
    public Task submitTask(UUID taskId, String userEmail) {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));
        
        if (!task.getCreatedBy().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only submit your own tasks.");
        }
        
        if (task.getStatus() != Task.TaskStatus.DRAFT) {
            throw new RuntimeException("Only draft tasks can be submitted.");
        }
        
        task.setStatus(Task.TaskStatus.SUBMITTED);
        return taskDao.save(task);
    }
    
    public Task approveTask(UUID taskId, String approverEmail) {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));
        
        User approver = userDao.findByEmail(approverEmail)
                .orElseThrow(() -> new RuntimeException("Approver not found."));
        
        if (task.getStatus() != Task.TaskStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted tasks can be approved.");
        }
        
        task.setStatus(Task.TaskStatus.APPROVED);
        task.setApprovedBy(approver);
        task.setRejectionReason(null);
        return taskDao.save(task);
    }
    
    public Task rejectTask(UUID taskId, String rejectionReason, String approverEmail) {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));
        
        User approver = userDao.findByEmail(approverEmail)
                .orElseThrow(() -> new RuntimeException("Approver not found."));
        
        if (task.getStatus() != Task.TaskStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted tasks can be rejected.");
        }
        
        task.setStatus(Task.TaskStatus.REJECTED);
        task.setApprovedBy(approver);
        task.setRejectionReason(rejectionReason);
        return taskDao.save(task);
    }
    
    public List<Task> getMyTasks(String userEmail) {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));
        
        return taskDao.findByCreatedBy(user);
    }
    
    public List<Task> getSubmittedTasks() {
        return taskDao.findSubmittedTasks();
    }
    
    public Task getTaskById(UUID taskId) {
        return taskDao.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found."));
    }
    
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskDao.findByStatus(status);
    }
    
    public List<Task> getAllTasks() {
        return taskDao.findAllWithCreatedBy();
    }
}
