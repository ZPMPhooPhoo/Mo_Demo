package mo.demo.repository;

import mo.demo.model.Task;
import mo.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByCreatedBy(User createdBy);
    List<Task> findByStatus(Task.TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.status IN ('SUBMITTED', 'APPROVED', 'REJECTED')")
    List<Task> findSubmittedTasks();
    
    @Query("SELECT t FROM Task t WHERE t.createdBy = :user AND t.status = :status")
    List<Task> findByUserAndStatus(@Param("user") User user, @Param("status") Task.TaskStatus status);
}
