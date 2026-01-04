package mo.demo.dao;

import mo.demo.model.Task;
import mo.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskDao {
    
    Task save(Task task);
    
    Optional<Task> findById(UUID id);
    
    List<Task> findByCreatedBy(User createdBy);
    
    List<Task> findByStatus(Task.TaskStatus status);
    
    List<Task> findSubmittedTasks();
    
    List<Task> findByUserAndStatus(User user, Task.TaskStatus status);
    
    void deleteById(UUID id);
    
    List<Task> findAll();
    
    List<Task> findAllWithCreatedBy();
}
