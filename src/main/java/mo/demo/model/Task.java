package mo.demo.model;

import lombok.Data;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.DRAFT;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    @Column(length = 500)
    private String rejectionReason;
    
    @Column(updatable = false)
    private String created_at;
    
    private String updated_at;
    
    @PrePersist
    protected void onCreate() {
        created_at = java.time.LocalDateTime.now().toString();
        updated_at = java.time.LocalDateTime.now().toString();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updated_at = java.time.LocalDateTime.now().toString();
    }
    
    public enum TaskStatus {
        DRAFT,
        SUBMITTED,
        APPROVED,
        REJECTED
    }
}
