package mo.demo.dao.impl;

import lombok.RequiredArgsConstructor;
import mo.demo.dao.TaskDao;
import mo.demo.model.Task;
import mo.demo.model.User;
import mo.demo.repository.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskDaoImpl implements TaskDao {
    
    private final TaskRepository taskRepository;
    
    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
    
    @Override
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id);
    }
    
    @Override
    public List<Task> findByCreatedBy(User createdBy) {
        return taskRepository.findByCreatedBy(createdBy);
    }
    
    @Override
    public List<Task> findByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
    
    @Override
    public List<Task> findSubmittedTasks() {
        return taskRepository.findSubmittedTasks();
    }
    
    @Override
    public List<Task> findByUserAndStatus(User user, Task.TaskStatus status) {
        return taskRepository.findByUserAndStatus(user, status);
    }
    
    @Override
    public void deleteById(UUID id) {
        taskRepository.deleteById(id);
    }
    
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}
