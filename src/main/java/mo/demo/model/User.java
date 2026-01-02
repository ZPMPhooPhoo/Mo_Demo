package mo.demo.model;

import lombok.Data;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(length = 50)
    private String role = "USER";
    
    @Column(length = 20)
    private String status = "ACTIVE";
    
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
}

